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
import java.util.List;

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

    // make sure SessionManager.init(...) was called in LoginActivity or Application
    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    // bind views
    ivProfilePic = findViewById(R.id.ivProfilePic);
    tvNameAge = findViewById(R.id.tvNameAge);
    btnSettings = findViewById(R.id.btnSettings);
    btnEditProfile = findViewById(R.id.btnEditProfile);
    btnAddHouse = findViewById(R.id.btnAddHouse);

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
    // btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
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
    Cursor c =
        dbHelper
            .getReadableDatabase()
            .query(
                DatabaseHelper.TABLE_USERS,
                new String[] {
                  DatabaseHelper.COLUMN_USER_FIRSTNAME,
                  DatabaseHelper.COLUMN_USER_LASTNAME,
                  DatabaseHelper.COLUMN_USER_BIRTHDAY
                },
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[] {String.valueOf(currentUserId)},
                null,
                null,
                null);

    if (c.moveToFirst()) {
      String first = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FIRSTNAME));
      String last = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_LASTNAME));
      String birthday = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_BIRTHDAY));
      int age = calculateAge(birthday);
      tvNameAge.setText(String.format("%s %s, %d", first, last, age));
    }
    c.close();
  }

  private int calculateAge(String birthIso) {
    if (birthIso == null || !birthIso.matches("\\d{4}-\\d{2}-\\d{2}")) {
      // Not a valid YYYY-MM-DD
      return 0;
    }
    try {
      String[] parts = birthIso.split("-");
      int year = Integer.parseInt(parts[0]);
      int month = Integer.parseInt(parts[1]) - 1; // Calendar months are 0-based
      int day = Integer.parseInt(parts[2]);

      java.util.Calendar dob = java.util.Calendar.getInstance();
      dob.setLenient(false);
      dob.set(year, month, day);

      java.util.Calendar today = java.util.Calendar.getInstance();
      int age = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR);

      // if today’s date is before birthday this year, subtract 1
      if (today.get(java.util.Calendar.MONTH) < dob.get(java.util.Calendar.MONTH)
          || (today.get(java.util.Calendar.MONTH) == dob.get(java.util.Calendar.MONTH)
              && today.get(java.util.Calendar.DAY_OF_MONTH)
                  < dob.get(java.util.Calendar.DAY_OF_MONTH))) {
        age--;
      }
      return Math.max(age, 0);
    } catch (Exception e) {
      // parse or invalid date (e.g. Feb 30)
      return 0;
    }
  }
}
