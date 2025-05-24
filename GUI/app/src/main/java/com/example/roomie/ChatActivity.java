package com.example.roomie;

import android.content.Intent; // Make sure this is imported
import android.os.Bundle;
import android.util.Log; // Make sure this is imported
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
import de.hdodenhof.circleimageview.CircleImageView; // Import if you use it here

public class ChatActivity extends AppCompatActivity {

    // Fields for ChatActivity
    private ImageView ivBackButton;
    private CircleImageView ivToolbarProfile; // For the toolbar
    private TextView tvToolbarTitle;        // For the toolbar
    private ImageView ivInfoButton;

    private CircleImageView ivCenterProfile;  // For the center profile view
    private Button btnViewProfile;

    private RecyclerView rvChatMessages;
    private ChatMessageAdapter chatMessageAdapter; // Adapter for individual chat messages
    private List<ChatMessage> messageList;      // List of ChatMessage objects

    private ImageView ivEmoji;
    private EditText etMessage;
    private ImageView ivMic;
    private ImageView ivAttach;
    private ImageView ivSend;

    // Fields to store data received from MessagesActivity
    private String chatPartnerName;
    private int chatPartnerProfileImageRes;
    // private String chatPartnerUserId; // If you pass a user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChatActivity", "onCreate: ChatActivity started.");
        setContentView(R.layout.activity_chat); // Layout for the chat screen

        // --- RECEIVE AND PROCESS INTENT DATA HERE ---
        Intent intent = getIntent();
        if (intent != null) {
            chatPartnerName = intent.getStringExtra("USER_NAME");
            chatPartnerProfileImageRes = intent.getIntExtra("PROFILE_IMAGE_RES", R.drawable.roomie_logo); // Default
            // chatPartnerUserId = intent.getStringExtra("USER_ID"); // If you pass a user ID

            Log.d("ChatActivity", "onCreate: Received USER_NAME=" + chatPartnerName + ", PROFILE_IMAGE_RES=" + chatPartnerProfileImageRes);
        } else {
            Log.e("ChatActivity", "onCreate: Intent was null!");
            // Handle case where intent is null, maybe finish activity or show an error
        }

        // Initialize your views from R.layout.activity_chat
        ivBackButton = findViewById(R.id.ivBackButton);
        ivToolbarProfile = findViewById(R.id.ivToolbarProfile); // Ensure this ID exists in activity_chat.xml
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);     // Ensure this ID exists
        ivInfoButton = findViewById(R.id.ivInfoButton);

        ivCenterProfile = findViewById(R.id.ivCenterProfile);   // Ensure this ID exists
        btnViewProfile = findViewById(R.id.btnViewProfile);

        rvChatMessages = findViewById(R.id.rvChatMessages);
        etMessage = findViewById(R.id.etMessage);
        ivEmoji = findViewById(R.id.ivEmoji);
        ivMic = findViewById(R.id.ivMic);
        ivAttach = findViewById(R.id.ivAttach);
        ivSend = findViewById(R.id.ivSend);

        // --- Use the received data to set up UI ---
        if (tvToolbarTitle != null && chatPartnerName != null) {
            tvToolbarTitle.setText(chatPartnerName);
        } else {
            Log.e("ChatActivity", "onCreate: tvToolbarTitle or chatPartnerName is null after intent processing.");
            // Potentially set a default title or handle error
        }

        if (ivToolbarProfile != null && chatPartnerProfileImageRes != 0) {
            ivToolbarProfile.setImageResource(chatPartnerProfileImageRes);
        }
        if (ivCenterProfile != null && chatPartnerProfileImageRes != 0) {
            ivCenterProfile.setImageResource(chatPartnerProfileImageRes);
        }

        // Setup RecyclerView for chat messages
        messageList = new ArrayList<>();
        loadDummyMessagesForUser(); // This method should use chatPartnerName/Id to load relevant messages
        chatMessageAdapter = new ChatMessageAdapter(this, messageList); // Assuming ChatMessageAdapter is for these messages
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvChatMessages.setLayoutManager(linearLayoutManager);
        rvChatMessages.setAdapter(chatMessageAdapter);

        // Set Click Listeners for ChatActivity's buttons
        ivBackButton.setOnClickListener(v -> onBackPressed());
        ivInfoButton.setOnClickListener(v -> Toast.makeText(ChatActivity.this, "Info clicked for " + chatPartnerName, Toast.LENGTH_SHORT).show());
        btnViewProfile.setOnClickListener(v -> Toast.makeText(ChatActivity.this, "View Profile for " + chatPartnerName, Toast.LENGTH_SHORT).show());
        ivSend.setOnClickListener(v -> sendMessage());
        // ... other listeners
    }

    private void loadDummyMessagesForUser() {
        // This should be populated based on chatPartnerName or a unique ID
        // For now, let's just log it
        Log.d("ChatActivity", "loadDummyMessagesForUser: called for " + chatPartnerName);
        messageList.clear(); // Clear any old messages

        if (chatPartnerName != null && chatPartnerName.equals("Mike M.")) {
             messageList.add(new ChatMessage(chatPartnerName, "currentUser", "Hello nice to meet you.", "00:01", false, chatPartnerProfileImageRes));
             messageList.add(new ChatMessage("currentUser", chatPartnerName, "I dont talk to strangers..", "4:19", true));
             messageList.add(new ChatMessage(chatPartnerName, "currentUser", "But why?", "4:20", false, chatPartnerProfileImageRes));
        } else if (chatPartnerName != null) {
            // Add some generic message if no specific dummy data
            messageList.add(new ChatMessage(chatPartnerName, "currentUser", "Hi there!", "00:01", false, chatPartnerProfileImageRes));
        }
        if (chatMessageAdapter != null) {
             chatMessageAdapter.notifyDataSetChanged();
        }
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            String timeString = android.text.format.DateFormat.format("HH:mm", timestamp).toString();
            ChatMessage newMessage = new ChatMessage(
                "currentUser", // Replace with actual current user ID
                chatPartnerName, // Or chatPartnerUserId
                messageText,
                timeString,
                true
            );
            messageList.add(newMessage);
            chatMessageAdapter.notifyItemInserted(messageList.size() - 1);
            rvChatMessages.scrollToPosition(messageList.size() - 1);
            etMessage.setText("");
        } else {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
