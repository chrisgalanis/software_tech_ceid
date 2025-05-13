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

        // Setup DB & userId
        dbHelper = new DatabaseHelper(this);
        userId   = getIntent().getLongExtra("user_id", -1);

        // View bindings
        etFirst     = findViewById(R.id.etFirstName);
        etLast      = findViewById(R.id.etLastName);
        btnContinue = findViewById(R.id.btnContinueStep1);

        photoSlots = new ImageView[] {
                findViewById(R.id.imgPhoto1),
                findViewById(R.id.imgPhoto2),
                findViewById(R.id.imgPhoto3)
        };

        // 1) Setup gallery picker (multi‐select)
        pickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        // Multiple images
                        if (data.getClipData() != null) {
                            int count = Math.min(data.getClipData().getItemCount(), photoSlots.length);
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                setPhotoInSlot(i, uri);
                            }
                        }
                        // Single image
                        else if (data.getData() != null) {
                            setPhotoInSlot(currentSlot, data.getData());
                        }
                        updateContinueState();
                    }
                }
        );

        // 2) Launch picker on slot tap
        for (int i = 0; i < photoSlots.length; i++) {
            final int idx = i;
            photoSlots[i].setOnClickListener(v -> {
                currentSlot = idx;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                pickLauncher.launch(Intent.createChooser(intent, "Select Photos"));
            });
        }

        // 3) Watch name fields
        TextWatcher tw = new SimpleTextWatcher(this::updateContinueState);
        etFirst.addTextChangedListener(tw);
        etLast .addTextChangedListener(tw);
        updateContinueState();

        // 4) Continue → save to DB and go to Interests
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

    /** Ensures photoUris.size() ≥ size */
    private void ensureSize(int size) {
        while (photoUris.size() < size) {
            photoUris.add(null);
        }
    }

    /** Sets the image in slotIndex and stores its URI */
    private void setPhotoInSlot(int slotIndex, Uri uri) {
        ensureSize(slotIndex + 1);
        photoUris.set(slotIndex, uri);
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            photoSlots[slotIndex].setImageBitmap(bmp);
        } catch (IOException e) {
            Log.e(TAG, "Failed to load image", e);
        }
    }

    /** Enables Continue only when names are non‐empty and ≥2 photos chosen */
    private void updateContinueState() {
        boolean namesFilled = !etFirst.getText().toString().isEmpty()
                && !etLast.getText().toString().isEmpty();
        long count = 0;
        for (Uri u : photoUris) if (u != null) count++;
        btnContinue.setEnabled(namesFilled && count >= 2);
    }

    /** Simple TextWatcher so we can pass a lambda */
    private static class SimpleTextWatcher implements TextWatcher {
        private final Runnable onChange;
        SimpleTextWatcher(Runnable onChange) { this.onChange = onChange; }
        @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
        @Override public void onTextChanged(CharSequence s,int st,int b,int c){ onChange.run(); }
        @Override public void afterTextChanged(Editable s){}
    }
}
