package com.example.roomie;

public class MessageItem {
<<<<<<< HEAD
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
=======
  private String userName;
  private String lastMessage;
  private String timestamp;
  private int profileImageRes;
>>>>>>> 313cd41 (fix during rebase)

  // private String userId; // Optional: for a unique ID

<<<<<<< HEAD
    // --- Public Getter Methods ---
    public String getUserName() {
        return userName;
>>>>>>> 3f39587 (messages-page done)
    }
=======
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
>>>>>>> 313cd41 (fix during rebase)

    public String getLastMessage() {
        return lastMessage;
    }

<<<<<<< HEAD
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
=======
  public String getTimestamp() {
    return timestamp;
  }
>>>>>>> 313cd41 (fix during rebase)

  public int getProfileImageRes() {
    return profileImageRes;
  }

<<<<<<< HEAD
    // public String getUserId() { return userId; } // Uncomment if you have and need this
>>>>>>> 3f39587 (messages-page done)
=======
  // public String getUserId() { return userId; } // Uncomment if you have and need this
>>>>>>> 313cd41 (fix during rebase)
}
