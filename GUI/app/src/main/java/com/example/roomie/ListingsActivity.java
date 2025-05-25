package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListingsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private RecyclerView          recycler;
    private GoogleMap             mMap;
    private List<House>           allHouses;
    private ListingsAdapter       adapter;
    private DatabaseHelper        dbHelper;
    private SupportMapFragment    mapFrag;
    private View                  tvEmpty;    // optional “no results” view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        // 1) Bottom nav setup
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this, R.id.nav_home
        );

        // 2) RecyclerView + adapter
        recycler  = findViewById(R.id.recyclerHouses);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        allHouses = new ArrayList<>();
        adapter   = new ListingsAdapter(allHouses, house -> {
            startActivity(new Intent(this, HouseDetailActivity.class)
                    .putExtra("EXTRA_HOUSE_ID", house.id));
        });
        recycler.setAdapter(adapter);

        // (optional) find your empty-state TextView
        tvEmpty = findViewById(R.id.tvEmpty);

        // 3) Map fragment
        mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }

        // 4) DB helper
        dbHelper = new DatabaseHelper(this);

        // 5) initial load
        loadHouses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh every time we return here
        loadHouses();
    }

    /** Fetches from SQLite, updates both RecyclerView & map markers */
    private void loadHouses() {
        List<House> fresh = dbHelper.getAllHouses();
        allHouses.clear();
        allHouses.addAll(fresh);
        adapter.notifyDataSetChanged();

        // show/hide an empty-state view if you have one
        if (tvEmpty != null) {
            tvEmpty.setVisibility(allHouses.isEmpty()
                    ? View.VISIBLE
                    : View.GONE);
        }

        // update map markers
        if (mMap != null) {
            mMap.clear();
            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            for (House h : allHouses) {
                LatLng pos = h.location;
                Marker mk = mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(h.address)
                        .snippet("€" + (int)h.rent + "/mo"));
                if (mk != null) mk.setTag(h.id);
                bounds.include(pos);
            }
            // zoom to fit all markers
            if (!allHouses.isEmpty()) {
                int padding = 100;
                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngBounds(bounds.build(), padding)
                );
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // when you click a marker, go to detail
        mMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof Long) {
                long houseId = (Long) tag;
                startActivity(new Intent(this, HouseDetailActivity.class)
                        .putExtra("EXTRA_HOUSE_ID", houseId));
            }
            return true;
        });

        // if map is ready after initial load, re-draw markers
        loadHouses();
    }
}
