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

  private CheckBox cb90s, cbHarry, cbHome, cbHockey;
  private Button btnSkip, btnContinue;

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

    cb90s = findViewById(R.id.cb90sKid);
    cbHarry = findViewById(R.id.cbHarryPotter);
    cbHome = findViewById(R.id.cbHomeWorkout);
    cbHockey = findViewById(R.id.cbHockey);
    btnSkip = findViewById(R.id.btnSkip);
    btnContinue = findViewById(R.id.btnContinueStep2);

    btnSkip.setOnClickListener(
        v -> {
          Toast.makeText(this, "No interests selected", Toast.LENGTH_SHORT).show();
          finish();
        });

    btnContinue.setOnClickListener(
        v -> {
          List<String> selected = new ArrayList<>();
          if (cb90s.isChecked()) selected.add("90s Kid");
          if (cbHarry.isChecked()) selected.add("Harry Potter");
          if (cbHome.isChecked()) selected.add("Home Workout");
          if (cbHockey.isChecked()) selected.add("Hockey");

          dbHelper.insertUserInterests(userId, selected);

          Toast.makeText(this, "Saved interests: " + selected, Toast.LENGTH_LONG).show();
          // now go to your main screen:
          startActivity(new Intent(this, ProfileActivity.class));
          finish();
        });
  }
}
