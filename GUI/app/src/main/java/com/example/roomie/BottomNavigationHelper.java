package com.example.roomie;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {
    /**
     * @param navView           the BottomNavigationView
     * @param activity          the current Activity
     * @param selectedItemId    the R.id.* of the tab to highlight
     */
    public static void setup(BottomNavigationView navView,
                             final Activity activity,
                             int selectedItemId) {
        // highlight up front
        navView.setSelectedItemId(selectedItemId);

        // listener for taps
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) {
                // tapped the current tab → no-op
                return true;
            }

            Intent intent;
            if (id == R.id.nav_home) {
                intent = new Intent(activity, HomeFilterActivity.class);
            }
            else if (id == R.id.nav_search) {
                intent = new Intent(activity, SearchRoommatesActivity.class);
            }
            else if (id == R.id.nav_favorites) {
                intent = new Intent(activity, FavoritesActivity.class);
            }
            else if (id == R.id.nav_messages) {
                intent = new Intent(activity, MessagesActivity.class);
            }
            else if (id == R.id.nav_feed) {
                intent = new Intent(activity, ProfileActivity.class);
            }
            else {
                return false;
            }

            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
            return true;
        });
    }
}
