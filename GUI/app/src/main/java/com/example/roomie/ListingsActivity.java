package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {

  private RecyclerView recycler;
  private List<HouseListing> allListings; // now HouseListing
  private ListingsAdapter adapter;
  private DatabaseHelper dbHelper;
  private View tvEmpty;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listings);

    BottomNavigationHelper.setup(findViewById(R.id.bottom_navigation), this, R.id.nav_home);

    recycler = findViewById(R.id.recyclerHouses);
    recycler.setLayoutManager(new LinearLayoutManager(this));

    allListings = new ArrayList<>();
    adapter =
        new ListingsAdapter(
            allListings,
            listing -> {
              // now click gives you full listing
              startActivity(
                  new Intent(this, HouseDetailActivity.class)
                      .putExtra("EXTRA_HOUSE_ID", listing.house.id));
            });
    recycler.setAdapter(adapter);

    tvEmpty = findViewById(R.id.tvEmpty);
    dbHelper = new DatabaseHelper(this);

    loadHouses();
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadHouses();
  }

  private void loadHouses() {
    // 1) fetch every listing
    List<HouseListing> listings = dbHelper.getAllHouseListings();

    // 2) update adapter dataset
    allListings.clear();
    allListings.addAll(listings);
    adapter.notifyDataSetChanged();

    // 3) empty-state
    tvEmpty.setVisibility(allListings.isEmpty() ? View.VISIBLE : View.GONE);
  }
}
