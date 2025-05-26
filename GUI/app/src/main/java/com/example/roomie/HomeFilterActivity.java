package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFilterActivity extends AppCompatActivity {
  private EditText etAddress, etPriceFrom, etPriceTo, etAreaFrom, etAreaTo;
  private Spinner spinnerFloor;
  private Button btnSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_filter);

    etAddress = findViewById(R.id.etAddress);
    etPriceFrom = findViewById(R.id.etPriceFrom);
    etPriceTo = findViewById(R.id.etPriceTo);
    etAreaFrom = findViewById(R.id.etAreaFrom);
    etAreaTo = findViewById(R.id.etAreaTo);
    spinnerFloor = findViewById(R.id.spinnerFloor);
    btnSearch = findViewById(R.id.btnSearch);

    btnSearch.setOnClickListener(
        v -> {
          Intent i = new Intent(this, ListingsActivity.class);
          i.putExtra("addr", etAddress.getText().toString().trim());
          i.putExtra("priceFrom", parseDouble(etPriceFrom));
          i.putExtra("priceTo", parseDouble(etPriceTo));
          i.putExtra("areaFrom", parseDouble(etAreaFrom));
          i.putExtra("areaTo", parseDouble(etAreaTo));
          i.putExtra("floor", spinnerFloor.getSelectedItemPosition());
          startActivity(i);
        });
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_home);
  }

  private double parseDouble(EditText e) {
    try {
      return Double.parseDouble(e.getText().toString());
    } catch (Exception ex) {
      return -1;
    }
  }
}
