package com.example.roomie;

public class MessageItem {
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

  // Add your getter methods here (getName(), getLastMessage(), getTime(),
  // getProfileImageResource())
  public String getName() {
    return name;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public String getTime() {
    return time;
  }

  public int getProfileImageResource() {
    return profileImageResource;
  }
}
