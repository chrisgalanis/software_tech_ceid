package com.example.roomie;

public class Warning {
    public long   id;
    public long   userId;
    public String text;
    public String status;   // "PENDING" or "ACKNOWLEDGED"
    public String timestamp;

    public Warning(long id, long userId, String text, String status, String timestamp) {
        this.id        = id;
        this.userId    = userId;
        this.text      = text;
        this.status    = status;
        this.timestamp = timestamp;
    }
}
