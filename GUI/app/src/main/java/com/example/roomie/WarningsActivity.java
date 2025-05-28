package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class WarningsActivity extends AppCompatActivity {

  private RecyclerView rv;
  private WarningAdapter adapter;
  private DatabaseHelper dbHelper;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_warnings);
    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    // toolbar
    Toolbar tb = findViewById(R.id.toolbar);
    setSupportActionBar(tb);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_feed);

    dbHelper = new DatabaseHelper(this);

    rv = findViewById(R.id.warningsRecyclerView);
    rv.setLayoutManager(new LinearLayoutManager(this));

    loadWarnings();
  }

  private void loadWarnings() {
    List<Warning> list = dbHelper.getWarningsForUser(currentUserId);
    adapter = new WarningAdapter(this, list);
    rv.setAdapter(adapter);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
