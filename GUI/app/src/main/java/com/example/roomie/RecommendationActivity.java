package com.example.roomie;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

  private CardView cardContainer;
  private ImageView imgProfile;
  private TextView tvNameAge, tvBudgetCity;
  private ImageButton btnReject, btnAccept;

  private DatabaseHelper dbHelper;
  private List<User> recs;
  private int currentIndex = 0;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommendation);
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_search);
    SessionManager.init(this);
    currentUserId = SessionManager.get().getUserId();

    cardContainer = findViewById(R.id.cardContainer);
    imgProfile = findViewById(R.id.imgProfile);
    tvNameAge = findViewById(R.id.tvNameAge);
    tvBudgetCity = findViewById(R.id.tvBudgetCity);
    btnReject = findViewById(R.id.btnReject);
    btnAccept = findViewById(R.id.btnAccept);

    dbHelper = new DatabaseHelper(this);

    // ← LOAD YOUR DATA HERE
    loadRecommendations();

    btnReject.setOnClickListener(
        v -> {
          mark(false);
          nextCard();
        });
    btnAccept.setOnClickListener(
        v -> {
          mark(true);
          nextCard();
        });
  }

  private void loadRecommendations() {
    recs = dbHelper.getRecommendations(currentUserId);
    if (recs.isEmpty()) {
      Toast.makeText(this, "No users to recommend", Toast.LENGTH_SHORT).show();
      return;
    }
    currentIndex = 0;
    cardContainer.setVisibility(View.VISIBLE);
    showCard(recs.get(0));
  }

  private void showCard(User u) {
    List<String> photos = dbHelper.getUserPhotos(u.id);
    if (!photos.isEmpty()) {
      Uri uri = Uri.parse(photos.get(0));
      Glide.with(this)
          .load(uri)
          .placeholder(R.drawable.ic_profile_placeholder)
          .error(R.drawable.ic_profile_placeholder)
          .into(imgProfile);
    }
    tvNameAge.setText(u.firstName + " " + u.lastName + (u.age > 0 ? ", " + u.age : ""));
    tvBudgetCity.setText("Budget: €" + u.minBudget + "-" + u.maxBudget + " • City: " + u.city);
  }

  private void mark(boolean liked) {
    User u = recs.get(currentIndex);
    dbHelper.likeUser(currentUserId, u.id);
  }

  private void nextCard() {
    currentIndex++;
    if (currentIndex < recs.size()) {
      showCard(recs.get(currentIndex));
    } else {
      Toast.makeText(this, "End of list", Toast.LENGTH_SHORT).show();
      cardContainer.setVisibility(View.GONE);
    }
  }
}
