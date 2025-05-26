package com.example.roomie;

public class MessageItem {
  private long userId; // <<< ADD THIS: The actual ID from the users table
  private String displayName; // Name to show in the list
  private String lastMessage;
  private String timestamp;
  private int profileImageRes;

  // Updated constructor
  public MessageItem(
      long userId, String displayName, String lastMessage, String timestamp, int profileImageRes) {
    this.userId = userId;
    this.displayName = displayName;
    this.lastMessage = lastMessage;
    this.timestamp = timestamp;
    this.profileImageRes = profileImageRes;
  }

  // Getters
  public long getUserId() { // <<< ADD THIS
    return userId;
  }

  public String getUserName() { // <<< RENAME from getUserName for clarity
    return displayName;
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
}
