package com.example.smsgateway.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.smsgateway.Model.User;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "users_db";


    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(User.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long addUser(String fristname, String lastname,String phone_number) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(User.COLUMN_FRIST_NAME, fristname);
        values.put(User.COLUMN_LAST_NAME, lastname);
        values.put(User.COLUMN_PHONE_NUMBER, phone_number);

        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }


    public User getUser(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_FRIST_NAME,User.COLUMN_LAST_NAME,User.COLUMN_PHONE_NUMBER, User.COLUMN_TIMESTAMP},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        User note = new User(
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_FRIST_NAME)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }



    public Cursor getAllUsers() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from usertable", null );

        return res;
    }


    public ArrayList<String> getPhoneNumbers() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from usertable", null);
        if (result.getCount() == 0) {
            System.out.println(result.getCount());
            return null;
        } else {
            while (result.moveToNext()) {
                array_list.add(result.getString(3));
            }
            return array_list;
        }
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_FRIST_NAME, user.getFristname());
        values.put(User.COLUMN_LAST_NAME, user.getLastname());
        values.put(User.COLUMN_PHONE_NUMBER, user.getPhone_number());
        // updating row
        return db.update(User.TABLE_NAME, values, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
}
