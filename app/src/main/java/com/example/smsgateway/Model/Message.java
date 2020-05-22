package com.example.smsgateway.Model;

public class Message {
    public static final String TABLE_NAME = "message";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_Title = "title";
    public static final String COLUMN_Content = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String title;
    private String content;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Title + " TEXT,"
                    + COLUMN_Content + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Message() {
    }

    public Message(int id, String title, String content, String timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
