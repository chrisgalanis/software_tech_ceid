package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AuthenticateListingsActivity extends AppCompatActivity {

    private RecyclerView rvListings;
    private AuthenticateHouseAdapter adapter;
    private DatabaseHelper dbHelper;
    private final List<HouseListing> listingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_listings);

        // DB helper
        dbHelper = new DatabaseHelper(this);

        // RecyclerView + adapter
        rvListings = findViewById(R.id.rvListings);
        rvListings.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with View + Approve/Disapprove callbacks
        adapter = new AuthenticateHouseAdapter(
                this,
                dbHelper,
                listingList,
                listing -> {
                    // View detail
                    Intent i = new Intent(this, HouseDetailActivity.class);
                    i.putExtra("EXTRA_HOUSE_ID", listing.house.id);
                    startActivity(i);
                },
                listing -> {
                    // Approve action
                    boolean ok = dbHelper.approveListing(listing);
                    if (ok) {
                        int pos = listingList.indexOf(listing);
                        if (pos >= 0) {
                            listingList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                        Toast.makeText(
                                this,
                                "Approved listing at " + listing.house.address,
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(this, "Failed to approve listing.", Toast.LENGTH_SHORT).show();
                    }
                },
                listing -> {
                    // Disapprove action
                    boolean ok = dbHelper.disapproveListing(listing);
                    if (ok) {
                        int pos = listingList.indexOf(listing);
                        if (pos >= 0) {
                            listingList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }
                    Toast.makeText(
                            this,
                            "Disapproved listing at " + listing.house.address,
                            Toast.LENGTH_SHORT
                    ).show();

                    Warning warning = new Warning(0, listing.house.ownerId, "Your House listing has been disapproved by the system admin", "PENDING", null);
                    dbHelper.insertWarning(warning);
                });

        rvListings.setAdapter(adapter);

        // Load initial listings
        loadListings();

        // Bottom navigation setup
        AdminNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }

    private void loadListings() {
        listingList.clear();
        listingList.addAll(dbHelper.getAllHouseListings());
        adapter.notifyDataSetChanged();
    }
}
