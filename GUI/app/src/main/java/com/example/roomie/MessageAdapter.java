package com.example.roomie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

  private List<MessageItem> messageList;

  public MessageAdapter(List<MessageItem> messageList) {
    this.messageList = messageList;
  }

  @NonNull
  @Override
  public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
    return new MessageViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    MessageItem currentItem = messageList.get(position);
    holder.textViewName.setText(currentItem.getName());
    holder.textViewLastMessage.setText(currentItem.getLastMessage());
    holder.textViewTime.setText(currentItem.getTime());
    holder.imageViewProfile.setImageResource(currentItem.getProfileImageResource());

    // Set an OnClickListener for each item to handle clicks (e.g., to open the chat)
    holder.itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // Handle the click event here
            String name = currentItem.getName();
            // You might want to start a new activity or fragment to display the chat with this user
            // Example:
            // Intent intent = new Intent(v.getContext(), ChatActivity.class);
            // intent.putExtra("userName", name);
            // v.getContext().startActivity(intent);
          }
        });
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  public static class MessageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewProfile;
    public TextView textViewName;
    public TextView textViewLastMessage;
    public TextView textViewTime;

    public MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
      textViewName = itemView.findViewById(R.id.textViewName);
      textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
      textViewTime = itemView.findViewById(R.id.textViewTime);
    }
  }
}
