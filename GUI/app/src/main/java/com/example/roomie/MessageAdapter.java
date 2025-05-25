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
  private OnMessageItemClickListener listener;

  // --- 1. Interface for click events ---
  public interface OnMessageItemClickListener {
    void onMessageItemClick(MessageItem messageItem, int position);
  }

  // --- 2. Constructor that accepts the listener ---
  public MessageAdapter(List<MessageItem> messageList, OnMessageItemClickListener listener) {
    this.messageList = messageList;
    this.listener = listener;
  }

  @NonNull
  @Override
  public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate your item layout XML here (e.g., R.layout.item_message_preview)
    View itemView =
        LayoutInflater.from(parent.getContext())
            .inflate(
                R.layout.item_message,
                parent,
                false); // <<<<<<< ENSURE R.layout.item_message IS YOUR CORRECT LAYOUT FILE FOR EACH
    // ROW
    return new MessageViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    MessageItem currentItem = messageList.get(position);
    // --- 3. Call the ViewHolder's bind method ---
    holder.bind(currentItem, listener);
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  // --- 4. ViewHolder Class ---
  public static class MessageViewHolder extends RecyclerView.ViewHolder {
    // Declare views for your item layout (e.g., item_message.xml)
    public TextView textViewName;
    public TextView textViewLastMessage;
    public TextView textViewTimestamp;
    public ImageView imageViewProfile;

    public MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      // Initialize views - REPLACE R.id.xxx WITH YOUR ACTUAL IDs
      textViewName = itemView.findViewById(R.id.textViewMessageItemName); // <<<< YOUR ID
      textViewLastMessage = itemView.findViewById(R.id.textViewMessageItemLastMsg); // <<<< YOUR ID
      textViewTimestamp = itemView.findViewById(R.id.textViewMessageItemTimestamp); // <<<< YOUR ID
      imageViewProfile = itemView.findViewById(R.id.imageViewMessageItemProfile); // <<<< YOUR ID
    }

    // --- 5. Bind method in ViewHolder ---
    public void bind(final MessageItem messageItem, final OnMessageItemClickListener listener) {
      // Set data to views
      if (messageItem.getUserName() != null) {
        textViewName.setText(messageItem.getUserName());
      }
      if (messageItem.getLastMessage() != null) {
        textViewLastMessage.setText(messageItem.getLastMessage());
      }
      if (messageItem.getTimestamp() != null) {
        textViewTimestamp.setText(messageItem.getTimestamp());
      }
      if (messageItem.getProfileImageRes() != 0) {
        imageViewProfile.setImageResource(messageItem.getProfileImageRes());
      } else {
        // Maybe set a default placeholder if no image
        // imageViewProfile.setImageResource(R.drawable.default_avatar);
      }

      // --- 6. Set OnClickListener INSIDE ViewHolder's bind method (or constructor) ---
      itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (listener != null) {
                int position = getAdapterPosition(); // Get current position
                if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                  listener.onMessageItemClick(messageItem, position);
                }
              }
            }
          });
    }
  }
}
