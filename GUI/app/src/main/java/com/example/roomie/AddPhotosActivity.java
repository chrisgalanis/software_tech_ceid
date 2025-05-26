package com.example.roomie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.*;
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
    private static final int REQ_PICK_PHOTO = 1001;


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

        dbHelper = new DatabaseHelper(this);

        btnContinue = findViewById(R.id.btnContinue2);
        for (int i = 0; i < 6; i++) {
            int idx = i + 1;
            slots[i]        = findViewById(getResId("img" + idx));
            placeholders[i] = findViewById(getResId("ivPlaceholder" + idx));
            removes[i]      = findViewById(getResId("btnRemove" + idx));
        }

        btnContinue.setOnClickListener(v -> {
            List<Uri> uris = new ArrayList<>();
            for (ImageView slot : slots) {
                Object tag = slot.getTag();
                if (tag instanceof Uri) {
                    uris.add((Uri) tag);
                }
            }
            dbHelper.insertUserPhotos(userId, uris);
            startActivity(new Intent(this, InterestsActivity.class));
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

    /** Called when user taps a slot or its “+” */
    public void onPickPhoto(View v) {
        currentSlot = -1;
        for (int i = 0; i < 6; i++) {
            if (v.getId() == slots[i].getId()
                    || v.getId() == placeholders[i].getId()) {
                currentSlot = i;
                break;
            }
        }
        if (currentSlot < 0) return;
        launchImagePicker();

        // open gallery
        Intent pick = new Intent(Intent.ACTION_PICK);
        pick.setType("image/*");
        startActivityForResult(pick, REQ_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_PHOTO
                && resultCode  == Activity.RESULT_OK
                && data        != null) {

            Uri srcUri = data.getData();
            if (srcUri != null && currentSlot >= 0) {
                // 1) copy into app's external-files/Pictures/roomie
                try (InputStream in = getContentResolver().openInputStream(srcUri)) {
                    File dir = new File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "roomie"
                    );
                    if (!dir.exists()) dir.mkdirs();

                    String fname = "photo_" + System.currentTimeMillis() + ".jpg";
                    File outFile = new File(dir, fname);

                    try (OutputStream out = new FileOutputStream(outFile)) {
                        byte[] buf = new byte[4096];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }

                    // 2) get a content:// URI backed by FileProvider
                    Uri storedUri = FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".fileprovider",
                            outFile
                    );

                    // display & tag it
                    slots[currentSlot].setImageURI(storedUri);
                    slots[currentSlot].setTag(storedUri);
                    placeholders[currentSlot].setVisibility(View.GONE);
                    removes[currentSlot].setVisibility(View.VISIBLE);
                    photosCount++;
                    btnContinue.setEnabled(photosCount >= 2);
                    currentSlot = -1;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to store photo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /** Called when user taps the “×” */
    public void onRemovePhoto(View v) {
        for (int i = 0; i < 6; i++) {
            if (v.getId() == removes[i].getId()) {
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

    /** helper to convert "img1" → R.id.img1 */
    private int getResId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }
}
