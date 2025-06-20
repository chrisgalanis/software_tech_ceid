package com.example.roomie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
  private DatabaseHelper dbHelper;
  private EditText emailEditText;
  private EditText passwordEditText;
  private EditText confirmPasswordEditText;
  private Button registerButton;
  private TextView loginTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    // Initialize views
    dbHelper = new DatabaseHelper(this);
    emailEditText = findViewById(R.id.registerEmailEditText);
    passwordEditText = findViewById(R.id.registerPasswordEditText);
    confirmPasswordEditText = findViewById(R.id.registerConfirmPasswordEditText);
    registerButton = findViewById(R.id.registerButton);
    loginTextView = findViewById(R.id.loginTextView);

    // Set click listeners
    registerButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            // Basic input validation (you'll likely want more robust validation)
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
              Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT)
                  .show();
              return;
            }

            if (password.equals(confirmPassword)) {
              SQLiteDatabase db = dbHelper.getWritableDatabase();
              ContentValues values = new ContentValues();
              values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
              values.put(
                  DatabaseHelper.COLUMN_USER_PASSWORD, password); // In a real app, hash this!

              long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);
              db.close();

              if (newRowId != -1) {
                Toast.makeText(
                        RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT)
                    .show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
              } else {
                Toast.makeText(
                        RegisterActivity.this, "Error during registration.", Toast.LENGTH_SHORT)
                    .show();
              }
            } else {
              confirmPasswordEditText.setError("Passwords do not match.");
            }

            // Optionally, navigate back to the login screen after successful registration
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the register activity
          }
        });

    loginTextView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // Navigate back to the login activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the register activity
          }
        });
  }
}
