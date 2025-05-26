package com.example.roomie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class CreateListingActivity extends AppCompatActivity implements OnMapReadyCallback {

  private EditText etAddress, etRent, etArea, etFloor;
  private Button btnPickImages, btnSubmit;
  private GoogleMap map;
  private LatLng chosenLocation;
  private List<Uri> selectedUris = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_listing);

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_home);

    etAddress = findViewById(R.id.etAddress);
    etRent = findViewById(R.id.etRent);
    etArea = findViewById(R.id.etArea);
    etFloor = findViewById(R.id.etFloor);
    btnPickImages = findViewById(R.id.btnPickImages);
    btnSubmit = findViewById(R.id.btnSubmit);

    // Map setup
    SupportMapFragment mapFrag =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
    mapFrag.getMapAsync(this);

    btnPickImages.setOnClickListener(
        v -> {
          Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
          i.setType("image/*");
          i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
          startActivityForResult(i, 1001);
        });

    btnSubmit.setOnClickListener(
        v -> {
          String address = etAddress.getText().toString().trim();
          String rentStr = etRent.getText().toString().trim();
          String areaStr = etArea.getText().toString().trim();
          String floorStr = etFloor.getText().toString().trim();

          if (address.isEmpty()
              || rentStr.isEmpty()
              || areaStr.isEmpty()
              || floorStr.isEmpty()
              || chosenLocation == null) {
            Toast.makeText(this, "Please fill all fields and pick a location", Toast.LENGTH_SHORT)
                .show();
            return;
          }

          double rent = Double.parseDouble(rentStr);
          double area = Double.parseDouble(areaStr);
          int floor = Integer.parseInt(floorStr);

          long ownerId = SessionManager.get().getUserId();
          DatabaseHelper db = new DatabaseHelper(this);
          long hid =
              db.insertHouse(
                  ownerId,
                  address,
                  rent,
                  area,
                  floor,
                  chosenLocation.latitude,
                  chosenLocation.longitude);
          db.insertHousePhotos(hid, selectedUris);

          Toast.makeText(this, "Listing created!", Toast.LENGTH_SHORT).show();
          finish();
        });
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;

    map.getUiSettings().setZoomControlsEnabled(true);
    map.getUiSettings().setZoomGesturesEnabled(true);

    map.setOnMapClickListener(
        latLng -> {
          map.clear();
          map.addMarker(new MarkerOptions().position(latLng));
          chosenLocation = latLng;
        });
  }

  @Override
  protected void onActivityResult(int req, int res, @Nullable Intent data) {
    super.onActivityResult(req, res, data);
    if (req == 1001 && res == Activity.RESULT_OK && data != null) {
      selectedUris.clear();
      if (data.getData() != null) {
        selectedUris.add(data.getData());
      } else if (data.getClipData() != null) {
        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
          selectedUris.add(data.getClipData().getItemAt(i).getUri());
        }
      }
    }
  }
}
