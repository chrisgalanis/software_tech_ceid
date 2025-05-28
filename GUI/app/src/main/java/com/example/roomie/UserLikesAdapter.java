package com.example.roomie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserLikesAdapter extends RecyclerView.Adapter<UserLikesAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private DatabaseHelper db;
    private long currentUserId;

    public UserLikesAdapter(
            Context context, List<User> userList, DatabaseHelper db, long currentUserId) {
        this.context = context;
        this.userList = userList;
        this.db = db;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_like, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.firstName + " " + user.lastName);

        // View Profile button functionality
        holder.btnViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, user.id);
            context.startActivity(intent);
        });

        holder.btnAccept.setOnClickListener(v -> {
            db.createMatch(user.id, currentUserId);
            db.unlikeUser(user.id, currentUserId);
            userList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(
                    context,
                    "Matched with " + user.firstName + " from your likes",
                    Toast.LENGTH_SHORT
            ).show();
        });

        holder.btnReject.setOnClickListener(v -> {
            db.unlikeUser(user.id, currentUserId);
            userList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(
                    context,
                    "Removed " + user.firstName + " from your likes",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        ImageButton btnAccept, btnReject;
        Button btnViewProfile;    // <-- added

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage        = itemView.findViewById(R.id.userImage);
            userName         = itemView.findViewById(R.id.userName);
            btnAccept        = itemView.findViewById(R.id.btnAccept);
            btnReject        = itemView.findViewById(R.id.btnReject);
            btnViewProfile   = itemView.findViewById(R.id.btnViewProfile);  // <-- bind
        }
    }
}
