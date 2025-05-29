package com.example.roomie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
  private Button btnViewProfile;

  private DatabaseHelper dbHelper;
  private List<User> recs;
  private int currentIndex = 0;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recommendation);

    // bottom nav setup
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_search);

    // session & user
    SessionManager.init(this);
    currentUserId = SessionManager.get().getUserId();

    // view bindings
    cardContainer = findViewById(R.id.cardContainer);
    imgProfile = findViewById(R.id.imgProfile);
    tvNameAge = findViewById(R.id.tvNameAge);
    tvBudgetCity = findViewById(R.id.tvBudgetCity);
    btnReject = findViewById(R.id.btnReject);
    btnAccept = findViewById(R.id.btnAccept);
    btnViewProfile = findViewById(R.id.btnViewProfile);

    dbHelper = new DatabaseHelper(this);

    // load data
    loadRecommendations();

    // listeners
    btnReject.setOnClickListener(
        v -> {
          User u = recs.get(currentIndex);
          dbHelper.unlikeUser(currentUserId, u.id);
          nextCard();
        });
    btnAccept.setOnClickListener(
        v -> {
          User u = recs.get(currentIndex);
          dbHelper.likeUser(currentUserId, u.id);
          nextCard();
        });
    btnViewProfile.setOnClickListener(
        v -> {
          if (recs != null && !recs.isEmpty() && currentIndex < recs.size()) {
            User u = recs.get(currentIndex);
            Intent i = new Intent(this, OtherUserProfileActivity.class);
            i.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, u.id);
            startActivity(i);
          }
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
    showCard(recs.get(currentIndex));
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
    } else {
      imgProfile.setImageResource(R.drawable.ic_profile_placeholder);
    }

    int age = User.calculateAge(u.birthday);
    String ageStr = age > 0 ? ", " + age : "";
    tvNameAge.setText(u.firstName + " " + u.lastName + ageStr);

    tvBudgetCity.setText("Budget: €" + u.minBudget + " - " + u.maxBudget + " • City: " + u.city);
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
