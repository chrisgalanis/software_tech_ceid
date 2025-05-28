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
import com.bumptech.glide.Glide; // <<< --- IMPORT GLIDE ---
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

  private DatabaseHelper dbHelper;
  private long currentUserId = -1;
  private long chatPartnerId = -1;
  private String chatPartnerNameForDisplay;
  private String chatPartnerAvatarUrlString; // <<< To store the URL from Intent
  private int chatPartnerProfileImageResFallback; // <<< For fallback drawable resource

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

    currentUserId = SessionManager.get().getUserId();
    if (currentUserId == -1) {
      Log.e("ChatActivity", "Critical Error: currentUserId is -1. Cannot proceed.");
      Toast.makeText(this, "Login error. Please restart.", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

    Intent intent = getIntent();
    if (intent != null) {
      chatPartnerId = intent.getLongExtra("CHAT_PARTNER_ID", -1L);
      chatPartnerNameForDisplay = intent.getStringExtra("CHAT_PARTNER_NAME");
      // Get the STRING URL for the partner's avatar from the intent
      chatPartnerAvatarUrlString = intent.getStringExtra("CHAT_PARTNER_AVATAR_URL");
      // Get the INT FALLBACK resource ID from the intent
      chatPartnerProfileImageResFallback =
          intent.getIntExtra("PROFILE_IMAGE_RES_FALLBACK", R.drawable.roomie_logo);

      Log.d(
          "ChatActivity",
          "onCreate: Received CHAT_PARTNER_ID="
              + chatPartnerId
              + ", CHAT_PARTNER_NAME="
              + chatPartnerNameForDisplay
              + ", CHAT_PARTNER_AVATAR_URL="
              + chatPartnerAvatarUrlString
              + ", PROFILE_IMAGE_RES_FALLBACK="
              + chatPartnerProfileImageResFallback);

      if (chatPartnerId == -1L) {
        Log.e("ChatActivity", "onCreate: CHAT_PARTNER_ID not received or invalid.");
        Toast.makeText(this, "Error: Could not load chat partner details.", Toast.LENGTH_LONG)
            .show();
        finish();
        return;
      }
      // chatPartnerNameForDisplay null check is fine as is
    } else {
      Log.e("ChatActivity", "onCreate: Intent was null!");
      Toast.makeText(this, "Error: Could not load chat details.", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

    // Initialize views
    ivBackButton = findViewById(R.id.ivBackButton);
    ivToolbarProfile = findViewById(R.id.ivToolbarProfile);
    tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
    ivCenterProfile = findViewById(R.id.ivCenterProfile);
    btnViewProfile = findViewById(R.id.btnViewProfile);
    rvChatMessages = findViewById(R.id.rvChatMessages);
    etMessage = findViewById(R.id.etMessage);
    ivEmoji = findViewById(R.id.ivEmoji);
    ivMic = findViewById(R.id.ivMic);
    ivAttach = findViewById(R.id.ivAttach);
    ivSend = findViewById(R.id.ivSend);

    // Set up UI
    if (tvToolbarTitle != null && chatPartnerNameForDisplay != null) {
      tvToolbarTitle.setText(chatPartnerNameForDisplay);
    } else if (tvToolbarTitle != null) {
      tvToolbarTitle.setText("Chat"); // Fallback
    }

    // --- CORRECTED: Load CHAT PARTNER's avatar into Toolbar and Center profile views ---
    if (ivToolbarProfile != null) {
      if (chatPartnerAvatarUrlString != null && !chatPartnerAvatarUrlString.isEmpty()) {
        Glide.with(this) // Context is 'this' (ChatActivity)
            .load(chatPartnerAvatarUrlString) // Load partner's URL
            .placeholder(chatPartnerProfileImageResFallback)
            .error(chatPartnerProfileImageResFallback)
            .into(ivToolbarProfile);
      } else if (chatPartnerProfileImageResFallback != 0) {
        ivToolbarProfile.setImageResource(chatPartnerProfileImageResFallback);
      } else {
        ivToolbarProfile.setImageResource(R.drawable.roomie_logo); // Ultimate fallback
      }
    }

    if (ivCenterProfile != null) {
      if (chatPartnerAvatarUrlString != null && !chatPartnerAvatarUrlString.isEmpty()) {
        Glide.with(this) // Context is 'this' (ChatActivity)
            .load(chatPartnerAvatarUrlString) // Load partner's URL
            .placeholder(chatPartnerProfileImageResFallback)
            .error(chatPartnerProfileImageResFallback)
            .into(ivCenterProfile);
      } else if (chatPartnerProfileImageResFallback != 0) {
        ivCenterProfile.setImageResource(chatPartnerProfileImageResFallback);
      } else {
        ivCenterProfile.setImageResource(R.drawable.roomie_logo); // Ultimate fallback
      }
    }

    // Setup RecyclerView
    messageList = new ArrayList<>();
    chatMessageAdapter = new ChatMessageAdapter(this, messageList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setStackFromEnd(true);
    rvChatMessages.setLayoutManager(linearLayoutManager);
    rvChatMessages.setAdapter(chatMessageAdapter);

    loadMessagesFromDb();

    // Set Click Listeners
    // ... (your existing click listeners) ...
    ivBackButton.setOnClickListener(v -> onBackPressed());
    btnViewProfile.setOnClickListener(
        v -> {
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

    List<ChatMessage> dbMessages =
        dbHelper.getChatMessagesBetweenUsers(currentUserId, chatPartnerId);

    messageList.clear();

    messageList.addAll(dbMessages);

    chatMessageAdapter.notifyDataSetChanged();

    if (!messageList.isEmpty()) {

      rvChatMessages.scrollToPosition(messageList.size() - 1);
    }
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

        User currentUserForAvatar = dbHelper.getUserById(currentUserId);
        String currentUserAvatarUrl =
            (currentUserForAvatar != null && currentUserForAvatar.avatarUrl != null)
                ? currentUserForAvatar.avatarUrl
                : null;

        ChatMessage newMessage =
            new ChatMessage(
                String.valueOf(currentUserId),
                String.valueOf(chatPartnerId),
                messageText,
                timeString,
                true, // isSentByCurrentUser
                currentUserAvatarUrl // Pass current user's avatar URL for their sent messages
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
