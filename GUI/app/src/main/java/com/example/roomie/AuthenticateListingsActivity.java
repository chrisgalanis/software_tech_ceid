package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthenticateListingsActivity extends AppCompatActivity
        implements AuthenticateHouseAdapter.OnActionListener {

    private RecyclerView rvListings;
    private AuthenticateHouseAdapter adapter;
    private final List<House> houseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_listings);

        // 1) set up RecyclerView & adapter
        rvListings = findViewById(R.id.rvListings);
        rvListings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AuthenticateHouseAdapter(houseList, this);
        rvListings.setAdapter(adapter);

        // 2) populate hard-coded data
        loadHardcodedHouses();

        AdminNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home

        );
    }

    private void loadHardcodedHouses() {
        houseList.clear();

        houseList.add(new House(
                1,
                100,
                "123 Main St, Springfield",
                1200.00,
                750.0,
                2,
                Arrays.asList(
                        "https://via.placeholder.com/400x300.png?text=House+1"
                ),
                "Alice Johnson",
                "https://via.placeholder.com/100.png?text=AJ",
                new LatLng(37.7749, -122.4194)
        ));

        houseList.add(new House(
                2,
                101,
                "456 Elm St, Metropolis",
                1500.00,
                900.0,
                5,
                Arrays.asList(
                        "https://via.placeholder.com/400x300.png?text=House+2"
                ),
                "Bob Smith",
                "https://via.placeholder.com/100.png?text=BS",
                new LatLng(40.7128, -74.0060)
        ));

        houseList.add(new House(
                3,
                102,
                "789 Oak Ave, Gotham",
                1000.00,
                600.0,
                1,
                Arrays.asList(
                        "https://via.placeholder.com/400x300.png?text=House+3"
                ),
                "Carol Lee",
                "https://via.placeholder.com/100.png?text=CL",
                new LatLng(34.0522, -118.2437)
        ));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onApprove(House house) {
        houseList.remove(house);
        adapter.notifyDataSetChanged();
        Toast.makeText(this,
                "Approved listing at " + house.address,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDisapprove(House house) {
        houseList.remove(house);
        adapter.notifyDataSetChanged();
        Toast.makeText(this,
                "Disapproved listing at " + house.address,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onViewDetail(House house) {
        Toast.makeText(this,
                "Viewing details for " + house.address,
                Toast.LENGTH_SHORT
        ).show();
    }
}
