package com.example.roomie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateListingActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final int REQ_PICK_PHOTO = 1001;

  private EditText etAddress, etRent, etArea, etFloor;
  private Button btnPickImages, btnSubmit;
  private GoogleMap map;
  private LatLng chosenLocation;
  private List<Uri> selectedUris = new ArrayList<>();

  private DatabaseHelper db;
  private long ownerId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_listing);

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_home);

    db = new DatabaseHelper(this);
    ownerId = SessionManager.get().getUserId();

    etAddress = findViewById(R.id.etAddress);
    etRent = findViewById(R.id.etRent);
    etArea = findViewById(R.id.etArea);
    etFloor = findViewById(R.id.etFloor);
    btnPickImages = findViewById(R.id.btnPickImages);
    btnSubmit = findViewById(R.id.btnSubmit);

    // disable submit until we have at least 2 photos
    btnSubmit.setEnabled(false);

    SupportMapFragment mapFrag =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
    mapFrag.getMapAsync(this);

    btnPickImages.setOnClickListener(
        v -> {
          Intent pick =
              new Intent(
                  Intent.ACTION_PICK,
                  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          pick.setType("image/*");
          startActivityForResult(pick, REQ_PICK_PHOTO);
        });

    btnSubmit.setOnClickListener(v -> submitListing());
  }

  private void submitListing() {
    String address = etAddress.getText().toString().trim();
    String rentStr = etRent.getText().toString().trim();
    String areaStr = etArea.getText().toString().trim();
    String floorStr = etFloor.getText().toString().trim();

    if (address.isEmpty()
        || rentStr.isEmpty()
        || areaStr.isEmpty()
        || floorStr.isEmpty()
        || chosenLocation == null
        || selectedUris.size() < 2) {
      Toast.makeText(
              this,
              "Please fill all fields, pick a location and at least 2 photos",
              Toast.LENGTH_SHORT)
          .show();
      return;
    }

    double rent = Double.parseDouble(rentStr);
    double area = Double.parseDouble(areaStr);
    int floor = Integer.parseInt(floorStr);

    List<String> photoUrls = new ArrayList<>();
    for (Uri uri : selectedUris) {
      photoUrls.add(uri.toString());
    }

    House house =
        new House(
            0,
            ownerId,
            address,
            rent,
            area,
            floor,
            chosenLocation.latitude,
            chosenLocation.longitude,
            photoUrls);

    User u = db.getUserById(ownerId);
    String ownerName = u.firstName + " " + u.lastName;
    String ownerAvatarUrl = u.avatarUrl;
    String houseAvatarUrl = photoUrls.get(0);

    HouseListing listing = new HouseListing(house, ownerName, ownerAvatarUrl, houseAvatarUrl, true);

    db.insertHouseListing(listing);
    Toast.makeText(this, "Listing created!", Toast.LENGTH_SHORT).show();
    finish();
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
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_PICK_PHOTO
        && resultCode == Activity.RESULT_OK
        && data != null
        && data.getData() != null) {

      Uri srcUri = data.getData();
      processAndStoreUri(srcUri);
      btnSubmit.setEnabled(selectedUris.size() >= 1);
    }
  }

  private void processAndStoreUri(Uri srcUri) {
    try (InputStream in = getContentResolver().openInputStream(srcUri)) {
      File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "roomie");
      if (!dir.exists()) dir.mkdirs();

      String fname = "photo_" + System.currentTimeMillis() + ".jpg";
      File outFile = new File(dir, fname);

      try (OutputStream out = new FileOutputStream(outFile)) {
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
      }

      Uri storedUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", outFile);

      selectedUris.add(storedUri);

    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Failed to store photo", Toast.LENGTH_SHORT).show();
    }
  }
}
