package com.example.roomie;

import android.content.Intent; // <<< --- 1. IMPORT INTENT ---
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

// --- 2. Implement the CORRECTLY QUALIFIED interface ---
public class MessagesActivity extends AppCompatActivity
    implements MessageAdapter.OnMessageItemClickListener {

  private RecyclerView recyclerViewMessages;
  private MessageAdapter messageAdapter; // Use the custom MessageAdapter
  private List<MessageItem> messageList;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages); // Ensure this layout exists

    currentUserId = SessionManager.get().getUserId(); // Assuming SessionManager is correctly set up
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    recyclerViewMessages =
        findViewById(R.id.recyclerViewMessages); // Ensure this ID exists in activity_messages.xml
    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

    messageList = new ArrayList<>();
    // Populate messageList (your existing code)
    messageList.add(
        new MessageItem("Chris G.", "2 new messages!!", "13:40", R.drawable.roomie_logo));
    messageList.add(new MessageItem("Titos H.", "New message!!", "19:43", R.drawable.roomie_logo));
    messageList.add(
        new MessageItem("Kostas M.", "I am hungry, let's go!!!!", "10:13", R.drawable.roomie_logo));
    messageList.add(new MessageItem("Mike M.", "But why?", "04:20", R.drawable.roomie_logo));
    messageList.add(
        new MessageItem("Rafas P.", "When will we meet?", "11:20", R.drawable.roomie_logo));

    // --- 3. Pass 'this' as the listener to the MessageAdapter constructor ---
    messageAdapter = new MessageAdapter(messageList, this);
    recyclerViewMessages.setAdapter(messageAdapter);

    BottomNavigationHelper.setup( // Assuming BottomNavigationHelper is correctly set up
        (BottomNavigationView) findViewById(R.id.bottom_navigation), // Ensure this ID exists
        this,
        R.id.nav_messages // Ensure this menu ID exists
        );
  }

  // --- 4. Override the interface method ---
  @Override
  public void onMessageItemClick(MessageItem clickedMessageItem, int position) {
    Toast.makeText(this, "Clicked on: " + clickedMessageItem.getUserName(), Toast.LENGTH_SHORT)
        .show();

    Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
    intent.putExtra("USER_NAME", clickedMessageItem.getUserName());
    intent.putExtra("PROFILE_IMAGE_RES", clickedMessageItem.getProfileImageRes());
    // Add other relevant data like a unique user/chat ID
    // intent.putExtra("USER_ID", clickedMessageItem.getUserId()); // If you added getUserId() to
    // MessageItem

    startActivity(intent);
  }
}
