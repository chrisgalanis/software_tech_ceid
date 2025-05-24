package com.example.roomie;

public class MessageItem {
<<<<<<< HEAD
    private String name;
    private String lastMessage;
    private String time;
    private int profileImageResource;

    public MessageItem(String name, String lastMessage, String time, int profileImageResource) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.profileImageResource = profileImageResource;
    }

    // Add your getter methods here (getName(), getLastMessage(), getTime(), getProfileImageResource())
    public String getName() {
        return name;
=======
    private String userName;
    private String lastMessage;
    private String timestamp;
    private int profileImageRes;
    // private String userId; // Optional: for a unique ID

    // Constructor
    public MessageItem(String userName, String lastMessage, String timestamp, int profileImageRes) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.profileImageRes = profileImageRes;
    }

    // --- Public Getter Methods ---
    public String getUserName() {
        return userName;
>>>>>>> 3f39587 (messages-page done)
    }

    public String getLastMessage() {
        return lastMessage;
    }

<<<<<<< HEAD
    public String getTime() {
        return time;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }
=======
    public String getTimestamp() {
        return timestamp;
    }

    public int getProfileImageRes() {
        return profileImageRes;
    }

    // public String getUserId() { return userId; } // Uncomment if you have and need this
>>>>>>> 3f39587 (messages-page done)
}
