package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

  // Fields for ChatActivity
  private DatabaseHelper dbHelper;
  private long currentUserId = -1; // ID of the logged-in user
  private long chatPartnerId = -1; // <<< Actual ID of the chat partner from Intent
  private String chatPartnerNameForDisplay; // For display in toolbar, etc.
  private int chatPartnerProfileImageRes;

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
    Log.d("ChatActivity", "onCreate: ChatActivity started.");
    setContentView(R.layout.activity_chat);

    dbHelper = new DatabaseHelper(this);

    // Get current user's ID (e.g., from SessionManager)
    currentUserId =
        SessionManager.get().getUserId(); // Ensure SessionManager is accessible and works
    if (currentUserId == -1) {
      Log.e("ChatActivity", "Critical Error: currentUserId is -1. Cannot proceed.");
      Toast.makeText(this, "Login error. Please restart.", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

    // --- RECEIVE AND PROCESS INTENT DATA HERE ---
    Intent intent = getIntent();
    if (intent != null) {
      // <<< CORRECTED: Get the long ID and the display name directly
      chatPartnerId = intent.getLongExtra("CHAT_PARTNER_ID", -1L);
      chatPartnerNameForDisplay = intent.getStringExtra("CHAT_PARTNER_NAME");
      chatPartnerProfileImageRes = intent.getIntExtra("PROFILE_IMAGE_RES", R.drawable.roomie_logo);

      Log.d(
          "ChatActivity",
          "onCreate: Received CHAT_PARTNER_ID="
              + chatPartnerId
              + ", CHAT_PARTNER_NAME="
              + chatPartnerNameForDisplay
              + ", PROFILE_IMAGE_RES="
              + chatPartnerProfileImageRes);

      if (chatPartnerId == -1L) {
        Log.e(
            "ChatActivity",
            "onCreate: CHAT_PARTNER_ID not received correctly from intent or is invalid.");
        Toast.makeText(this, "Error: Could not load chat partner details.", Toast.LENGTH_LONG)
            .show();
        finish();
        return;
      }
      if (chatPartnerNameForDisplay == null) {
        // Fallback or error if name is crucial for display and not received
        Log.w("ChatActivity", "onCreate: CHAT_PARTNER_NAME is null. Using a default or ID.");
        // You might fetch the name from DB using chatPartnerId if needed here,
        // or ensure MessagesActivity always sends it. For now, we'll proceed.
        // User partnerUser = dbHelper.getUserById(chatPartnerId);
        // if (partnerUser != null) chatPartnerNameForDisplay = partnerUser.firstName;
        // else chatPartnerNameForDisplay = "Chat Partner";
      }

    } else {
      Log.e("ChatActivity", "onCreate: Intent was null!");
      Toast.makeText(this, "Error: Could not load chat details.", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

    // Initialize your views from R.layout.activity_chat
    ivBackButton = findViewById(R.id.ivBackButton);
    ivToolbarProfile = findViewById(R.id.ivToolbarProfile);
    tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
    ivInfoButton = findViewById(R.id.ivInfoButton);
    ivCenterProfile = findViewById(R.id.ivCenterProfile);
    btnViewProfile = findViewById(R.id.btnViewProfile);
    rvChatMessages = findViewById(R.id.rvChatMessages);
    etMessage = findViewById(R.id.etMessage);
    ivEmoji = findViewById(R.id.ivEmoji);
    ivMic = findViewById(R.id.ivMic);
    ivAttach = findViewById(R.id.ivAttach);
    ivSend = findViewById(R.id.ivSend);

    // --- Use the received data to set up UI ---
    if (tvToolbarTitle != null && chatPartnerNameForDisplay != null) {
      tvToolbarTitle.setText(chatPartnerNameForDisplay);
    } else if (tvToolbarTitle != null) {
      tvToolbarTitle.setText("Chat"); // Fallback title
    }

    if (ivToolbarProfile != null && chatPartnerProfileImageRes != 0) {
      ivToolbarProfile.setImageResource(chatPartnerProfileImageRes);
    }
    if (ivCenterProfile != null && chatPartnerProfileImageRes != 0) {
      ivCenterProfile.setImageResource(chatPartnerProfileImageRes);
    }

    // Setup RecyclerView for chat messages
    messageList = new ArrayList<>();
    chatMessageAdapter = new ChatMessageAdapter(this, messageList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setStackFromEnd(true);
    rvChatMessages.setLayoutManager(linearLayoutManager);
    rvChatMessages.setAdapter(chatMessageAdapter);

    loadMessagesFromDb();

    // Set Click Listeners
    ivBackButton.setOnClickListener(v -> onBackPressed());
    //REPORT BUTTON
    ivInfoButton.setOnClickListener(
        v ->
            Toast.makeText(
                    ChatActivity.this,
                    "Info for "
                        + (chatPartnerNameForDisplay != null ? chatPartnerNameForDisplay : "User"),
                    Toast.LENGTH_SHORT)
                .show());
    //VIEW PROFILE BUTTON
    btnViewProfile.setOnClickListener(v -> {

        Intent i = new Intent(this, OtherUserProfileActivity.class);
        i.putExtra(OtherUserProfileActivity.EXTRA_USER_ID, chatPartnerId);
        startActivity(i);

    });
    ivSend.setOnClickListener(v -> sendMessage());
  }

  private void loadMessagesFromDb() {
    if (currentUserId == -1 || chatPartnerId == -1) {
      Log.e(
          "ChatActivity",
          "Cannot load messages: Invalid user IDs. currentUserId="
              + currentUserId
              + ", chatPartnerId="
              + chatPartnerId);
      Toast.makeText(this, "Error loading messages.", Toast.LENGTH_SHORT).show();
      return;
    }
    Log.d(
        "ChatActivity",
        "Loading messages from DB for user " + currentUserId + " and partner " + chatPartnerId);
    List<ChatMessage> dbMessages =
        dbHelper.getChatMessagesBetweenUsers(
            currentUserId, chatPartnerId, chatPartnerProfileImageRes);
    messageList.clear();
    messageList.addAll(dbMessages);
    chatMessageAdapter.notifyDataSetChanged();
    if (!messageList.isEmpty()) {
      rvChatMessages.scrollToPosition(messageList.size() - 1);
    }
    Log.d("ChatActivity", "Loaded " + messageList.size() + " messages from DB.");
  }

  private void sendMessage() {
    String messageText = etMessage.getText().toString().trim();
    if (!messageText.isEmpty()) {
      if (currentUserId == -1 || chatPartnerId == -1) {
        Toast.makeText(this, "Error: Cannot send message. User IDs invalid.", Toast.LENGTH_SHORT)
            .show();
        return;
      }

      long timestampMs = System.currentTimeMillis();
      long newRowId =
          dbHelper.addChatMessage(currentUserId, chatPartnerId, messageText, timestampMs);

      if (newRowId != -1) {
        String timeString = android.text.format.DateFormat.format("HH:mm", timestampMs).toString();
        ChatMessage newMessage =
            new ChatMessage(
                String.valueOf(currentUserId),
                String.valueOf(chatPartnerId),
                messageText,
                timeString,
                true // isSentByCurrentUser
                );
        messageList.add(newMessage);
        chatMessageAdapter.notifyItemInserted(messageList.size() - 1);
        rvChatMessages.scrollToPosition(messageList.size() - 1);
        etMessage.setText("");
      } else {
        Toast.makeText(this, "Failed to send message (DB error).", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
    }
  }
}
