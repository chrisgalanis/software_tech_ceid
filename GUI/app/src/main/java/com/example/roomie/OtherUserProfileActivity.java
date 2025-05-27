package com.example.roomie;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class OtherUserProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";

    private ImageView ivProfile;
    private TextView  tvNamePronounsAge, tvBudgetCity, tvInterests;
    private Button    btnViewHouse, btnReport;
    private DatabaseHelper dbHelper;
    private long      userId;
    private long      currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        // toolbar with back arrow
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

        // read the user ID to display
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
        // handle toolbar back click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfile() {
        // 1) Load profile image
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
            int age = u.getAge();

            StringBuilder sb = new StringBuilder();
            sb.append(u.firstName).append(" ").append(u.lastName);
            sb.append(",").append(age);
            sb.append(" • ").append(pronouns);

            tvNamePronounsAge.setText(sb.toString());

            tvBudgetCity.setText("Budget: €" + u.minBudget + "–" + u.maxBudget +" • City: " + u.city);
        }

        // 3) Interests
        List<String> ints = dbHelper.getUserInterests(userId);
        tvInterests.setText(
                ints.isEmpty()
                        ? "—"
                        : android.text.TextUtils.join(", ", ints)
        );

        // 4) View house button (stub)
        btnViewHouse.setOnClickListener(v -> {
            long houseId = dbHelper.getHouseIdByUserId(userId);
            if (houseId != -1) {
                startActivity(new Intent(this, HouseDetailActivity.class)
                        .putExtra("EXTRA_HOUSE_ID", houseId));
            } else {
                Toast.makeText(this,"This user doesn’t have a house listing",Toast.LENGTH_SHORT).show();
            }
        });
        // Report button: show input dialog
        btnReport.setOnClickListener(v -> showReportDialog());
    }

    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Report User");

        // multi-line input
        final EditText input = new EditText(this);
        input.setHint("Describe why you're reporting this user");
        input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        // Submit button
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String reportText = input.getText().toString().trim();
            if (reportText.isEmpty()) {
                Toast.makeText(this,
                        "Report text cannot be empty",
                        Toast.LENGTH_SHORT).show();
            } else {
                long reportId = dbHelper.insertReport(userId, reportText);
                if (reportId > 0) {
                    Toast.makeText(this,
                            "Report submitted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Failed to submit report",
                            Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }
}
