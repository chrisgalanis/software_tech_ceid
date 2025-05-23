package com.example.roomie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

    private EditText    etBudget, etCity;
    private Button      btnSubmit;
    private CardView    cardContainer;
    private ImageView   imgProfile;
    private TextView    tvNameAge, tvBudgetCity;
    private ImageButton btnReject, btnAccept;

    private DatabaseHelper dbHelper;
    private List<User>     recs;
    private int            currentIndex = 0;
    private long           currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        // initialize your session manager
        SessionManager.init(this);
        currentUserId = SessionManager.get().getUserId();
        if (currentUserId < 0) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // view binding
        etBudget      = findViewById(R.id.etBudget);
        etCity        = findViewById(R.id.etCity);
        btnSubmit     = findViewById(R.id.btnSubmitPrefs);
        cardContainer = findViewById(R.id.cardContainer);
        imgProfile    = findViewById(R.id.imgProfile);
        tvNameAge     = findViewById(R.id.tvNameAge);
        tvBudgetCity  = findViewById(R.id.tvBudgetCity);
        btnReject     = findViewById(R.id.btnReject);
        btnAccept     = findViewById(R.id.btnAccept);

        dbHelper = new DatabaseHelper(this);

        // listeners
        btnSubmit.setOnClickListener(v -> savePreferences());
        btnReject.setOnClickListener(v -> {
            mark(false);
            nextCard();
        });
        btnAccept.setOnClickListener(v -> {
            mark(true);
            nextCard();
        });
    }

    private void savePreferences() {
        String cityText = etCity.getText().toString().trim();
        String budText  = etBudget.getText().toString().trim();
        if (cityText.isEmpty() || budText.isEmpty()) {
            Toast.makeText(this, "Please enter both budget and city", Toast.LENGTH_SHORT).show();
            return;
        }

        int budget;
        try {
            budget = Integer.parseInt(budText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid budget", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.updateUserPreferences(currentUserId, budget, cityText);
        loadRecommendations();
    }

    private void loadRecommendations() {
        recs = dbHelper.getRecommendations(currentUserId);
        if (recs.isEmpty()) {
            Toast.makeText(this, "No users to recommend", Toast.LENGTH_SHORT).show();
            return;
        }
        currentIndex = 0;
        cardContainer.setVisibility(View.VISIBLE);
        showCard(recs.get(currentIndex));
    }

    private void showCard(User u) {
        // if you store photos, load here with Glide/Picasso...
        tvNameAge.setText(u.firstName + " " + u.lastName
                + (u.age > 0 ? ", " + u.age : ""));
        tvBudgetCity.setText("Budget: €" + u.budget
                + " • City: " + u.city);
    }

    private void mark(boolean liked) {
        User u = recs.get(currentIndex);
        dbHelper.markLike(currentUserId, u.id, liked);
    }

    private void nextCard() {
        currentIndex++;
        if (currentIndex < recs.size()) {
            showCard(recs.get(currentIndex));
        } else {
            Toast.makeText(this, "You’ve reached the end!", Toast.LENGTH_SHORT).show();
            cardContainer.setVisibility(View.GONE);
        }
    }
}
