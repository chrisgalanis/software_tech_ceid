package com.example.roomie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

  private ImageView ivProfilePic;
  private TextView tvNameAge;
  private ImageButton btnSettings, btnEditProfile, btnAddHouse;
  private DatabaseHelper dbHelper;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    // make sure SessionManager.init() was called in LoginActivity
    currentUserId = SessionManager.get().getUserId();

    // bind views
    ivProfilePic = findViewById(R.id.ivProfile);
    tvNameAge = findViewById(R.id.tvNameAge);
    btnSettings = findViewById(R.id.btnSettings);
    btnEditProfile = findViewById(R.id.btnEditProfile);
    btnAddHouse = findViewById(R.id.btnAddHouse);

    dbHelper = new DatabaseHelper(this);

    // load data
    loadProfileData();

    // wire up bottom nav (use the same ID as in your layout XML!)
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_feed);

    // notifications button
    btnSettings.setOnClickListener(v -> startActivity(new Intent(this, WarningsActivity.class)));

    // edit profile page
    // btnEditProfile.setOnClickListener(v -> startActivity(new Intent(this,
    // EditProfileActivity.class)))

    // create listing button
    btnAddHouse.setOnClickListener(
        v -> startActivity(new Intent(this, CreateListingActivity.class)));
  }

  private void loadProfileData() {

    User u = dbHelper.getUserById(currentUserId);

    if (u != null) {
      String first = u.firstName;
      String last = u.lastName;
      int age = u.getAge();
      tvNameAge.setText(String.format("%s %s, %d", first, last, age));
      // profile pic
      Uri uri = Uri.parse(u.avatarUrl);
      Glide.with(this)
          .load(uri)
          .placeholder(R.drawable.ic_profile_placeholder)
          .error(R.drawable.ic_profile_placeholder)
          .into(ivProfilePic);
    }
  }
}
