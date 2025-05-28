package com.example.roomie;

public class ChatMessage {
  private String senderId;
  private String receiverId;
  private String messageText;
  private String timestamp; // e.g., "HH:mm"
  private boolean isSentByCurrentUser;
  private String senderAvatarUrl; // <<< Should be String

  public ChatMessage(
      String senderId,
      String receiverId,
      String messageText,
      String timestamp,
      boolean isSentByCurrentUser) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.messageText = messageText;
    this.timestamp = timestamp;
    this.isSentByCurrentUser = isSentByCurrentUser;
    this.senderAvatarUrl = null;
  }

  public ChatMessage(
      String senderId,
      String receiverId,
      String messageText,
      String timestamp,
      boolean isSentByCurrentUser,
      String senderAvatarUrl) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.messageText = messageText;
    this.timestamp = timestamp;
    this.isSentByCurrentUser = isSentByCurrentUser;
    this.senderAvatarUrl = senderAvatarUrl; // <<< String type
  }

  // Getters
  public String getSenderId() {
    return senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public String getMessageText() {
    return messageText;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public boolean isSentByCurrentUser() {
    return isSentByCurrentUser;
  }

  public String getSenderAvatarUrl() {
    return senderAvatarUrl;
  } // <<< Returns String
}
