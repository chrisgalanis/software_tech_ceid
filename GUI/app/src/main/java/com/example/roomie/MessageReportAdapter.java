package com.example.roomie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageReportAdapter
        extends RecyclerView.Adapter<MessageReportAdapter.ViewHolder> {

    private final List<MessageReport> reports;
    private final Context ctx;
    private final DatabaseHelper dbHelper;

    public MessageReportAdapter(Context ctx, List<MessageReport> reports) {
        this.ctx      = ctx;
        this.reports  = reports;
        this.dbHelper = new DatabaseHelper(ctx);
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_message_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        MessageReport mr = reports.get(position);

        holder.tvMsgPreview.setText(mr.message.messageText);

        // 2) Show the report text
        holder.tvReportReason.setText(mr.text);

        // 3) Dismiss action
        holder.btnDismiss.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            dbHelper.deleteMessageReport(mr.id);
            reports.remove(pos);
            notifyItemRemoved(pos);
            Toast.makeText(ctx, "Report dismissed", Toast.LENGTH_SHORT).show();
        });

        // 4) Warn sender action
        holder.btnWarn.setOnClickListener(v -> {
            // Prompt for warning text

                String warnText = "Your message : '"+mr.message.messageText+"' has been deleted";
                // Insert warning
                Warning w = new Warning(
                        0,
                        mr.message.senderId,
                        warnText,
                        "PENDING",
                        null
                );
                long wid = dbHelper.insertWarning(w);
                if (wid > 0) {
                    // Dismiss the report too
                    dbHelper.deleteMessageReport(mr.id);
                    int pos2 = holder.getAdapterPosition();
                    reports.remove(pos2);
                    notifyItemRemoved(pos2);
                    Toast.makeText(ctx, "Sender warned", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "Failed to send warning", Toast.LENGTH_SHORT).show();
                }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsgPreview, tvReportReason;
        Button btnDismiss, btnWarn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMsgPreview   = itemView.findViewById(R.id.tvMsgPreview);
            tvReportReason = itemView.findViewById(R.id.tvReportReason);
            btnDismiss     = itemView.findViewById(R.id.btnDismissMsgReport);
            btnWarn        = itemView.findViewById(R.id.btnWarnMsgUser);
        }
    }
}
