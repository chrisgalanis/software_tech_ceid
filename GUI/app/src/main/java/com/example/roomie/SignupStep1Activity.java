package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SignupStep1Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    private ArrayList<Uri> photoUris = new ArrayList<>();
    private ImageView[] photoSlots;
    private Button btnContinue;
    private EditText etFirst, etLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_step1);

        etFirst = findViewById(R.id.etFirstName);
        etLast  = findViewById(R.id.etLastName);
        btnContinue = findViewById(R.id.btnContinueStep1);

        photoSlots = new ImageView[]{
                findViewById(R.id.imgPhoto1),
                findViewById(R.id.imgPhoto2),
                findViewById(R.id.imgPhoto3)
        };

        // Set click listeners on each photo slot
        for (int i = 0; i < photoSlots.length; i++) {
            final int index = i;
            photoSlots[i].setOnClickListener(v -> openImagePicker(index));
        }

        // Enable the Continue button only when name fields are non-empty and at least 2 photos
        etFirst.addTextChangedListener(new SimpleTextWatcher(this::updateContinueState));
        etLast.addTextChangedListener(new SimpleTextWatcher(this::updateContinueState));
        updateContinueState();

        btnContinue.setOnClickListener(v -> {
            // collect data and go to next screen
            Intent intent = new Intent(this, com.example.app.InterestsActivity.class);
            intent.putExtra("first_name", etFirst.getText().toString());
            intent.putExtra("last_name", etLast.getText().toString());
            intent.putParcelableArrayListExtra("photos", photoUris);
            startActivity(intent);
        });
    }

    private void openImagePicker(int slotIndex) {
        Intent chooser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooser, PICK_IMAGE_REQUEST + slotIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int slotIndex = requestCode - PICK_IMAGE_REQUEST;
        if (resultCode == RESULT_OK && data != null && slotIndex >= 0 && slotIndex < photoSlots.length) {
            Uri uri = data.getData();
            photoUris.add(uri);
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                photoSlots[slotIndex].setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateContinueState();
        }
    }

    private void updateContinueState() {
        boolean namesFilled = etFirst.getText().length() > 0 && etLast.getText().length() > 0;
        boolean enoughPhotos = photoUris.size() >= 2;
        btnContinue.setEnabled(namesFilled && enoughPhotos);
    }
}
