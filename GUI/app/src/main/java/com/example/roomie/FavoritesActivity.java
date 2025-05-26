package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        BottomNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_favorites
        );

        setupUserLikes();
        setupHouseLikes();  // see note below
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
        recyclerUsers.setAdapter(
                new UserLikesAdapter(this, likers, db, currentUserId)
        );
    }


    private void setupHouseLikes() {
        // Dummy “liked” houses
        List<House> houseLikes = Arrays.asList(
                new House(
                        1L,              // house ID
                        10L,             // owner ID
                        "Athens Center, Kolonaki",  // address
                        160.0,           // rent
                        60.0,            // area (m²)
                        2,               // floor
                        Arrays.asList(
                                "https://example.com/photos/athens1.jpg",
                                "https://example.com/photos/athens2.jpg"
                        ),               // photo URLs
                        "Chris P.",      // owner name
                        "https://example.com/avatars/chrisp.jpg", // owner avatar URL
                        new LatLng(37.9755, 23.7409) // location
                ),
                new House(
                        2L,
                        11L,
                        "5 Agiou Nikolaou St, Patras",
                        230.0,
                        70.0,
                        3,
                        Arrays.asList(
                                "https://example.com/photos/patras1.jpg"
                        ),
                        "Maria G.",
                        "https://example.com/avatars/mariag.jpg",
                        new LatLng(38.2466, 21.7345)
                )
        );

        RecyclerView recyclerHouses = findViewById(R.id.recyclerHouses);
        recyclerHouses.setLayoutManager(new LinearLayoutManager(this));
        recyclerHouses.setAdapter(new LikedHousesAdapter(this, houseLikes, house ->
                Toast.makeText(this,
                        "This action would show the house of " + house.ownerName,
                        Toast.LENGTH_SHORT).show()
        ));
    }

}

