package com.example.roomie;

import android.content.Context;
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

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_SENT = 1;
  private static final int VIEW_TYPE_RECEIVED = 2;

  private Context context;
  private List<ChatMessage> messageList;

  public ChatMessageAdapter(Context context, List<ChatMessage> messageList) {
    this.context = context;
    this.messageList = messageList;
  }

  @Override
  public int getItemViewType(int position) {
    ChatMessage message = messageList.get(position);
    return message.isSentByCurrentUser() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_SENT) {
      View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_chat_message_sent, parent, false);
      return new SentMessageViewHolder(view);
    } else {
      View view = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_chat_message_received, parent, false);
      return new ReceivedMessageViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ChatMessage message = messageList.get(position);
    if (holder.getItemViewType() == VIEW_TYPE_SENT) {
      ((SentMessageViewHolder) holder).bind(message);
    } else {
      ((ReceivedMessageViewHolder) holder).bind(message);
    }
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  // ViewHolder for Sent Messages
  static class SentMessageViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessageText;
    TextView tvMessageTimestamp;

    SentMessageViewHolder(@NonNull View itemView) {
      super(itemView);
      tvMessageText = itemView.findViewById(R.id.tvMessageText);
      tvMessageTimestamp = itemView.findViewById(R.id.tvMessageTimestamp);
    }

    void bind(ChatMessage message) {
      tvMessageText.setText(message.getMessageText());
      tvMessageTimestamp.setText(message.getTimestamp());
    }
  }

  // ViewHolder for Received Messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
      ImageView ivSenderProfile;
      TextView tvMessageText;
      TextView tvMessageTimestamp;
      Button btnReport;
      View messageContent;

      ReceivedMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ivSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
        tvMessageText = itemView.findViewById(R.id.tvMessageText);
        tvMessageTimestamp = itemView.findViewById(R.id.tvMessageTimestamp);
        btnReport = itemView.findViewById(R.id.btnReport);
        messageContent = itemView.findViewById(R.id.messageContent);
      }

      void bind(ChatMessage message) {
        tvMessageText.setText(message.getMessageText());
        tvMessageTimestamp.setText(message.getTimestamp());

        String avatarUrl = message.getSenderAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
          Glide.with(itemView.getContext())
                  .load(avatarUrl)
                  .placeholder(R.drawable.roomie_logo)
                  .error(R.drawable.roomie_logo)
                  .into(ivSenderProfile);
        } else {
          ivSenderProfile.setImageResource(R.drawable.roomie_logo);
        }

        btnReport.setVisibility(View.GONE); // always hide first

        // Toggle visibility when the message content is clicked
        messageContent.setOnClickListener(v -> {
          btnReport.setVisibility(
                  btnReport.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
          );
        });

        btnReport.setOnClickListener(v -> {
          Toast.makeText(itemView.getContext(),
                  "Report clicked for: " + message.getMessageText(),
                  Toast.LENGTH_SHORT).show();
        });
      }
    }

}
