package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AuthenticateListingsActivity extends AppCompatActivity
        implements AuthenticateHouseAdapter.OnActionListener {

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
        adapter = new AuthenticateHouseAdapter(listingList, this);
        rvListings.setAdapter(adapter);

        // load from the DB
        loadListings();

        AdminNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }

    private void loadListings() {
        listingList.clear();
        // pull all listings (house + metadata)
        listingList.addAll(dbHelper.getAllHouseListings());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onApprove(HouseListing listing) {
        boolean ok = dbHelper.approveListing(listing);
        if (ok) {
            // remove from the pending list and refresh
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
    }

    @Override
    public void onDisapprove(HouseListing listing) {
        // e.g. mark as disapproved in DB, then remove
        // dbHelper.markListingDisapproved(listing.house.id);
        listingList.remove(listing);
        adapter.notifyDataSetChanged();

        Toast.makeText(
                this,
                "Disapproved listing at " + listing.house.address,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onViewDetail(HouseListing listing) {
        Intent i = new Intent(this, HouseDetailActivity.class);
        i.putExtra("EXTRA_HOUSE_ID", listing.house.id);
        startActivity(i);
    }
}
