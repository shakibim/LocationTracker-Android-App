package com.example.locationtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.materialswitch.MaterialSwitch;

public class HomeFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private MaterialSwitch locationSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        locationSwitch = view.findViewById(R.id.locationSwitch);

        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isTracking = sharedPreferences.getBoolean("isTracking", false);
        locationSwitch.setChecked(isTracking);

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkPermissionsAndStart();
            } else {
                stopService();
            }
        });
        return view;
    }

    private void checkPermissionsAndStart() {

        // Checking Fine location
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
            return;
        }

        // Checking Background location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

                // sending to settings if Cannot request via popup
                Toast.makeText(
                        requireContext(),
                        "Please enable 'Allow all the time' location access",
                        Toast.LENGTH_LONG
                ).show();

                Intent intent = new Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                );
                intent.setData(
                        android.net.Uri.parse("package:" + requireContext().getPackageName())
                );
                startActivity(intent);
                locationSwitch.setChecked(false);
                saveState(false);
                return;
            }
        }
        // Starting The Service If All Permissions Are Met
        startService();
    }


    private void startService() {
        saveState(true);
        Intent intent = new Intent(requireContext(), LocationService.class);
        ContextCompat.startForegroundService(requireContext(), intent);
    }

    private void stopService() {
        saveState(false);
        Intent intent = new Intent(requireContext(), LocationService.class);
        requireContext().stopService(intent);
    }

    private void saveState(boolean state) {
        sharedPreferences.edit().putBoolean("isTracking", state).apply();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                checkPermissionsAndStart(); //  rechecking background
            } else {
                locationSwitch.setChecked(false);
                saveState(false);
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}