package com.example.roomie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class AddPhotosActivity extends AppCompatActivity {

    private ImageView[]   slots        = new ImageView[6];
    private ImageView[]   placeholders = new ImageView[6];
    private ImageButton[] removes      = new ImageButton[6];
    private int           currentSlot  = -1;
    private int           photosCount  = 0;
    private Button        btnContinue;

    private long           userId;
    private DatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> pickPhotoLauncher;

    private void handleImageSelection(Uri uri) {
        if (uri != null && currentSlot >= 0) {
            // show it
            slots[currentSlot].setImageURI(uri);
            slots[currentSlot].setTag(uri);

            // swap icons
            placeholders[currentSlot].setVisibility(View.GONE);
            removes[currentSlot].setVisibility(View.VISIBLE);

            photosCount++;
            currentSlot = -1;
            btnContinue.setEnabled(photosCount >= 2);
        }
    }

    private void launchImagePicker() {
        pickPhotoLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        userId = SessionManager.get().getUserId();
        if (userId < 0) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2) init DB helper
        dbHelper = new DatabaseHelper(this);

        // 3) wire up views
        btnContinue = findViewById(R.id.btnContinue2);
        for (int i = 0; i < 6; i++) {
            int idx = i + 1;
            slots[i]        = findViewById(getResId("img" + idx));
            placeholders[i] = findViewById(getResId("ivPlaceholder" + idx));
            removes[i]      = findViewById(getResId("btnRemove" + idx));
        }

        // 4) Continue button: save into DB, then go next
        btnContinue.setOnClickListener(v -> {
            // collect URIs
            List<Uri> uris = new ArrayList<>();
            for (ImageView slot : slots) {
                Object tag = slot.getTag();
                if (tag instanceof Uri) {
                    uris.add((Uri) tag);
                }
            }
            // save into SQLite
            dbHelper.insertUserPhotos(userId, uris);

            // launch next screen (e.g. InterestsActivity)
            Intent intent = new Intent(this, InterestsActivity.class);
            startActivity(intent);
            finish();
        });

        pickPhotoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleImageSelection(result.getData().getData());
                    }
                });
    }

    /** helper to convert "img1" → R.id.img1 */
    private int getResId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }

    /** Called when user taps a slot or its “+” */
    public void onPickPhoto(View v) {
        for (int i = 0; i < 6; i++) {
            if (v.getId() == slots[i].getId() ||
                    v.getId() == placeholders[i].getId()) {
                currentSlot = i;
                break;
            }
        }
        if (currentSlot < 0) return;
        launchImagePicker();
    }

    /** Called when user taps the “×” */
    public void onRemovePhoto(View v) {
        for (int i = 0; i < 6; i++) {
            if (v.getId() == removes[i].getId()) {
                // clear
                slots[i].setImageDrawable(null);
                slots[i].setTag(null);

                placeholders[i].setVisibility(View.VISIBLE);
                removes[i].setVisibility(View.GONE);

                photosCount--;
                break;
            }
        }
        btnContinue.setEnabled(photosCount >= 2);
    }
}
