package com.example.locationtracker;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<LocationModel> locationList = new ArrayList<>();
    private LocationDatabaseHelper dbHelper;

    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new LocationDatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = view.findViewById(R.id.locationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadLocationsFromDatabase();
        adapter = new LocationAdapter(getContext(), locationList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadLocationsFromDatabase() {
        locationList.clear();

        // Query database - sorting by time descending (newest first)
        Cursor cursor = dbHelper.getReadableDatabase().query(
                LocationDatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                LocationDatabaseHelper.COL_TIME + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(LocationDatabaseHelper.COL_ID));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationDatabaseHelper.COL_LAT));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationDatabaseHelper.COL_LON));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(LocationDatabaseHelper.COL_TIME));
                locationList.add(new LocationModel(id, lat, lon, timestamp));
            }
            cursor.close();
        }
    }
}