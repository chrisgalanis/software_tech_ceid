package com.example.roomie;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchRoommatesActivity extends AppCompatActivity {
    private EditText etCitySearch, etBudgetFrom, etBudgetTo;
    private Button   btnApply;
    private long     currentUserId;
    private DatabaseHelper dbHelper;    // ← add this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_roommates);

        // bottom nav setup (unchanged)
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_search
        );

        // login check (unchanged)
        SessionManager.init(this);
        currentUserId = SessionManager.get().getUserId();
        if (currentUserId < 0) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // wire up views
        etCitySearch = findViewById(R.id.etCitySearch);
        etBudgetFrom = findViewById(R.id.etBudgetFrom);
        etBudgetTo   = findViewById(R.id.etBudgetTo);
        btnApply     = findViewById(R.id.btnApplyFilters);

        // instantiate your DB helper
        dbHelper = new DatabaseHelper(this);

        btnApply.setOnClickListener(v -> {
            String cityText = etCitySearch.getText().toString().trim();
            String fromText = etBudgetFrom.getText().toString().trim();
            String toText   = etBudgetTo.getText().toString().trim();

            if (cityText.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
                Toast.makeText(this, "Please enter city, min and max budget", Toast.LENGTH_SHORT).show();
                return;
            }

            int budgetFrom, budgetTo;
            try {
                budgetFrom = Integer.parseInt(fromText);
                budgetTo   = Integer.parseInt(toText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid budget values", Toast.LENGTH_SHORT).show();
                return;
            }

            if (budgetFrom > budgetTo) {
                Toast.makeText(this, "Min budget must be ≤ max budget", Toast.LENGTH_SHORT).show();
                return;
            }

            // ← Persist the filters into your SQLite users table
            dbHelper.updateUserPreferences(currentUserId, budgetFrom, budgetTo, cityText);

            // now go show the recommendations
            Intent intent = new Intent(this, RecommendationActivity.class);
            startActivity(intent);
        });
    }
}
