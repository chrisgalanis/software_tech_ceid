package com.example.roomie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
  private final List<Report> reports;
  private final Context ctx;
  private final DatabaseHelper dbHelper;

  public ReportAdapter(Context ctx, List<Report> reports) {
    this.ctx = ctx;
    this.reports = reports;
    this.dbHelper = new DatabaseHelper(ctx);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(ctx).inflate(R.layout.item_report, parent, false);
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
    holder.viewProfile.setOnClickListener(
        v -> {
          Intent intent = new Intent(ctx, OtherUserProfileActivity.class);
          intent.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, r.reportedUserId);
          ctx.startActivity(intent);
        });

    // DISMISS
    holder.dismiss.setOnClickListener(
        v -> {
          int pos = holder.getAdapterPosition();
          if (pos != RecyclerView.NO_POSITION) {
            dbHelper.dismissReport(r.id);
            reports.remove(pos);
            notifyItemRemoved(pos);
            Toast.makeText(ctx, "Report dismissed", Toast.LENGTH_SHORT).show();
          }
        });

    // WARN USER (with input dialog)
    holder.warn.setOnClickListener(
        v -> {
          int pos = holder.getAdapterPosition();
          if (pos == RecyclerView.NO_POSITION) return;

          // show dialog to enter warning message
          AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
          builder.setTitle("Warn User");

          final EditText input = new EditText(ctx);
          input.setHint("Enter warning message");
          input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
          builder.setView(input);

          builder.setPositiveButton(
              "Send",
              (dialog, which) -> {
                String warningMessage = input.getText().toString().trim();
                if (warningMessage.isEmpty()) {
                  Toast.makeText(ctx, "Warning text cannot be empty", Toast.LENGTH_SHORT).show();
                  return;
                }

                // create and insert Warning
                Warning warning =
                    new Warning(
                        0, // id (auto-generated)
                        r.reportedUserId, // target user
                        warningMessage, // message
                        "PENDING", // status
                        null // timestamp (DB default)
                        );
                long warningId = dbHelper.insertWarning(warning);
                if (warningId > 0) {
                  // optionally mark report handled
                  dbHelper.fullfillReport(r.id);

                  // update UI
                  reports.remove(pos);
                  notifyItemRemoved(pos);

                  Toast.makeText(ctx, "Warning sent (id=" + warningId + ")", Toast.LENGTH_SHORT)
                      .show();
                } else {
                  Toast.makeText(ctx, "Failed to send warning", Toast.LENGTH_SHORT).show();
                }
              });

          builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
          builder.show();
        });
  }

  @Override
  public int getItemCount() {
    return reports.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView avatar;
    TextView username;
    TextView reportText;
    TextView viewProfile;
    Button dismiss;
    Button warn;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      avatar = itemView.findViewById(R.id.avatarImageView);
      username = itemView.findViewById(R.id.usernameTextView);
      reportText = itemView.findViewById(R.id.reportTextView);
      viewProfile = itemView.findViewById(R.id.viewProfileTextView);
      dismiss = itemView.findViewById(R.id.dismissButton);
      warn = itemView.findViewById(R.id.warnButton);
    }
  }
}
