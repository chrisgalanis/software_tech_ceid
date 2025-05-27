package com.example.roomie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfilePic;
    private TextView  tvNameAge;
    private ImageButton btnSettings, btnEditProfile, btnAddHouse;
    private DatabaseHelper dbHelper;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // make sure SessionManager.init(...) was called in LoginActivity or Application
        currentUserId = SessionManager.get().getUserId();
        if (currentUserId < 0) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // bind views
        ivProfilePic   = findViewById(R.id.ivProfilePic);
        tvNameAge      = findViewById(R.id.tvNameAge);
        btnSettings    = findViewById(R.id.btnSettings);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnAddHouse    = findViewById(R.id.btnAddHouse);

        dbHelper = new DatabaseHelper(this);

        // load data
        loadProfileData();

        // wire up bottom nav (use the same ID as in your layout XML!)
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_feed
        );

    // click listeners (optional)
     btnSettings.setOnClickListener(v -> startActivity(new Intent(this, WarningsActivity.class)));
    // btnEditProfile.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
    btnAddHouse.setOnClickListener(v -> {
      Toast.makeText(this, "Add‐House clicked!", Toast.LENGTH_SHORT).show();
      startActivity(new Intent(this, CreateListingActivity.class));
    });
  }

    private void loadProfileData() {
        // photos
        List<String> photos = dbHelper.getUserPhotos(currentUserId);
        if (!photos.isEmpty()) {
            Uri uri = Uri.parse(photos.get(0));
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(ivProfilePic);
        } else {
            ivProfilePic.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // name + age
        User u = dbHelper.getUserById(currentUserId);

        if (u!=null) {
            String first    = u.firstName;
            String last     = u.lastName;
            int age         = u.getAge();
            tvNameAge.setText(String.format("%s %s, %d", first, last, age));
        }
    }

}
