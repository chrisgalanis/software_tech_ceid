package com.example.roomie;

import com.example.roomie.MessageItem;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<MessageItem> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your data source (replace with your actual data fetching logic)
        messageList = new ArrayList<>();
        messageList.add(new MessageItem("Chris G.", "2 new messages!!", "13:40", R.drawable.roomie_logo)); // Replace with actual image resource
        messageList.add(new MessageItem("Titos H.", "New message!!", "19:43", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Kostas M.", "I am hungry, let's go!!!!", "10:13", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Mike M.", "But why?", "4:20", R.drawable.roomie_logo));
        messageList.add(new MessageItem("Rafas P.", "When will we meet?", "11:20", R.drawable.roomie_logo));

        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setAdapter(messageAdapter);
    }
}
