package com.example.roomie;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class AdminMessageReportsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private MessageReportAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message_reports);

        // set up toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Review Message Reports");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // init DB helper
        dbHelper = new DatabaseHelper(this);

        // find RecyclerView and wire it up
        recyclerView = findViewById(R.id.reportsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // load pending message‐reports from DB
        List<MessageReport> pending = dbHelper.getAllPendingMessageReports();

        // set up the MessageReportAdapter
        adapter = new MessageReportAdapter(this, pending);
        recyclerView.setAdapter(adapter);

        // bottom navigation (if you have one)
        AdminNavigationHelper.setup(
                findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_messages
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
