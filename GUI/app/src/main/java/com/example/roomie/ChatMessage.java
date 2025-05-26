package com.example.roomie; // Replace with your package name

public class ChatMessage {
  private String senderId;
  private String receiverId; // Optional, useful for group chats or more complex logic
  private String messageText;
  private String timestamp;
  private boolean isSentByCurrentUser;
  private int senderProfileImageRes; // Resource ID for profile image (for received messages)

  // Constructor for messages sent by current user
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
    this.senderProfileImageRes = 0; // Not needed if sent by current user, or use a default
  }

  // Constructor for messages received from others
  public ChatMessage(
      String senderId,
      String receiverId,
      String messageText,
      String timestamp,
      boolean isSentByCurrentUser,
      int senderProfileImageRes) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.messageText = messageText;
    this.timestamp = timestamp;
    this.isSentByCurrentUser = isSentByCurrentUser;
    this.senderProfileImageRes = senderProfileImageRes;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

  public String getMessageText() {
    return messageText;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isSentByCurrentUser() {
    return isSentByCurrentUser;
  }

  public void setSentByCurrentUser(boolean sentByCurrentUser) {
    isSentByCurrentUser = sentByCurrentUser;
  }

  public int getSenderProfileImageRes() {
    return senderProfileImageRes;
  }

  public void setSenderProfileImageRes(int senderProfileImageRes) {
    this.senderProfileImageRes = senderProfileImageRes;
  }
}
