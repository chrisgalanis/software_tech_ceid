package com.example.roomie;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class AdminReportsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView   recyclerView;
    private ReportAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        // set up toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Reported Users");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // init DB helper
        dbHelper = new DatabaseHelper(this);

        // find RecyclerView
        recyclerView = findViewById(R.id.reportsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // load pending reports from DB
        List<Report> pendingReports = dbHelper.getAllPendingReports();

        // set up adapter
        adapter = new ReportAdapter(this, pendingReports);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
