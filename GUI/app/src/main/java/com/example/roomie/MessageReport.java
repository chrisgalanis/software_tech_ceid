package com.example.roomie;

public class MessageReport {
    public long   id;
    public ChatMessage message;

    public String text;        // report text
    public String status;      // e.g. "PENDING"

    public MessageReport(long id, ChatMessage message, String text, String status) {
        this.id      = id;
        this.message = message;
        this.text    = text;
        this.status  = status;
    }
}
