package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class HouseDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private House house;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        // Toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Bind views
        ViewPager2 viewPager = findViewById(R.id.viewPagerImages);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvOwnerName = findViewById(R.id.tvOwnerName);
        TextView tvOwnerSub = findViewById(R.id.tvOwnerSub);
        ImageView ivOwnerAvatar = findViewById(R.id.ivOwnerAvatar);
        Button btnViewProf = findViewById(R.id.btnViewProfile);
        Button btnMatch = findViewById(R.id.btnMatch);


        // Read the house ID from the Intent
        long houseId = getIntent().getLongExtra("EXTRA_HOUSE_ID", -1);
        if (houseId < 0) {
            finish();
            return;
        }

        // Fetch from DB
        DatabaseHelper db = new DatabaseHelper(this);
        house = db.getHouseById(houseId);
        User owner = db.getUserById(house.ownerId);
        List<String> uris = db.getHousePhotos(houseId);

        // Populate UI
        viewPager.setAdapter(new ImageSliderAdapter(this, uris));
        tvAddress.setText(house.address +" , "+(house.floor == 0 ? "Ground" : house.floor + "th") + " Floor");
        tvPrice.setText("€" + (int) house.rent + "/month"+" , "+house.area + " m²");

        tvOwnerName.setText(owner.firstName);
        tvOwnerSub.setText(owner.lastName);

        // Load owner avatar with Glide and circleCrop
        Glide.with(this)
                .load(owner.avatarUrl)
                .circleCrop()
                .into(ivOwnerAvatar);

        // "View Profile" button
        btnViewProf.setOnClickListener(v -> {
            Intent intent = new Intent(HouseDetailActivity.this, OtherUserProfileActivity.class);
            intent.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, house.ownerId);
            startActivity(intent);
        });

        // "Match with Owner" button
        btnMatch.setOnClickListener(v -> {
           currentUserId = SessionManager.get().getUserId();
            db.likeUser(currentUserId, house.ownerId);
            Toast.makeText(this, "Sent match request to " + owner.firstName + " " + owner.lastName, Toast.LENGTH_SHORT).show();
        });

        // Initialize map
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add marker for this house
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(house.getLocation())
                .title(house.address)
                .snippet("€" + (int) house.rent + "/mo"));
        if (marker != null) marker.setTag(house.id);
        bounds.include(house.getLocation());

        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 300));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
