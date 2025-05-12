package com.example.roomie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignupStep1Activity extends AppCompatActivity {
    private static final String TAG = "SignupStep1";

    private long userId;
    private DatabaseHelper dbHelper;

    private EditText etFirst, etLast;
    private ImageView[] photoSlots;
    private Button btnContinue;
    private final List<Uri> photoUris = new ArrayList<>();

    private ActivityResultLauncher<Intent> pickLauncher;
    private int currentSlot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step1);

        dbHelper = new DatabaseHelper(this);
        userId   = getIntent().getLongExtra("user_id", -1);

        etFirst     = findViewById(R.id.etFirstName);
        etLast      = findViewById(R.id.etLastName);
        btnContinue = findViewById(R.id.btnContinueStep1);

        photoSlots = new ImageView[] {
                findViewById(R.id.imgPhoto1),
                findViewById(R.id.imgPhoto2),
                findViewById(R.id.imgPhoto3)
        };

        // Setup image picker
        pickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri == null) return;
                            ensureSize(currentSlot + 1);
                            photoUris.set(currentSlot, uri);
                            try {
                                Bitmap bmp = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), uri);
                                photoSlots[currentSlot].setImageBitmap(bmp);
                            } catch (IOException e) {
                                Log.e(TAG, "image load failed", e);
                            }
                            updateContinueState();
                        }
                    }
                }
        );

        // Photo slot clicks
        for (int i = 0; i < photoSlots.length; i++) {
            final int idx = i;
            photoSlots[i].setOnClickListener(v -> {
                currentSlot = idx;
                Intent pick = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickLauncher.launch(pick);
            });
        }

        // Text watchers
        TextWatcher tw = new SimpleTextWatcher(this::updateContinueState);
        etFirst.addTextChangedListener(tw);
        etLast .addTextChangedListener(tw);
        updateContinueState();

        // Continue → save names & photos, jump to interests
        btnContinue.setOnClickListener(v -> {
            String first = etFirst.getText().toString().trim();
            String last  = etLast.getText().toString().trim();

            dbHelper.updateUserName(userId, first, last);
            dbHelper.insertUserPhotos(userId, photoUris);

            Intent intent = new Intent(this, InterestsActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        });
    }

    private void ensureSize(int size) {
        while (photoUris.size() < size) {
            photoUris.add(null);
        }
    }

    private void updateContinueState() {
        boolean namesFilled = !etFirst.getText().toString().isEmpty()
                && !etLast.getText().toString().isEmpty();
        long selectedPhotos = photoUris.stream().filter(u -> u != null).count();
        btnContinue.setEnabled(namesFilled && selectedPhotos >= 0);
    }

    // Simple TextWatcher helper
    private static class SimpleTextWatcher implements TextWatcher {
        private final Runnable onChange;
        SimpleTextWatcher(Runnable onChange) { this.onChange = onChange; }
        @Override public void beforeTextChanged(CharSequence s,int a,int b,int c){}
        @Override public void onTextChanged(CharSequence s,int a,int b,int c){ onChange.run(); }
        @Override public void afterTextChanged(Editable s){}
    }
}
