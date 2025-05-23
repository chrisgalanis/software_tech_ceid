package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchHousesActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap     map;
    private List<House>   houses;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_houses);

        // ——— 1) init DB helper and load real houses
        dbHelper = new DatabaseHelper(this);
        houses   = dbHelper.getAllHouses();   // SELECT * FROM houses

        // ——— 2) Bottom nav (optional)
        BottomNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_search
        );

        // ——— 3) Map fragment wiring
        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map_fragment);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        for (House h : houses) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(h.location)
                    .title(h.address)
                    .snippet("€" + (int)h.rent + "/mo"));
            if (marker != null) marker.setTag(h.id);
        }

        map.setOnMarkerClickListener(this);

        // Zoom camera to show all (or just center on first)
        if (!houses.isEmpty()) {
            LatLng first = houses.get(0).location;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 12f));
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
