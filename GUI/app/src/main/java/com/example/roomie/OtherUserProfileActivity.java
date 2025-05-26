package com.example.roomie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";

    private ImageView ivProfile;
    private TextView  tvNamePronounsAge, tvBudgetCity, tvInterests;
    private Button    btnViewHouse, btnReport;
    private DatabaseHelper dbHelper;
    private long      userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        // set up toolbar with back arrow
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // bind views
        ivProfile         = findViewById(R.id.ivProfile);
        tvNamePronounsAge = findViewById(R.id.tvNamePronounsAge);
        tvBudgetCity      = findViewById(R.id.tvBudgetCity);
        tvInterests       = findViewById(R.id.tvInterests);
        btnViewHouse      = findViewById(R.id.btnViewHouse);
        btnReport         = findViewById(R.id.btnReport);

        // get passed userId
        userId = getIntent().getLongExtra(EXTRA_USER_ID, -1);
        if (userId < 0) {
            Toast.makeText(this, "No user to display", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        loadProfile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle toolbar back button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfile() {
        List<String> photos = dbHelper.getUserPhotos(userId);
        if (!photos.isEmpty()) {
            Uri uri = Uri.parse(photos.get(0));
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(ivProfile);
        } else {
            ivProfile.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // 2) Basic info
        User u = dbHelper.getUserById(userId);
        if (u != null) {
            String pronouns = u.pronounsForGender(u.gender);
            int age = u.calculateAge(u.birthday);

            StringBuilder sb = new StringBuilder();
            sb.append(u.firstName).append(" ").append(u.lastName);
            if (age > 0) {
                sb.append(", ").append(age);
            }
            if (pronouns != null) {
                sb.append(" • ").append(pronouns);
            }
            tvNamePronounsAge.setText(sb.toString());

            tvBudgetCity.setText("Budget: €" + u.minBudget +"-"+u.maxBudget+ " • City: " + u.city);
        }

        // 3) Interests
        List<String> ints = dbHelper.getUserInterests(userId);
        tvInterests.setText(
                ints.isEmpty()
                        ? "—"
                        : android.text.TextUtils.join(", ", ints)
        );

        // 4) Buttons
        btnViewHouse.setOnClickListener(v -> {
//            Intent i = new Intent(this, HouseDetailActivity.class);
//            i.putExtra(HouseDetailActivity.EXTRA_OWNER_ID, userId);
//            startActivity(i);
        });

        btnReport.setOnClickListener(v -> {
            // reporting logic here
            Toast.makeText(this, "User reported", Toast.LENGTH_SHORT).show();
        });
    }
}
