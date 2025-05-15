// com/example/roomie/SearchHousesActivity.java
package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import java.util.*;

public class SearchHousesActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private List<House> houses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_houses);
        houses = getMockHouses();
        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        for (House h : houses) {
            Marker m = map.addMarker(new MarkerOptions()
                    .position(h.location)
                    .title(h.address)
                    .snippet("€" + (int)h.rent + "/mo"));
            m.setTag(h.id);
        }
        map.setOnMarkerClickListener(this);
        if (!houses.isEmpty()) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    houses.get(0).location, 12f));
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        long houseId = (Long)marker.getTag();
        Intent i = new Intent(this, HouseDetailActivity.class);
        i.putExtra("EXTRA_HOUSE_ID", houseId);
        startActivity(i);
        return true;
    }

    public List<House> getMockHouses() {
        return Arrays.asList(
                new House(1, "Lemesou 20, Patras", 180, 88, 3,
                        Arrays.asList("https://ex.com/1.jpg","https://ex.com/2.jpg"), "KwstasMav", "avatarURL",
                        new LatLng(38.2466,21.7345)),
                new House(2, "Agiou Nikolaou 5, Patras", 200, 95, 2,
                        Arrays.asList("https://ex.com/a1.jpg","https://ex.com/a2.jpg"), "whodis", "avatarURL",
                        new LatLng(38.2460,21.7320)),
                new House(3, "Sisinis 25, Patras", 400, 109, 4,
                        Arrays.asList("https://ex.com/b1.jpg","https://ex.com/b2.jpg"), "mikethefreak", "avatarURL",
                        new LatLng(38.2440,21.7300))
        );
    }
}
