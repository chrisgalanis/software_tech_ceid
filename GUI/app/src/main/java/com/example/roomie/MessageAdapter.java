package com.example.roomie;

import com.example.roomie.MessageItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

<<<<<<< HEAD
    private List<MessageItem> messageList;

    public MessageAdapter(List<MessageItem> messageList) {
        this.messageList = messageList;
=======
    private List<MessageItem> messageList; // This list is for the adapter
    private OnMessageItemClickListener listener;

    public interface OnMessageItemClickListener {
        void onMessageItemClick(MessageItem messageItem, int position);
    }

    public MessageAdapter(List<MessageItem> messageList, OnMessageItemClickListener listener) {
        this.messageList = messageList;
        this.listener = listener;
>>>>>>> 3f39587 (messages-page done)
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
=======
        // IMPORTANT: Make sure R.layout.item_message is the correct name of your XML layout file
        // for a single row in your messages list.
>>>>>>> 3f39587 (messages-page done)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageItem currentItem = messageList.get(position);
<<<<<<< HEAD
        holder.textViewName.setText(currentItem.getName());
        holder.textViewLastMessage.setText(currentItem.getLastMessage());
        holder.textViewTime.setText(currentItem.getTime());
        holder.imageViewProfile.setImageResource(currentItem.getProfileImageResource());

        // Set an OnClickListener for each item to handle clicks (e.g., to open the chat)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
=======
        holder.bind(currentItem, listener);
    }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

    // ViewHolder Inner Class
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewLastMessage;
        TextView textViewTimestamp;
        ImageView imageViewProfile;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // IMPORTANT: The R.id.xxx names below MUST match the android:id="@+id/xxx"
            // attributes in your R.layout.item_message XML file.
            textViewName = itemView.findViewById(R.id.textViewMessageItemName);
            textViewLastMessage = itemView.findViewById(R.id.textViewMessageItemLastMsg);
            textViewTimestamp = itemView.findViewById(R.id.textViewMessageItemTimestamp);
            imageViewProfile = itemView.findViewById(R.id.imageViewMessageItemProfile);
        }

        public void bind(final MessageItem messageItem, final OnMessageItemClickListener listener) {
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
                // Optional: imageViewProfile.setImageResource(R.drawable.default_placeholder);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Use the 'messageItem' passed to this bind method.
                            // This 'messageItem' is the specific item this ViewHolder is currently bound to.
                            listener.onMessageItemClick(messageItem, position);
                        }
                    }
                }
            });
        }
    }
  }
>>>>>>> 3f39587 (messages-page done)
