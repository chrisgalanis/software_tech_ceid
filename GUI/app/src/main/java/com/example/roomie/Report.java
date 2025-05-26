package com.example.roomie;

public class Report {
    public long   id;
    public long   reportedUserId;
    public String reportedUserName;
    public String avatarUrl;
    public String text;

    public Report(long id, long userId, String name, String avatarUrl, String text) {
        this.id           = id;
        this.reportedUserId = userId;
        this.reportedUserName = name;
        this.avatarUrl    = avatarUrl;
        this.text         = text;
    }
}
