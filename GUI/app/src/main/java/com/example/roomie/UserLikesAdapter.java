package com.example.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserLikesAdapter extends RecyclerView.Adapter<UserLikesAdapter.ViewHolder> {

    private Context context;
    private List<String> userList;

    public UserLikesAdapter(Context context, List<String> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_like, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String user = userList.get(position);
        holder.userName.setText(user);

        holder.btnAccept.setOnClickListener(v ->
                Toast.makeText(context, "This action would match with " + user, Toast.LENGTH_SHORT).show()
        );

        holder.btnReject.setOnClickListener(v ->
                Toast.makeText(context, "This action would remove " + user + " from feed", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        ImageButton btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
