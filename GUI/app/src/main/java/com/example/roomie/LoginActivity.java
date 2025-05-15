package com.example.roomie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        dbHelper = new DatabaseHelper(this);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Login logic
        loginButton.setOnClickListener(v -> {
            String email    = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection   = { DatabaseHelper.COLUMN_ID };
            String   selection    = DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " +
                    DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";
            String[] selectionArgs= { email, password };

            Cursor cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    projection,
                    selection,
                    selectionArgs,
                    null, null, null
            );

            boolean userExists = cursor.getCount() > 0;
            cursor.close();
            db.close();

            if (userExists) {
                long userId = dbHelper.getUserIdByEmail(email);
                SessionManager.init(this);
                SessionManager.get().setUserId(userId);
                // NEW: check profile completeness
                boolean complete = dbHelper.isProfileComplete(userId);
                Intent next;
                if (complete) {
                    // all set — go to main/profile page
                    next = new Intent(this, ProfileActivity.class);
                } else {
                    // missing first/last name (or other fields) — send to profile‐setup
                    next = new Intent(this, UserDetailsActivity.class);
                }
                startActivity(next);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
            }
        });


        // Navigate to registration
        View.OnClickListener goToRegister = v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        };

        registerButton.setOnClickListener(goToRegister);
        registerTextView.setOnClickListener(goToRegister);
    }
}
