package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button   loginButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Bind views
        backButton        = findViewById(R.id.adminBackButton);
        usernameEditText  = findViewById(R.id.adminUsernameEditText);
        passwordEditText  = findViewById(R.id.adminPasswordEditText);
        loginButton       = findViewById(R.id.adminLoginButton);

        // Back arrow just finishes this Activity
        backButton.setOnClickListener(v -> finish());

        // Handle admin login
        loginButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: replace this check with real admin authentication (DB, API, etc)
            if (user.equals("admin") && pass.equals("admin123")) {
                // on success, navigate to Admin dashboard/activity
                Intent intent = new Intent(AdminLoginActivity.this, AdminReportsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
