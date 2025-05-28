package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity
    implements MessageAdapter.OnMessageItemClickListener {

  private RecyclerView recyclerViewMessages;
  private MessageAdapter messageAdapter;
  private List<MessageItem> messageList;
  private long currentUserId;
  private DatabaseHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages);

    dbHelper = new DatabaseHelper(this);

    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

    messageList = new ArrayList<>();
    messageAdapter = new MessageAdapter(messageList, this);
    recyclerViewMessages.setAdapter(messageAdapter);

    loadMatchedUsersList();

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_messages);
  }

  @Override
  protected void onResume() {
    super.onResume();
    loadMatchedUsersList();
  }

  private void loadMatchedUsersList() {
    if (currentUserId < 0) {
      Log.e("MessagesActivity", "Cannot load matches, currentUserId is invalid.");
      return;
    }

    messageList.clear();

    List<Long> matchedPartnerIds = dbHelper.getMatches(currentUserId);

    Log.d(
        "MessagesActivity",
        "Found " + matchedPartnerIds.size() + " matched partners for user " + currentUserId);

    if (matchedPartnerIds.isEmpty()) {
      Toast.makeText(this, "No matches yet!", Toast.LENGTH_SHORT).show();
    } else {
      for (long partnerId : matchedPartnerIds) {
        User partnerUser = dbHelper.getUserById(partnerId);
        if (partnerUser != null) {
          String lastMessagePreview = "Tap to chat!";
          String timestampPreview = "";

          ChatMessage latestMsgObject =
              dbHelper.getLatestChatMessage(
                  currentUserId, partnerUser.id); // Call without profileImageRes

          if (latestMsgObject != null) {
            lastMessagePreview = latestMsgObject.getMessageText();
            timestampPreview = latestMsgObject.getTimestamp();
          }

          String avatarUrlForListItem =
              (partnerUser.avatarUrl != null && !partnerUser.avatarUrl.isEmpty())
                  ? partnerUser.avatarUrl
                  : null; // Or a default URL string if you have one, or handle null in adapter

          messageList.add(
              new MessageItem(
                  partnerUser.id,
                  partnerUser.getDisplayName(),
                  lastMessagePreview,
                  timestampPreview,
                  avatarUrlForListItem));
        } else {
          Log.w(
              "MessagesActivity",
              "Could not retrieve details for matched partner ID: " + partnerId);
        }
      }
    }
    messageAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
  }

  @Override
  public void onMessageItemClick(MessageItem clickedMessageItem, int position) {
    Log.d(
        "MessagesActivity",
        "Clicked on: "
            + clickedMessageItem.getDisplayName()
            + " with ID: "
            + clickedMessageItem.getUserId());

    Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
    intent.putExtra("CHAT_PARTNER_ID", clickedMessageItem.getUserId());
    intent.putExtra("CHAT_PARTNER_NAME", clickedMessageItem.getDisplayName());
    intent.putExtra("PROFILE_IMAGE_RES", clickedMessageItem.getProfileAvatarUrl());

    startActivity(intent);
  }
}
