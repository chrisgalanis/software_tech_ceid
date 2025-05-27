package com.example.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.VH> {
    private final List<Warning>  warnings;
    private final Context        ctx;
    private final DatabaseHelper dbHelper;

    public WarningAdapter(Context ctx, List<Warning> warnings) {
        this.ctx      = ctx;
        this.warnings = warnings;
        this.dbHelper = new DatabaseHelper(ctx);
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_warning, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Warning w = warnings.get(pos);
        h.text.setText(w.text);
        h.time.setText(w.timestamp);

        // disable button if already acknowledged
        boolean isAck = "ACKNOWLEDGED".equals(w.status);
        h.ack.setEnabled(!isAck);
        h.ack.setText(isAck ? "Acknowledged" : "Acknowledge");

        h.ack.setOnClickListener(v -> {
            dbHelper.acknowledgeWarning(w.id);
            w.status = "ACKNOWLEDGED";
            notifyItemChanged(pos);
            Toast.makeText(ctx, "Warning acknowledged", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return warnings.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView text, time;
        Button   ack;

        VH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tvWarningText);
            time = itemView.findViewById(R.id.tvWarningTime);
            ack  = itemView.findViewById(R.id.btnAcknowledge);
        }
    }
}