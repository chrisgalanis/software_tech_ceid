package com.example.roomie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Make sure you have this import for Glide
// import com.bumptech.glide.request.RequestOptions; // Optional for more Glide options
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

  private List<MessageItem> messageList;
  private OnMessageItemClickListener listener;

  public interface OnMessageItemClickListener {
    void onMessageItemClick(MessageItem messageItem, int position);
  }

  public MessageAdapter(List<MessageItem> messageList, OnMessageItemClickListener listener) {
    this.messageList = messageList;
    this.listener = listener;
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
    holder.bind(currentItem, listener);
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  public static class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewName;
    public TextView textViewLastMessage;
    public TextView textViewTimestamp;
    public ImageView imageViewProfile; // This is the ImageView for MessageItem

    public MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewName = itemView.findViewById(R.id.textViewMessageItemName);
      textViewLastMessage = itemView.findViewById(R.id.textViewMessageItemLastMsg);
      textViewTimestamp = itemView.findViewById(R.id.textViewMessageItemTimestamp);
      imageViewProfile = itemView.findViewById(R.id.imageViewMessageItemProfile);
    }

    public void bind(final MessageItem messageItem, final OnMessageItemClickListener listener) {
      // Use getDisplayName() from MessageItem
      if (messageItem.getDisplayName() != null) {
        textViewName.setText(messageItem.getDisplayName());
      } else {
        textViewName.setText(""); // Or some default
      }

      if (messageItem.getLastMessage() != null) {
        textViewLastMessage.setText(messageItem.getLastMessage());
      } else {
        textViewLastMessage.setText("");
      }

      if (messageItem.getTimestamp() != null) {
        textViewTimestamp.setText(messageItem.getTimestamp());
      } else {
        textViewTimestamp.setText("");
      }

      // Use getProfileAvatarUrl() from MessageItem
      String avatarUrl = messageItem.getProfileAvatarUrl();
      if (avatarUrl != null && !avatarUrl.isEmpty()) {
        Glide.with(itemView.getContext())
            .load(avatarUrl)
            .placeholder(R.drawable.roomie_logo) // Your placeholder
            .error(R.drawable.roomie_logo) // Your error placeholder
            // .apply(RequestOptions.circleCropTransform()) // If imageViewProfile is not a
            // CircleImageView and you want it cropped
            .into(imageViewProfile); // <<< CORRECTED: Use imageViewProfile
      } else {
        imageViewProfile.setImageResource(R.drawable.roomie_logo); // Default if no URL
      }

      itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                  listener.onMessageItemClick(messageItem, position);
                }
              }
            }
          });
    }
  }
}
