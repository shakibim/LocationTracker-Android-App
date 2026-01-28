package com.example.locationtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignIn extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton signinButton, signupButton, goButton;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.sign_in_activity);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.blue_nav));

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signinButton = findViewById(R.id.signinButton); // "Sign In" button
        signupButton = findViewById(R.id.signupButton); // "Sign Up" button to navigate
        goButton= findViewById(R.id.goButton);

        userDatabaseHelper = new UserDatabaseHelper(this);

        signinButton.setOnClickListener(v -> signInUser());

        goButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        });
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = password;

        // Checking if user exists
        if (!userDatabaseHelper.checkUserExists(email)) {
            Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validating user credentials
        if (!userDatabaseHelper.validateUser(email, hashedPassword)) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

        // Saving logged-in user ID in SharedPreferences
        int userId = userDatabaseHelper.getUserIdByEmail(email); // make this function in your DB helper
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit().putInt("logged_in_user_id", userId).apply();

        // sending to MainActivity
        Intent intent = new Intent(SignIn.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
