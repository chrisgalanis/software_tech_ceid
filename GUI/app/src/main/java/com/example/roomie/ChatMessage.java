package com.example.roomie;

public class ChatMessage {

  public long messageId;
  public long senderId;
  public long receiverId;
  public String messageText;
  public String timestamp; // e.g., "HH:mm"
  public boolean isSentByCurrentUser;
  public String senderAvatarUrl;

  public ChatMessage(long messageId,
                     long senderId,
                     long receiverId,
                     String messageText,
                     String timestamp,
                     boolean isSentByCurrentUser,
                     String senderAvatarUrl) {
    this.messageId = messageId;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.messageText = messageText;
    this.timestamp = timestamp;
    this.isSentByCurrentUser = isSentByCurrentUser;
    this.senderAvatarUrl = senderAvatarUrl;
  }

  public ChatMessage(long senderId,
                     long receiverId,
                     String messageText,
                     String timestamp,
                     boolean isSentByCurrentUser) {
    this(0, senderId, receiverId, messageText, timestamp, isSentByCurrentUser, null);
  }
}