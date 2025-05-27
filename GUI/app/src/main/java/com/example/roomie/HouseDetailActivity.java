package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class HouseDetailActivity extends AppCompatActivity {
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_house_detail);


   currentUserId = SessionManager.get().getUserId();

    // Bind views
    ViewPager2 viewPager = findViewById(R.id.viewPagerImages);
    TextView tvAddress = findViewById(R.id.tvAddress);
    TextView tvPrice = findViewById(R.id.tvPrice);
    TextView tvArea = findViewById(R.id.tvArea);
    TextView tvFloor = findViewById(R.id.tvFloor);
    ImageView ivOwnerAvatar = findViewById(R.id.ivOwnerAvatar);
    TextView tvOwnerName = findViewById(R.id.tvOwnerName);
    TextView tvOwnerSub = findViewById(R.id.tvOwnerSub);
    Button btnViewProf = findViewById(R.id.btnViewProfile);
    Button btnMatch = findViewById(R.id.btnMatch);
    // Read the house ID from the Intent
    long houseId = getIntent().getLongExtra("EXTRA_HOUSE_ID", -1);
    if (houseId < 0) {
      finish();
      return;
    }

    // Fetch from SQLite
    DatabaseHelper db = new DatabaseHelper(this);

    House house = db.getHouseById(houseId);
    User owner=db.getUserById(house.ownerId);
      List<String> uris = db.getHousePhotos(houseId);
      viewPager.setAdapter(
              new ImageSliderAdapter(this, uris));
    tvAddress.setText(house.address);

    tvPrice.setText("€" + (int) house.rent + "/month");
    tvArea.setText(house.area + " m²");
    tvFloor.setText((house.floor == 0 ? "Ground" : house.floor + "th") + " Floor");
    tvOwnerName.setText(owner.firstName);
    tvOwnerSub.setText(owner.lastName);
    Glide.with(this).load(owner.avatarUrl).circleCrop().into(ivOwnerAvatar);

    //VIEW PROFILE BUTTON
    btnViewProf.setOnClickListener(
        v ->{
          Intent i = new Intent(this, OtherUserProfileActivity.class);
          i.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, house.ownerId);
          startActivity(i);
        }
    );


    //MATCH WITH OWNER
    btnMatch.setOnClickListener(
        v ->{
          db.likeUser(currentUserId, house.ownerId);
          Toast.makeText(this, "Sent match request to "+owner.firstName+" "+owner.lastName, Toast.LENGTH_SHORT).show();
        }
    );

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_home);
  }
}
