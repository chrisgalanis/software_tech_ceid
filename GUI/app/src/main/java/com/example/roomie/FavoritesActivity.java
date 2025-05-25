package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import java.util.Arrays;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);

    BottomNavigationHelper.setup(findViewById(R.id.bottom_navigation), this, R.id.nav_favorites);

    // Dummy user likes
    List<String> userLikes = Arrays.asList("Titos H.", "Rafas P.", "Mike M.");
    RecyclerView recyclerUsers = findViewById(R.id.recyclerUsers);
    recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    recyclerUsers.setAdapter(new UserLikesAdapter(this, userLikes));

    // Dummy house likes
    List<House> houseLikes =
        Arrays.asList(
            new House(
                1,
                "Athens Center",
                160.0,
                60.0,
                2,
                Arrays.asList("url1"),
                "Chris P.",
                "",
                new LatLng(0, 0)),
            new House(
                2,
                "Patras",
                230.0,
                70.0,
                3,
                Arrays.asList("url2"),
                "Maria G.",
                "",
                new LatLng(0, 0)));
    RecyclerView recyclerHouses = findViewById(R.id.recyclerHouses);
    recyclerHouses.setLayoutManager(new LinearLayoutManager(this));
    recyclerHouses.setAdapter(
        new LikedHousesAdapter(
            this,
            houseLikes,
            house -> {
              Toast.makeText(
                      this,
                      "This action would show the house of " + house.ownerName,
                      Toast.LENGTH_SHORT)
                  .show();
            }));
  }
}
