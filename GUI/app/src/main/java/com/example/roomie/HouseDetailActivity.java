package com.example.roomie;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
<<<<<<< HEAD
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HouseDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        // Bind views
        ViewPager2 viewPager     = findViewById(R.id.viewPagerImages);
        TextView   tvAddress     = findViewById(R.id.tvAddress);
        TextView   tvPrice       = findViewById(R.id.tvPrice);
        TextView   tvArea        = findViewById(R.id.tvArea);
        TextView   tvFloor       = findViewById(R.id.tvFloor);
        ImageView  ivOwnerAvatar = findViewById(R.id.ivOwnerAvatar);
        TextView   tvOwnerName   = findViewById(R.id.tvOwnerName);
        TextView   tvOwnerSub    = findViewById(R.id.tvOwnerSub);
        Button     btnViewProf   = findViewById(R.id.btnViewProfile);
        Button     btnMatch      = findViewById(R.id.btnMatch);
        // Read the house ID from the Intent
        long houseId = getIntent().getLongExtra("EXTRA_HOUSE_ID", -1);
        if (houseId < 0) {
            finish();
            return;
        }

        // Fetch from SQLite
        DatabaseHelper db = new DatabaseHelper(this);
        House house = db.getHouseById(houseId);
        if (house == null) {
            finish();
            return;
        }

        // Populate UI
        viewPager.setAdapter(new ImageSliderAdapter(house.photoUrls));
        tvAddress   .setText(house.address);
        tvPrice     .setText("€" + (int)house.rent + "/month");
        tvArea      .setText(house.area + " m²");
        tvFloor     .setText((house.floor == 0 ? "Ground" : house.floor + "th") + " Floor");
        tvOwnerName .setText(house.ownerName);
        tvOwnerSub  .setText(db.getUserEmailById(house.ownerId));
        Glide.with(this)
                .load(house.ownerAvatarUrl)
                .circleCrop()
                .into(ivOwnerAvatar);

        btnViewProf.setOnClickListener(v ->
                Toast.makeText(this,
                        "View profile for " + house.ownerName,
                        Toast.LENGTH_SHORT).show()
        );
        btnMatch.setOnClickListener(v ->
                Toast.makeText(this,
                        "Match with " + house.ownerName,
                        Toast.LENGTH_SHORT).show()
        );
        tvOwnerSub.setText(db.getUserEmailById(house.ownerId));

        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }
}
=======
import com.google.android.flexbox.FlexboxLayout;

public class HouseDetailActivity extends AppCompatActivity {
  private ViewPager2 viewPager;
  private TextView tvAddress, tvPrice, tvArea, tvFloor, tvOwnerName;
  private ImageView ivOwnerAvatar;
  private FlexboxLayout flexTags;
  private Button btnViewProfile, btnMatch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_house_detail);

    // bind views
    ViewPager2 pager = findViewById(R.id.viewPagerImages);
    TextView tvAddr = findViewById(R.id.tvAddress);
    TextView tvPr = findViewById(R.id.tvPrice);
    TextView tvAr = findViewById(R.id.tvArea);
    TextView tvFl = findViewById(R.id.tvFloor);
    ImageView ivAv = findViewById(R.id.ivOwnerAvatar);
    TextView tvOwnn = findViewById(R.id.tvOwnerName);
    Button btnVP = findViewById(R.id.btnViewProfile);
    Button btnMatch = findViewById(R.id.btnMatch);

    // bottom nav
    BottomNavigationHelper.setup(findViewById(R.id.bottom_navigation), this, R.id.nav_home);

    long id = getIntent().getLongExtra("EXTRA_HOUSE_ID", -1);
    House h =
        new SearchHousesActivity()
            .getMockHouses().stream().filter(x -> x.id == id).findFirst().orElse(null);
    if (h == null) finish();

    pager.setAdapter(new ImageSliderAdapter(h.photoUrls));
    tvAddr.setText(h.address);
    tvPr.setText("€" + (int) h.rent + "/month");
    tvAr.setText(h.area + " m²");
    tvFl.setText((h.floor == 0 ? "Ground" : h.floor + "th") + " Floor");
    tvOwnn.setText(h.ownerName);
    Glide.with(this).load(h.ownerAvatarUrl).circleCrop().into(ivAv);

    btnVP.setOnClickListener(
        v ->
            Toast.makeText(this, "Would open " + h.ownerName + "’s profile…", Toast.LENGTH_SHORT)
                .show());
    btnMatch.setOnClickListener(
        v -> Toast.makeText(this, "Matching with " + h.ownerName + "…", Toast.LENGTH_SHORT).show());
  }
}
>>>>>>> 2b1c838 (fix conflicts)
