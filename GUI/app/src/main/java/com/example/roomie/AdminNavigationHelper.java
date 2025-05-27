package com.example.roomie;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminNavigationHelper {
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
                intent = new Intent(activity, AuthenticateListingsActivity.class);
            }
            else if (id == R.id.nav_reports) {
                intent = new Intent(activity, AdminReportsActivity.class);
            }
            else if (id == R.id.nav_messages) {
                intent = new Intent(activity, AdminReportsActivity.class);
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
