package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {
    private long userId;
    private DatabaseHelper dbHelper;

    // all the checkboxes from your XML
    private CheckBox
            cb90s, cbHarry, cbHome, cbHockey,
            cbCooking, cbTravel, cbMusic, cbGaming,
            cbReading, cbTechnology, cbArt, cbMovies;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        dbHelper = new DatabaseHelper(this);
        userId = SessionManager.get().getUserId();
        if (userId < 0) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // wire up every CheckBox
        cb90s        = findViewById(R.id.cb90sKid);
        cbHarry      = findViewById(R.id.cbHarryPotter);
        cbHome       = findViewById(R.id.cbHomeWorkout);
        cbHockey     = findViewById(R.id.cbHockey);
        cbCooking    = findViewById(R.id.cbCooking);
        cbTravel     = findViewById(R.id.cbTravel);
        cbMusic      = findViewById(R.id.cbMusic);
        cbGaming     = findViewById(R.id.cbGaming);
        cbReading    = findViewById(R.id.cbReading);
        cbTechnology = findViewById(R.id.cbTechnology);
        cbArt        = findViewById(R.id.cbArt);
        cbMovies     = findViewById(R.id.cbMovies);

        btnContinue  = findViewById(R.id.btnContinueStep2);
        btnContinue.setOnClickListener(v -> {
            List<String> selected = new ArrayList<>();

            if (cb90s.isChecked())        selected.add("Books");
            if (cbHarry.isChecked())      selected.add("Star Wars");
            if (cbHome.isChecked())       selected.add("Home Workout");
            if (cbHockey.isChecked())     selected.add("Dancing");
            if (cbCooking.isChecked())    selected.add("Cooking");
            if (cbTravel.isChecked())     selected.add("Travel");
            if (cbMusic.isChecked())      selected.add("Music");
            if (cbGaming.isChecked())     selected.add("Gaming");
            if (cbReading.isChecked())    selected.add("Reading");
            if (cbTechnology.isChecked()) selected.add("Technology");
            if (cbArt.isChecked())        selected.add("Art");
            if (cbMovies.isChecked())     selected.add("Movies");

            dbHelper.insertUserInterests(userId, selected);

            Toast.makeText(
                    this,
                    "Saved interests: " + selected,
                    Toast.LENGTH_LONG
            ).show();

            // Navigate to your next screen
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }
}
