package com.example.roomie;

public class MessageItem {
  private String userName;
  private String lastMessage;
  private String timestamp;
  private int profileImageRes;
  private String userId; // Optional: for a unique ID

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
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public int getProfileImageRes() {
    return profileImageRes;
  }

  public String getUserId() {
    return userId;
  } // Uncomment if you have and need this
}
