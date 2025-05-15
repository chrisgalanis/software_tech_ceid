package com.example.roomie;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import java.util.List;

public class HouseDetailActivity extends AppCompatActivity {
    private ViewPager2    viewPager;
    private TextView      tvAddress, tvPrice, tvArea, tvFloor, tvOwnerName;
    private ImageView     ivOwnerAvatar;
    private FlexboxLayout flexTags;
    private Button        btnViewProfile, btnMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        // bind views
        ViewPager2  pager       = findViewById(R.id.viewPagerImages);
        TextView    tvAddr      = findViewById(R.id.tvAddress);
        TextView    tvPr        = findViewById(R.id.tvPrice);
        TextView    tvAr        = findViewById(R.id.tvArea);
        TextView    tvFl        = findViewById(R.id.tvFloor);
        ImageView   ivAv        = findViewById(R.id.ivOwnerAvatar);
        TextView    tvOwnn      = findViewById(R.id.tvOwnerName);
        Button      btnVP       = findViewById(R.id.btnViewProfile);
        Button      btnMatch    = findViewById(R.id.btnMatch);

        // bottom nav
        BottomNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this, R.id.nav_home);

        long id = getIntent().getLongExtra("EXTRA_HOUSE_ID",-1);
        House h = new SearchHousesActivity().getMockHouses()
                .stream().filter(x->x.id==id).findFirst().orElse(null);
        if (h==null) finish();

        pager.setAdapter(new ImageSliderAdapter(h.photoUrls));
        tvAddr.setText(h.address);
        tvPr  .setText("€"+(int)h.rent+"/month");
        tvAr  .setText(h.area+" m²");
        tvFl  .setText((h.floor==0?"Ground":h.floor+"th")+" Floor");
        tvOwnn.setText(h.ownerName);
        Glide.with(this)
                .load(h.ownerAvatarUrl)
                .circleCrop()
                .into(ivAv);

        btnVP .setOnClickListener(v->
                Toast.makeText(this,
                        "Would open "+h.ownerName+"’s profile…",
                        Toast.LENGTH_SHORT).show()
        );
        btnMatch.setOnClickListener(v->
                Toast.makeText(this,
                        "Matching with "+h.ownerName+"…",
                        Toast.LENGTH_SHORT).show()
        );
    }

    private void addTag(String text) {
        // Inflate a Material Chip programmatically
        Chip chip = new Chip(this);
        chip.setText(text);

        // Optionally apply the default outlined style:
        ChipDrawable drawable = ChipDrawable.createFromAttributes(
                this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice);
        chip.setChipDrawable(drawable);

        // You can tweak appearance if needed:
        // chip.setChipBackgroundColorResource(R.color.some_light_gray);
        // chip.setTextColor(getResources().getColor(R.color.some_dark_text));

        flexTags.addView(chip);
    }

}
