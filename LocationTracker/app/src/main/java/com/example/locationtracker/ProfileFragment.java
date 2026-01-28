package com.example.locationtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private TextInputEditText nameEditText, emailEditText, passwordEditText;
    private MaterialButton updateButton, logoutButton;

    private UserDatabaseHelper databaseHelper;
    private int loggedInUserId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Init views
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        updateButton = view.findViewById(R.id.updateButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        databaseHelper = new UserDatabaseHelper(requireContext());

        loadUserFromPreferences();

        updateButton.setOnClickListener(v -> updateProfile());
        logoutButton.setOnClickListener(v -> {
            logoutUser();
            Intent i = new Intent(requireContext(), SignIn.class);
            startActivity(i);
        });

        return view;
    }
    // Loading users
    private void loadUserFromPreferences() {
        SharedPreferences prefs =
                requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);

        loggedInUserId = prefs.getInt("logged_in_user_id", -1);
        if (loggedInUserId == -1) {
            clearFields();
            disableProfile();
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getUserById(loggedInUserId);

        if (cursor.moveToFirst()) {
            nameEditText.setText(cursor.getString(0));
            emailEditText.setText(cursor.getString(1));
            passwordEditText.setText(cursor.getString(2));
        }

        cursor.close();
        enableProfile();
    }
    //update user
    private void updateProfile() {
        if (loggedInUserId == -1) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean updated = databaseHelper.updateUser(
                loggedInUserId, name, email, password
        );

        if (updated) {
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
    // logout
    private void logoutUser() {
        SharedPreferences prefs =
                requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        loggedInUserId = -1;
        clearFields();
        disableProfile();
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
    }
    private void clearFields() {
        nameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
    }
    private void disableProfile() {
        nameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        updateButton.setEnabled(false);
    }
    private void enableProfile() {
        nameEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        updateButton.setEnabled(true);
    }
}