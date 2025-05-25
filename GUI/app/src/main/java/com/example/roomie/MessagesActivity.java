package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

  private RecyclerView recyclerViewMessages;
  private MessageAdapter messageAdapter;
  private List<MessageItem> messageList;
  private long currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_messages);

    // 1) grab the stored user ID
    currentUserId = SessionManager.get().getUserId();
    if (currentUserId < 0) {
      Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    // 2) init RecyclerView
    recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

    // 3) load your (real) data source
    messageList = new ArrayList<>();
    messageList.add(
        new MessageItem("Chris G.", "2 new messages!!", "13:40", R.drawable.roomie_logo));
    messageList.add(new MessageItem("Titos H.", "New message!!", "19:43", R.drawable.roomie_logo));
    messageList.add(
        new MessageItem("Kostas M.", "I am hungry, let's go!!!!", "10:13", R.drawable.roomie_logo));
    messageList.add(new MessageItem("Mike M.", "But why?", "04:20", R.drawable.roomie_logo));
    messageList.add(
        new MessageItem("Rafas P.", "When will we meet?", "11:20", R.drawable.roomie_logo));

    messageAdapter = new MessageAdapter(messageList);
    recyclerViewMessages.setAdapter(messageAdapter);

    // 4) wire up bottom nav (it will also highlight "Messages")
    BottomNavigationHelper.setup(
        (BottomNavigationView) findViewById(R.id.bottom_navigation), this, R.id.nav_messages);
  }
}
