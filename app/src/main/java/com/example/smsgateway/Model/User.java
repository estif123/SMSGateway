package com.example.smsgateway.Model;

public class User {

    public static final String TABLE_NAME = "usertable";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FRIST_NAME = "fristname";
    public static final String COLUMN_LAST_NAME = "lastname";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_TIMESTAMP= "timestamp";

    private int id;
    private String fristname;
    private String lastname;
    private String phone_number;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FRIST_NAME + " TEXT,"+ COLUMN_LAST_NAME+ " TEXT,"
                    + COLUMN_PHONE_NUMBER + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public User() {
    }

    public User(int id, String fristname, String lastname,String phone_number, String timestamp) {
        this.id = id;
        this.fristname = fristname;
        this.lastname = lastname;
        this.phone_number = phone_number;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getFristname() {
        return fristname;
    }
    public void setFristname(String fristname) {
        this.fristname = fristname;
    }
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname= lastname;
    }


    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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
