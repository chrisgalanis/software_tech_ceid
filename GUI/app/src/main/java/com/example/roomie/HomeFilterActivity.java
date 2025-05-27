package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class HomeFilterActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private EditText etAddress, etPriceFrom, etPriceTo, etAreaFrom, etAreaTo;
    private Spinner  spinnerFloor;
    private Button   btnSearch;
    private GoogleMap      mMap;
    private DatabaseHelper dbHelper = new DatabaseHelper(this);
    private List<House> allHouses;

    private List<HouseListing> allHL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_house);

        etAddress   = findViewById(R.id.etAddress);
        etPriceFrom = findViewById(R.id.etPriceFrom);
        etPriceTo   = findViewById(R.id.etPriceTo);
        etAreaFrom  = findViewById(R.id.etAreaFrom);
        etAreaTo    = findViewById(R.id.etAreaTo);
        spinnerFloor= findViewById(R.id.spinnerFloor);
        btnSearch   = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            Intent i = new Intent(this, ListingsActivity.class);
            i.putExtra("addr",      etAddress.getText().toString().trim());
            i.putExtra("priceFrom", parseDouble(etPriceFrom));
            i.putExtra("priceTo",   parseDouble(etPriceTo));
            i.putExtra("areaFrom",  parseDouble(etAreaFrom));
            i.putExtra("areaTo",    parseDouble(etAreaTo));
            i.putExtra("floor",     spinnerFloor.getSelectedItemPosition());
            startActivity(i);
        });

        // ——— set up map fragment
        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }

        // ——— “+” FAB to create a new house listing
        FloatingActionButton fab = findViewById(R.id.fabAddListing);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CreateListingActivity.class))
        );

        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }

    private double parseDouble(EditText e) {
        try { return Double.parseDouble(e.getText().toString()); }
        catch (Exception ex){ return -1; }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // load *all* houses and drop markers
        allHL= dbHelper.getAllHouseListings();
        allHouses=new ArrayList<>();
        for(HouseListing hl: allHL){
            allHouses.add(hl.house);
        }

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (House h : allHouses) {
            Marker mk = mMap.addMarker(new MarkerOptions()
                    .position(h.getLocation())
                    .title(h.address)
                    .snippet("€"+ (int)h.rent +"/mo"));
            if (mk != null) mk.setTag(h.id);
            bounds.include(h.getLocation());
        }

        mMap.setOnMarkerClickListener(this);

        // zoom to fit
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
            long houseId = (Long)tag;
            Intent i = new Intent(this, HouseDetailActivity.class);
            i.putExtra("EXTRA_HOUSE_ID", houseId);
            startActivity(i);
        }
        return true;
    }
}