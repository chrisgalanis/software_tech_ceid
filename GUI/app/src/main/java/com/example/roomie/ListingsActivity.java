package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {

  private RecyclerView recycler;
  private List<HouseListing> allListings;
  private AuthenticateHouseAdapter adapter;
  private DatabaseHelper dbHelper;
  private View tvEmpty;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listings);

    // ─── Toolbar setup for back navigation ────────────────────────────
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      // Optional: hide title if desired
      // getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    toolbar.setNavigationOnClickListener(v -> finish());

    // ─── Bottom navigation ────────────────────────────────────────────
    BottomNavigationHelper.setup(
            findViewById(R.id.bottom_navigation),
            this, R.id.nav_home
    );

    // ─── RecyclerView setup ────────────────────────────────────────────
    recycler = findViewById(R.id.recyclerHouses);
    recycler.setLayoutManager(new LinearLayoutManager(this));

    allListings = new ArrayList<>();
    dbHelper = new DatabaseHelper(this);

    adapter = new AuthenticateHouseAdapter(
            this,
            dbHelper,
            allListings,
            listing -> {
              // View full listing
              startActivity(new Intent(this, HouseDetailActivity.class)
                      .putExtra("EXTRA_HOUSE_ID", listing.house.id)
              );
            },
            null,  // no approve button
            null   // no disapprove button
    );

    recycler.setAdapter(adapter);

    // ─── Empty state view ─────────────────────────────────────────────
    tvEmpty = findViewById(R.id.tvEmpty);
    loadHouses();
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadHouses();
  }

  private void loadHouses() {
    List<HouseListing> listings = dbHelper.getAllHouseListings();
    allListings.clear();
    allListings.addAll(listings);
    adapter.notifyDataSetChanged();

    tvEmpty.setVisibility(
            allListings.isEmpty() ? View.VISIBLE : View.GONE
    );
  }
}