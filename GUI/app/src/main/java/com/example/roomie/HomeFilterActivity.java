package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFilterActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private EditText etAddress, etPriceFrom, etPriceTo;
    private Button btnSearch;
    private GoogleMap mMap;
    private DatabaseHelper dbHelper;
    private List<HouseListing> allHL;
    private List<House> allHouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_house);

        dbHelper = new DatabaseHelper(this);

        etAddress   = findViewById(R.id.etAddress);
        etPriceFrom = findViewById(R.id.etPriceFrom);
        etPriceTo   = findViewById(R.id.etPriceTo);
        btnSearch   = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            Intent i = new Intent(this, ListingsActivity.class);
            i.putExtra("addr",      etAddress.getText().toString().trim());
            i.putExtra("priceFrom", parseDouble(etPriceFrom));
            i.putExtra("priceTo",   parseDouble(etPriceTo));
            startActivity(i);
        });

        // set up map fragment
        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }

        // “+” FAB to create a new house listing
        Button btnCreateListing = findViewById(R.id.btnCreateListing);
        btnCreateListing.setOnClickListener(v ->
                startActivity(new Intent(this, CreateListingActivity.class))
        );

        BottomNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }

    private double parseDouble(EditText e) {
        try {
            String s = e.getText().toString().trim();
            return s.isEmpty() ? -1 : Double.parseDouble(s);
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // load all listings and drop markers
        allHL    = dbHelper.getApprovedHouseListings();
        allHouses = new ArrayList<>(allHL.size());
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (HouseListing hl : allHL) {
            House h = hl.house;
            allHouses.add(h);
            Marker mk = mMap.addMarker(new MarkerOptions()
                    .position(h.getLocation())
                    .title(h.address)
                    .snippet("€" + (int) h.rent + "/mo"));
            if (mk != null) mk.setTag(h.id);
            bounds.include(h.getLocation());
        }

        mMap.setOnMarkerClickListener(this);

        // zoom to fit all markers
        if (!allHouses.isEmpty()) {
            int padding = 100; // px
            mMap.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(bounds.build(), padding)
            );
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof Long) {
            long houseId = (Long) tag;
            Intent i = new Intent(this, HouseDetailActivity.class);
            i.putExtra("EXTRA_HOUSE_ID", houseId);
            startActivity(i);
        }
        return true;
    }
}
