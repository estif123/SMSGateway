package com.example.smsgateway.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.smsgateway.Model.Message;

import java.util.ArrayList;

public class MessageDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "messages_db";


    public MessageDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Message.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long addMessage(String title, String content) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        if (title != null && content
                != null) {
            values.put(Message.COLUMN_Title, title);
            values.put(Message.COLUMN_Content, content);
            long id = db.insert(Message.TABLE_NAME, null, values);
            db.close();
            return id;
        } else {
            System.out.println("unable to insert NUll into database");
            return  0;
        }


    }
    // return newly inserted row id

    public int getMessageCount() {
        String countQuery = "SELECT  * FROM " + Message.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public Message getMessage(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Message.TABLE_NAME,
                new String[]{Message.COLUMN_ID,
                        Message.COLUMN_Title,
                        Message.COLUMN_Content,
                        Message.COLUMN_TIMESTAMP},
                Message.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare message object
        Message message = new Message(
                cursor.getInt(cursor.getColumnIndex(Message.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Message.COLUMN_Title)),
                cursor.getString(cursor.getColumnIndex(Message.COLUMN_Content)),
                cursor.getString(cursor.getColumnIndex(Message.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return message;
    }

    public Cursor getAllMessages() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from message", null );

        return res;
    }

    public int updateMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Message.COLUMN_Title, message.getTitle());
        values.put(Message.COLUMN_Content, message.getContent());

        // updating row
        return db.update(Message.TABLE_NAME, values, Message.COLUMN_ID + " = ?",
                new String[]{String.valueOf(message.getId())});
    }

    public void deleteMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Message.TABLE_NAME, Message.COLUMN_ID + " = ?",
                new String[]{String.valueOf(message.getId())});
        db.close();
    }

}
