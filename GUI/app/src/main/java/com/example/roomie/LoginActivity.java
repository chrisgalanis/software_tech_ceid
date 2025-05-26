package com.example.roomie;

import android.content.Intent;
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
    private TextView adminLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        dbHelper           = new DatabaseHelper(this);
        emailEditText      = findViewById(R.id.emailEditText);
        passwordEditText   = findViewById(R.id.passwordEditText);
        loginButton        = findViewById(R.id.loginButton);
        registerButton     = findViewById(R.id.registerButton);
        registerTextView   = findViewById(R.id.registerTextView);
        adminLoginTextView = findViewById(R.id.adminLoginTextView);

        // LOGIN as normal user
        loginButton.setOnClickListener(v -> {
            String email    = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] projection    = { DatabaseHelper.COLUMN_ID };
            String   selection     = DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND "
                    + DatabaseHelper.COLUMN_USER_PASSWORD + " = ?";
            String[] selectionArgs = { email, password };

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

                // check profile completeness
                boolean complete = dbHelper.isProfileComplete(userId);
                Intent next;
                if (complete) {
                    next = new Intent(this, ProfileActivity.class);
                } else {
                    next = new Intent(this, UserDetailsActivity.class);
                }
                startActivity(next);
                finish();

            } else {
                Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
            }
        });

        // GO TO user registration
        View.OnClickListener goToRegister = v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        };
        registerButton.setOnClickListener(goToRegister);
        registerTextView.setOnClickListener(goToRegister);

        // NEW: GO TO admin login
        adminLoginTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
        });
    }
}
