package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class ListingsActivity extends AppCompatActivity {
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        recycler = findViewById(R.id.recyclerHouses);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // filter mock data
        List<House> all = new SearchHousesActivity().getMockHouses();
        //below are filtered mock data, if you want it to work add "filtered" as first argument on ListingsAdapter instead of "all"
/*
        List<House> filtered = new ArrayList<>();
        for (House h : all) {
            assert addr != null;
            if (!addr.isEmpty() && !h.address.toLowerCase()
                    .contains(addr.toLowerCase())) continue;
            if (minP>=0 && h.rent<minP) continue;
            if (maxP>=0 && h.rent>maxP) continue;
            if (minA>=0 && h.area<minA) continue;
            if (maxA>=0 && h.area>maxA) continue;
            if (floorFilter>=0 && h.floor!=floorFilter) continue;
            filtered.add(h);
        }
*/


        // wire up adapter with a clean callback
        ListingsAdapter adapter = new ListingsAdapter(all, house -> {
            Intent i = new Intent(this, HouseDetailActivity.class);
            i.putExtra("EXTRA_HOUSE_ID", house.id);
            startActivity(i);
        });
        recycler.setAdapter(adapter);

        // bottom nav
        BottomNavigationHelper.setup(
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_home
        );
    }
}
