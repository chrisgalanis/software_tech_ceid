package com.example.roomie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoritesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set a simple layout for Favorites; create res/layout/activity_favorites.xml
        setContentView(R.layout.activity_favorites);
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_favorites
        );
    }
}
