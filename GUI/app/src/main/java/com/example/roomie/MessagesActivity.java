package com.example.roomie;

import android.os.Bundle;
import android.widget.Toast;
<<<<<<< HEAD

=======
import android.util.Log;
import android.content.Intent; // Make sure this is imported
>>>>>>> 3f39587 (messages-page done)
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity implements MessageAdapter.OnMessageItemClickListener {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<MessageItem> messageList;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_messages);

        // 1) grab the stored user ID
        currentUserId = SessionManager.get().getUserId();
=======
        setContentView(R.layout.activity_messages); // Sets up the layout for MessagesActivity

        Log.d("MessagesActivity", "onCreate: MessagesActivity started."); // Log for this activity

        // 1) grab the stored user ID
        currentUserId = SessionManager.get().getUserId(); // Assuming SessionManager is correct
>>>>>>> 3f39587 (messages-page done)
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
<<<<<<< HEAD
        messageList.add(new MessageItem("Chris G.",  "2 new messages!!",          "13:40", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Titos H.", "New message!!",             "19:43", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Kostas M.","I am hungry, let's go!!!!","10:13", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Mike M.",  "But why?",                  "04:20", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Rafas P.", "When will we meet?",        "11:20", R.drawable.roomie_logo));

        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setAdapter(messageAdapter);

        // 4) wire up bottom nav (it will also highlight "Messages")
        BottomNavigationHelper.setup(
=======
        messageList.add(new MessageItem("Chris G.", "2 new messages!!", "13:40", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Titos H.", "New message!!", "19:43", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Kostas M.","I am hungry, let's go!!!!","10:13", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Mike M.", "But why?", "04:20", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Rafas P.", "When will we meet?", "11:20", R.drawable.roomie_logo));

        messageAdapter = new MessageAdapter(messageList, this);
        recyclerViewMessages.setAdapter(messageAdapter);

        // 4) wire up bottom nav (it will also highlight "Messages")
        BottomNavigationHelper.setup( // Assuming BottomNavigationHelper is correct
>>>>>>> 3f39587 (messages-page done)
                (BottomNavigationView) findViewById(R.id.bottom_navigation),
                this,
                R.id.nav_messages
        );
    }
<<<<<<< HEAD
=======

    @Override
    public void onMessageItemClick(MessageItem clickedMessageItem, int position) {
        Log.d("MessagesActivity", "onMessageItemClick: Clicked on item at position " + position + " with name: " + clickedMessageItem.getUserName());

        Toast.makeText(this, "Clicked on: " + clickedMessageItem.getUserName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
        intent.putExtra("USER_NAME", clickedMessageItem.getUserName());
        intent.putExtra("PROFILE_IMAGE_RES", clickedMessageItem.getProfileImageRes());
        // You might want to pass a unique ID too:
        // intent.putExtra("USER_ID", clickedMessageItem.getSomeUniqueId());

        Log.d("MessagesActivity", "onMessageItemClick: Starting ChatActivity with USER_NAME=" + clickedMessageItem.getUserName());
        startActivity(intent);
    }
>>>>>>> 3f39587 (messages-page done)
}
