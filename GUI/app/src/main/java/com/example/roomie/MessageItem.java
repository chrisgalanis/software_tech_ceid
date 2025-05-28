package com.example.roomie;

public class MessageItem {
  private long userId;
  private String displayName;
  private String lastMessage;
  private String timestamp;
  private String profileAvatarUrl; // <<< CHANGED from int profileImageRes to String

  // Updated constructor
  public MessageItem(
      long userId,
      String displayName,
      String lastMessage,
      String timestamp,
      String profileAvatarUrl) { // <<< CHANGED type
    this.userId = userId;
    this.displayName = displayName;
    this.lastMessage = lastMessage;
    this.timestamp = timestamp;
    this.profileAvatarUrl = profileAvatarUrl;
  }

  // Getters
  public long getUserId() {
    return userId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getProfileAvatarUrl() {
    return profileAvatarUrl;
  } // <<< CHANGED getter
}
