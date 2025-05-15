package com.example.roomie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchRoommatesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set a simple layout for Search; create res/layout/activity_search_roommates.xml
        setContentView(R.layout.activity_search_roommates);
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_search
        );
    }
}
