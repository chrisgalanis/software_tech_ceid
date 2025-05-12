package com.example.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {
    private ChipGroup chipGroup;
    private Button btnContinue;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        chipGroup    = findViewById(R.id.chipGroup);
        btnContinue  = findViewById(R.id.btnContinueStep2);
        tvSkip       = findViewById(R.id.tvSkip);

        // Continue → collect selected interests
        btnContinue.setOnClickListener(v -> {
            List<String> selected = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selected.add(chip.getText().toString());
                }
            }
            // TODO: send `selected` back to server or next screen
            Toast.makeText(this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
        });

        // Skip → just finish or move on
        tvSkip.setOnClickListener(v -> {
            // Treat as empty selection
            Toast.makeText(this, "No interests selected", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Back arrow
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
