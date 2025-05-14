package com.example.roomie; // Replace with your package name

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView; // If using CircleImageView

public class ChatActivity extends AppCompatActivity {

    private ImageView ivBackButton;
    private CircleImageView ivToolbarProfile;
    private TextView tvToolbarTitle;
    private ImageView ivInfoButton;

    private CircleImageView ivCenterProfile;
    private Button btnViewProfile;

    private RecyclerView rvChatMessages;
    private ChatMessageAdapter chatMessageAdapter;
    private List<ChatMessage> messageList;

    private ImageView ivEmoji;
    private EditText etMessage;
    private ImageView ivMic;
    private ImageView ivAttach;
    private ImageView ivSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // Setup RecyclerView
        messageList = new ArrayList<>();
        // TODO: Populate messageList with actual chat data
        loadDummyMessages(); // Example method
        chatMessageAdapter = new ChatMessageAdapter(this, messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true); // To show latest messages at the bottom
        rvChatMessages.setLayoutManager(linearLayoutManager);
        rvChatMessages.setAdapter(chatMessageAdapter);

        // Set Click Listeners
        ivBackButton.setOnClickListener(v -> onBackPressed());

        ivInfoButton.setOnClickListener(v -> {
            // Handle info button click
            Toast.makeText(ChatActivity.this, "Info clicked", Toast.LENGTH_SHORT).show();
        });

        btnViewProfile.setOnClickListener(v -> {
            // Handle view profile button click
            Toast.makeText(ChatActivity.this, "View Profile clicked", Toast.LENGTH_SHORT).show();
            // Example: Intent to ProfileActivity
            // Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
            // intent.putExtra("USER_ID", "mike_m_id"); // Pass user identifier
            // startActivity(intent);
        });

        ivSend.setOnClickListener(v -> sendMessage());

        ivEmoji.setOnClickListener(v -> {
            // Handle emoji button click
            Toast.makeText(ChatActivity.this, "Emoji clicked", Toast.LENGTH_SHORT).show();
        });

        ivMic.setOnClickListener(v -> {
            // Handle mic button click
            Toast.makeText(ChatActivity.this, "Mic clicked", Toast.LENGTH_SHORT).show();
        });

        ivAttach.setOnClickListener(v -> {
            // Handle attach button click
            Toast.makeText(ChatActivity.this, "Attach clicked", Toast.LENGTH_SHORT).show();
        });

        // Optionally set the current item as selected
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // TODO: Replace "currentUser" with actual sender logic
            // TODO: Replace "Mike M." and profile resource with actual recipient/sender info
            // For simplicity, new messages are added as "sent" by current user
            long timestamp = System.currentTimeMillis(); // Or get a proper timestamp string
            String timeString = android.text.format.DateFormat.format("HH:mm", timestamp).toString();

            ChatMessage newMessage = new ChatMessage(
                "currentUser", // senderId
                "Mike M.",     // receiverId (or use a generic ID for the other person)
                messageText,
                timeString,
                true // isSentByCurrentUser
            );
            messageList.add(newMessage);
            chatMessageAdapter.notifyItemInserted(messageList.size() - 1);
            rvChatMessages.scrollToPosition(messageList.size() - 1);
            etMessage.setText("");
        } else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDummyMessages() {
        // Simulating loading existing messages
        messageList.add(new ChatMessage("mike_m_id", "currentUser", "Hello nice to meet you.", "00:01", false, R.drawable.roomie_logo));
        messageList.add(new ChatMessage("currentUser", "mike_m_id", "I dont talk to strangers..", "4:19", true)); // Current user doesn't need a profile pic for their own bubble usually
        messageList.add(new ChatMessage("mike_m_id", "currentUser", "But why?", "4:20", false, R.drawable.roomie_logo));
        // Add more messages as needed
    }

    // You will need a ChatMessage data class (POJO)
    // public static class ChatMessage { ... } - See below

    // You will need a RecyclerView.Adapter
    // public class ChatMessageAdapter extends RecyclerView.Adapter<...> { ... } - See below
}
