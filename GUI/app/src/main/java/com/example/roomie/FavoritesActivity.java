package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
  private DatabaseHelper db;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);

    // get your logged-in user’s ID (pass it via Intent or session)
    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }
    db = new DatabaseHelper(this);

    BottomNavigationHelper.setup(findViewById(R.id.bottom_navigation), this, R.id.nav_favorites);

    setupUserLikes();
    setupHouseLikes(); // see note below
  }

  private void setupUserLikes() {
    // 1) Fetch IDs of users who liked me
    List<Long> likerIds = db.getUsersWhoLikedMe(currentUserId);

    // 2) Load their User objects
    List<User> likers = new ArrayList<>();
    for (long id : likerIds) {
      User u = db.getUserById(id);
      if (u != null) {
        likers.add(u);
      }
    }

    // 3) Bind to the same adapter—now showing “People who liked you”
    RecyclerView recyclerUsers = findViewById(R.id.recyclerUsers);
    recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    recyclerUsers.setAdapter(new UserLikesAdapter(this, likers, db, currentUserId));
  }

  private void setupHouseLikes() {
    // If you store house-likes in your DB, you’d do something similar here:
    //   - add a 'houses' table (and/or a house_likes join table)
    //   - DBHelper.getLikedHouses(currentUserId) → List<House>
    //
    // For now you can just fall back to your dummy list:
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
            house ->
                Toast.makeText(
                        this,
                        "This action would show the house of " + house.ownerName,
                        Toast.LENGTH_SHORT)
                    .show()));
  }
}
