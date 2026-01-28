package com.example.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final Context context;
    private final List<LocationModel> locations;

    public LocationAdapter(Context context, List<LocationModel> locations) {
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        LocationModel location = locations.get(position);

        holder.tvSI.setText(String.format(Locale.getDefault(), "%02d", position + 1));
        holder.tvLat.setText(String.format(Locale.getDefault(), "Lat: %.6f", location.getLatitude()));
        holder.tvLon.setText(String.format(Locale.getDefault(), "Lon: %.6f", location.getLongitude()));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault());
        String formattedDateTime = sdf.format(new Date(location.getTimestamp()));
        holder.tvTime.setText(formattedDateTime);

        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewDetailsLocation.class);
            intent.putExtra("location_name", "Unknown");
            intent.putExtra("latitude", location.getLatitude());
            intent.putExtra("longitude", location.getLongitude());
            intent.putExtra("time", new java.text.SimpleDateFormat("hh:mm a, dd MMM yyyy", java.util.Locale.getDefault())
                    .format(new java.util.Date(location.getTimestamp())));
            intent.putExtra("temperature", "N/A");
            intent.putExtra("weather_desc", "N/A");
            intent.putExtra("weather_icon", R.drawable.baseline_sunny_snowing_24);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
    static class LocationViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView tvSI, tvLat, tvLon, tvTime;
        MaterialButton btnView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSI = itemView.findViewById(R.id.tvSI);
            tvLat = itemView.findViewById(R.id.tvLat);
            tvLon = itemView.findViewById(R.id.tvLon);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}