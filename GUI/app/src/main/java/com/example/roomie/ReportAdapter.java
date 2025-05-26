package com.example.roomie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private final List<Report>   reports;
    private final Context        ctx;
    private final DatabaseHelper dbHelper;

    public ReportAdapter(Context ctx, List<Report> reports) {
        this.ctx      = ctx;
        this.reports  = reports;
        this.dbHelper = new DatabaseHelper(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report r = reports.get(position);

        holder.username.setText(r.reportedUserName);
        holder.reportText.setText(r.text);

        // load first photo, if any
        List<String> photos = dbHelper.getUserPhotos(r.reportedUserId);
        if (!photos.isEmpty()) {
            Uri uri = Uri.parse(photos.get(0));
            Glide.with(holder.avatar.getContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_profile_placeholder);
        }

        // VIEW PROFILE
        holder.viewProfile.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent intent = new Intent(c, OtherUserProfileActivity.class);
            intent.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, r.reportedUserId);
            c.startActivity(intent);
        });

        // DISMISS
        holder.dismiss.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                dbHelper.dismissReport(r.id);
                reports.remove(pos);
                notifyItemRemoved(pos);
                Toast.makeText(ctx, "Report dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        // WARN USER
        holder.warn.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                dbHelper.warnUser(r.reportedUserId, r.id);
                reports.remove(pos);
                notifyItemRemoved(pos);
                Toast.makeText(ctx, "User warned", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView  username;
        TextView  reportText;
        TextView  viewProfile;
        Button    dismiss;
        Button    warn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar       = itemView.findViewById(R.id.avatarImageView);
            username     = itemView.findViewById(R.id.usernameTextView);
            reportText   = itemView.findViewById(R.id.reportTextView);
            viewProfile  = itemView.findViewById(R.id.viewProfileTextView);
            dismiss      = itemView.findViewById(R.id.dismissButton);
            warn         = itemView.findViewById(R.id.warnButton);
        }
    }
}
