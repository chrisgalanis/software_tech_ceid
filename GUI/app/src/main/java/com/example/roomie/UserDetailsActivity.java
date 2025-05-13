package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {
    private EditText    etFirst, etLast, etBirthday;
    private RadioGroup  rgGender;
    private Button      btnContinue;

    private DatabaseHelper dbHelper;
    private long           userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // 1) wire up views
        etFirst    = findViewById(R.id.etFirstName);
        etLast     = findViewById(R.id.etLastName);
        etBirthday = findViewById(R.id.etBirthday);
        rgGender   = findViewById(R.id.rgGender);
        btnContinue= findViewById(R.id.btnContinue1);

        // 2) init your DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // 3) figure out which user we're editing:
        //    Ideally you passed the DB row ID in the Intent:
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId < 0) {
            // fallback: maybe you passed their email instead?
            String email = getIntent().getStringExtra("EMAIL");
            userId = dbHelper.getUserIdByEmail(email);
        }

        // 4) only enable Continue when all fields non-empty
        TextWatcher watcher = new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) { checkEnabled(); }
        };
        etFirst.addTextChangedListener(watcher);
        etLast.addTextChangedListener(watcher);
        etBirthday.addTextChangedListener(watcher);
        rgGender.setOnCheckedChangeListener((g, i) -> checkEnabled());

        // 5) on Continue: save into DB, then go on
        btnContinue.setOnClickListener(v -> {
            String first    = etFirst.getText().toString().trim();
            String last     = etLast.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            int checkedId   = rgGender.getCheckedRadioButtonId();
            String gender   = "";
            if (checkedId == R.id.rbWoman) gender = "WOMAN";
            else if (checkedId == R.id.rbMan) gender = "MAN";

            // *save* in your SQLite
            dbHelper.updateUserProfile(userId, first, last, gender, birthday);

            // now pass along userId into AddPhotosActivity
            Intent intent = new Intent(this, AddPhotosActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void checkEnabled() {
        boolean ok = etFirst.getText().length() > 0
                && etLast.getText().length() > 0
                && etBirthday.getText().length() > 0
                && rgGender.getCheckedRadioButtonId() != -1;
        btnContinue.setEnabled(ok);
    }

    // simple base for TextWatcher
    private abstract class SimpleTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
        public void onTextChanged(CharSequence s, int a, int b, int c) {}
    }
}
