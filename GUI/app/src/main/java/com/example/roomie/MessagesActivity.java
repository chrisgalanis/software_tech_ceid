package com.example.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Make sure Log is imported
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
  private DatabaseHelper dbHelper; // <<< ADD DatabaseHelper instance

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages);

    dbHelper = new DatabaseHelper(this); // <<< INITIALIZE DatabaseHelper

    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

    messageList = new ArrayList<>();
    //populateDummyMessageList(); // <<< CALL NEW METHOD TO POPULATE

    messageAdapter = new MessageAdapter(messageList, this);
    recyclerViewMessages.setAdapter(messageAdapter);

    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_messages);
  }

  private void populateDummyMessageList() {
    // Define your dummy users' details and their initial "last message"
    // Pair: Display Name, Email, Password, First, Last, Gender, Birthday, Initial Message Text,
    // Initial Message Time
    Object[][] dummyUserData = {
      {
        "Chris G.",
        "chris.g@example.com",
        "pass123",
        "Chris",
        "G.",
        "Male",
        "1990-01-01",
        "2 new messages!!",
        "13:40"
      },
      {
        "Titos H.",
        "titos.h@example.com",
        "pass123",
        "Titos",
        "H.",
        "Male",
        "1991-02-02",
        "New message!!",
        "19:43"
      },
      {
        "Kostas M.",
        "kostas.m@example.com",
        "pass123",
        "Kostas",
        "M.",
        "Male",
        "1992-03-03",
        "I am hungry, let's go!!!!",
        "10:13"
      },
      {
        "Mike M.",
        "mike.m@example.com",
        "pass123",
        "Mike",
        "M.",
        "Male",
        "1993-04-04",
        "But why?",
        "04:20"
      },
      {
        "Rafas P.",
        "rafas.p@example.com",
        "pass123",
        "Rafas",
        "P.",
        "Male",
        "1994-05-05",
        "When will we meet?",
        "11:20"
      }
    };

    long currentTime = System.currentTimeMillis();
    long timeOffset = 1000 * 60 * 60; // 1 hour in milliseconds, to make dummy messages seem older

    for (Object[] userData : dummyUserData) {
      String displayName = (String) userData[0];
      String email = (String) userData[1];
      String password = (String) userData[2];
      String firstName = (String) userData[3];
      String lastName = (String) userData[4];
      String gender = (String) userData[5];
      String birthday = (String) userData[6];
      String initialMessageText = (String) userData[7];
      String initialMessageTimeDisplay = (String) userData[8]; // For display in MessageItem

      long dummyUserId =
          dbHelper.findOrInsertUser(email, password, firstName, lastName, gender, birthday);

      if (dummyUserId != -1 && currentUserId != -1) {
        // Check if we should add the initial default message
        if (!dbHelper.hasMessagesBetweenUsers(dummyUserId, currentUserId)) {
          // Insert the initial message from the dummy user TO the current user
          // You can adjust the timestamp to make it seem like it was sent earlier
          long messageTimestampMs =
              currentTime - (timeOffset * (messageList.size() + 1)); // Stagger timestamps

          dbHelper.addChatMessage(
              dummyUserId, currentUserId, initialMessageText, messageTimestampMs);
          Log.d(
              "MessagesActivity", "Added initial message for " + displayName + " to current user.");
        }
        // The MessageItem will still show the hardcoded last message and time for the preview
        // If you want it to dynamically show the REAL last message, this part would need to query
        // the DB.
        // For now, we're just ensuring an initial message exists.
        messageList.add(
            new MessageItem(
                dummyUserId,
                displayName,
                initialMessageText, // This is the preview text
                initialMessageTimeDisplay, // This is the preview time
                R.drawable.roomie_logo));
      } else {
        Log.e(
            "MessagesActivity",
            "Failed to find or insert user: " + displayName + " or currentUserId is invalid.");
      }
    }
  }

  @Override
  public void onMessageItemClick(MessageItem clickedMessageItem, int position) {
    Log.d(
        "MessagesActivity",
        "Clicked on: "
            + clickedMessageItem.getUserName()
            + " with ID: "
            + clickedMessageItem.getUserId());

    Toast.makeText(this, "Clicked on: " + clickedMessageItem.getUserName(), Toast.LENGTH_SHORT)
        .show();

    Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
    // Pass the actual numeric user ID of the chat partner
    intent.putExtra("CHAT_PARTNER_ID", clickedMessageItem.getUserId());
    // Pass the name for display purposes
    intent.putExtra("CHAT_PARTNER_NAME", clickedMessageItem.getUserName());
    intent.putExtra("PROFILE_IMAGE_RES", clickedMessageItem.getProfileImageRes());

    startActivity(intent);
  }
}
