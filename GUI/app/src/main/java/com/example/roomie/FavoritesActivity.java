package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
  private DatabaseHelper db;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);

    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }
    db = new DatabaseHelper(this);

    BottomNavigationHelper.setup(
            findViewById(R.id.bottom_navigation),
            this,
            R.id.nav_favorites
    );

    setupUserLikes();
    setupHouseLikes();
  }

  private void setupUserLikes() {
    List<Long> likerIds = db.getUsersWhoLikedMe(currentUserId);
    List<User> likers = new java.util.ArrayList<>();
    for (long id : likerIds) {
      User u = db.getUserById(id);
      if (u != null) likers.add(u);
    }

    RecyclerView recyclerUsers = findViewById(R.id.recyclerUsers);
    recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    recyclerUsers.setAdapter(
            new UserLikesAdapter(this, likers, db, currentUserId)
    );
  }

  private void setupHouseLikes() {
    // Load only houses this user has liked
    List<HouseListing> likedListings =
            db.getAllHouseListings();

    RecyclerView recyclerHouses = findViewById(R.id.recyclerHouses);
    recyclerHouses.setLayoutManager(new LinearLayoutManager(this));
    recyclerHouses.setAdapter(
            new AuthenticateHouseAdapter(
                    this,
                    db,
                    likedListings,
                    listing -> {
                      // View detail only, no approve/disapprove
                      startActivity(
                              new Intent(this, HouseDetailActivity.class)
                                      .putExtra("EXTRA_HOUSE_ID", listing.house.id)
                      );
                    },
                    null,
                    null
            )
    );
  }
}
