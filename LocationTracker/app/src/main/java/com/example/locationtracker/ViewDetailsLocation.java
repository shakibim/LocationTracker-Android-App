package com.example.locationtracker;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewDetailsLocation extends AppCompatActivity {

    private TextView tvLocationName, tvLatLng, tvTime, tvTemperature, tvWeatherDesc;
    private ImageView imgWeatherIcon;
    private MapView osmMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), getSharedPreferences("osmdroid", MODE_PRIVATE));
        setContentView(R.layout.activity_view_details_location);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.blue_nav));
        tvLocationName = findViewById(R.id.tvLocationName);
        tvLatLng = findViewById(R.id.tvLatLng);
        tvTime = findViewById(R.id.tvTime);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);
        osmMap = findViewById(R.id.osmMap);

        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        String time = getIntent().getStringExtra("time");
        String temperature = getIntent().getStringExtra("temperature");
        String weatherDesc = getIntent().getStringExtra("weather_desc");
        int weatherIcon = getIntent().getIntExtra("weather_icon", R.drawable.baseline_sunny_snowing_24);
        getLocationName(latitude,longitude);
        getWeather(latitude,longitude);

        tvLatLng.setText("Lat: " + latitude + ", Lon: " + longitude);
        tvTime.setText(time);
        tvTemperature.setText(temperature);
        tvWeatherDesc.setText(weatherDesc);
        imgWeatherIcon.setImageResource(weatherIcon);

        osmMap.setMultiTouchControls(true);
        osmMap.getController().setZoom(15.0);
        osmMap.getController().setCenter(new org.osmdroid.util.GeoPoint(latitude, longitude));

        Marker marker = new Marker(osmMap);
        marker.setPosition(new org.osmdroid.util.GeoPoint(latitude, longitude));
        marker.setTitle("");
        osmMap.getOverlays().add(marker);
        osmMap.invalidate();
    }
    private void getLocationName(double lat, double lon) {
       // creating separate thread for sending API request
        new Thread(() -> {
            try {
                String urlStr = "https://nominatim.openstreetmap.org/reverse?format=json"
                        + "&lat=" + lat
                        + "&lon=" + lon;

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "LocationTrackerApp"); // REQUIRED

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject json = new JSONObject(response.toString());

                String locationName = json.getString("display_name");

                // Update UI on main thread
                runOnUiThread(() -> {
                    if (tvLocationName != null) {
                        tvLocationName.setText(locationName);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void getWeather(double lat, double lon) {
        // creating separate thread for sending API request

        new Thread(() -> {
            try {
                String urlStr =
                        "https://api.open-meteo.com/v1/forecast"
                                + "?latitude=" + lat
                                + "&longitude=" + lon
                                + "&current_weather=true";

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject current = json.getJSONObject("current_weather");

                double temperature = current.getDouble("temperature");
                double windSpeed = current.getDouble("windspeed");
                int weatherCode = current.getInt("weathercode");

                String condition = getWeatherCondition(weatherCode);
                //updating UI on main thread
                runOnUiThread(() -> {
                    tvTemperature.setText(temperature + " °C");
                    tvWeatherDesc.setText(condition + " | Wind: " + windSpeed + " km/h");
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String getWeatherCondition(int code) {
        if (code == 0) return "Clear sky";
        if (code <= 3) return "Partly cloudy";
        if (code <= 48) return "Foggy";
        if (code <= 67) return "Rainy";
        if (code <= 77) return "Snow";
        if (code <= 99) return "Thunderstorm";
        return "Unknown";
    }
}
