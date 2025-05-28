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

  private static final int VIEW_TYPE_SENT     = 1;
  private static final int VIEW_TYPE_RECEIVED = 2;

  private final Context context;
  private final List<ChatMessage> messageList;
  private final DatabaseHelper dbHelper;

  public ChatMessageAdapter(Context context, List<ChatMessage> messageList) {
    this.context     = context;
    this.messageList = messageList;
    this.dbHelper    = new DatabaseHelper(context);
  }

  @Override
  public int getItemViewType(int position) {
    return messageList.get(position).isSentByCurrentUser
            ? VIEW_TYPE_SENT
            : VIEW_TYPE_RECEIVED;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(
          @NonNull ViewGroup parent,
          int viewType
  ) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == VIEW_TYPE_SENT) {
      View view = inflater.inflate(
              R.layout.item_chat_message_sent,
              parent,
              false
      );
      return new SentMessageViewHolder(view);
    } else {
      View view = inflater.inflate(
              R.layout.item_chat_message_received,
              parent,
              false
      );
      return new ReceivedMessageViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(
          @NonNull RecyclerView.ViewHolder holder,
          int position
  ) {
    ChatMessage msg = messageList.get(position);
    if (holder instanceof SentMessageViewHolder) {
      ((SentMessageViewHolder) holder).bind(msg);
    } else {
      ((ReceivedMessageViewHolder) holder).bind(msg);
    }
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  // ─── Sent ViewHolder ─────────────────────────────────────────────────────

  static class SentMessageViewHolder extends RecyclerView.ViewHolder {
    TextView tvMessageText, tvMessageTimestamp;

    SentMessageViewHolder(@NonNull View itemView) {
      super(itemView);
      tvMessageText      = itemView.findViewById(R.id.tvMessageText);
      tvMessageTimestamp = itemView.findViewById(R.id.tvMessageTimestamp);
    }

    void bind(ChatMessage message) {
      tvMessageText.setText(message.messageText);
      tvMessageTimestamp.setText(message.timestamp);
    }
  }

  // ─── Received ViewHolder ─────────────────────────────────────────────────

  // Note: non-static so it can access ChatMessageAdapter.this.dbHelper
  class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
    ImageView ivSenderProfile;
    TextView  tvMessageText, tvMessageTimestamp;
    Button    btnReport;
    View      messageContent;

    ReceivedMessageViewHolder(@NonNull View itemView) {
      super(itemView);
      ivSenderProfile    = itemView.findViewById(R.id.ivSenderProfile);
      tvMessageText      = itemView.findViewById(R.id.tvMessageText);
      tvMessageTimestamp = itemView.findViewById(R.id.tvMessageTimestamp);
      btnReport          = itemView.findViewById(R.id.btnReport);
      messageContent     = itemView.findViewById(R.id.messageContent);
    }

    void bind(ChatMessage message) {
      // 1) Standard binding
      tvMessageText.setText(message.messageText);
      tvMessageTimestamp.setText(message.timestamp);

      String avatarUrl = message.senderAvatarUrl;
      if (avatarUrl != null && !avatarUrl.isEmpty()) {
        Glide.with(itemView.getContext())
                .load(avatarUrl)
                .placeholder(R.drawable.roomie_logo)
                .error(R.drawable.roomie_logo)
                .into(ivSenderProfile);
      } else {
        ivSenderProfile.setImageResource(R.drawable.roomie_logo);
      }

      // 2) Toggle and handle report
      btnReport.setVisibility(View.GONE);
      messageContent.setOnClickListener(v -> {
        btnReport.setVisibility(
                btnReport.getVisibility() == View.VISIBLE
                        ? View.GONE
                        : View.VISIBLE
        );
      });

      btnReport.setOnClickListener(v -> {
        long msgId = message.messageId; // ensure ChatMessage exposes its DB id
        long row   = dbHelper.insertMessageReport(msgId, "Inappropriate message");
        if (row != -1) {
          Toast.makeText(itemView.getContext(),
                  "Message reported!",
                  Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(itemView.getContext(),
                  "Failed to report message.",
                  Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
