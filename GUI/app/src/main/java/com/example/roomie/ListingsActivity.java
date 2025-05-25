package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class ListingsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private RecyclerView recycler;
  private GoogleMap mMap;
  private List<House> allHouses;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listings);

    // 1) Bottom nav
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_home);

    // 2) RecyclerView setup
    recycler = findViewById(R.id.recyclerHouses);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    allHouses = new SearchHousesActivity().getMockHouses();
    ListingsAdapter adapter =
        new ListingsAdapter(
            allHouses,
            house -> {
              Intent i = new Intent(this, HouseDetailActivity.class);
              i.putExtra("EXTRA_HOUSE_ID", house.id);
              startActivity(i);
            });
    recycler.setAdapter(adapter);

    // 3) Map fragment setup
    SupportMapFragment mapFrag =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
    if (mapFrag != null) {
      mapFrag.getMapAsync(this);
    }
  }

  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    mMap = googleMap;

    mMap.getUiSettings().setZoomControlsEnabled(true);
    mMap.getUiSettings().setZoomGesturesEnabled(true);

    // 4) Add markers for each house
    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
    for (House h : allHouses) {
      LatLng pos = h.location;
      Marker marker =
          mMap.addMarker(
              new MarkerOptions()
                  .position(pos)
                  .title(h.address)
                  .snippet("€" + (int) h.rent + "/mo"));
      if (marker != null) marker.setTag(h.id);
      boundsBuilder.include(pos);
    }

    // 5) Zoom to include all markers
    if (!allHouses.isEmpty()) {
      LatLngBounds bounds = boundsBuilder.build();
      int padding = 100; // offset from edges in pixels
      mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    // 6) Marker click → detail
    mMap.setOnMarkerClickListener(
        marker -> {
          Object tag = marker.getTag();
          if (tag instanceof Long) {
            long houseId = (Long) tag;
            startActivity(
                new Intent(this, HouseDetailActivity.class).putExtra("EXTRA_HOUSE_ID", houseId));
          }
          return true;
        });
  }
}
