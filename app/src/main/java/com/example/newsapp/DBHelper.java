package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "newsdatabase";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME= "news";
    public static final String ID = "_id";
    public static final String TITLE= "title";
    public static final String LINK= "link";


    public DBHelper(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME +"" +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" VARCHAR(28) ,"+LINK+" VARCHAR(28));";
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("db", "EXCEPTION in Create!!!");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        try
        {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("db", "EXCEPTION in Drop!!!");
        }
    }

    public long insertData(String email, String pass)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, email);
        contentValues.put(LINK, pass);
        return db.insert(TABLE_NAME, null, contentValues);

    }

    public  Cursor getCursorForData()
    {
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        //points to first row of the table
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public String getData()
    {
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        //points to first row of the table
        Cursor cursor = db.rawQuery(query,null);
        //iterate through each row (loop)
        //Buffer that appends everything in the string
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            @SuppressLint("Range") int id =  cursor.getInt(cursor.getColumnIndex(ID));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(TITLE));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(LINK));
            buffer.append(id +" "+email +" "+password);
        }
        return buffer.toString();
    }

    public  int deleteUser(String email)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {email};
        //if email in equal to arg passed through input delete
        int count = db.delete(TABLE_NAME, TITLE + " =?", args);
        //if count is +ive email is deleted, -ve not deleted
        return count;
    }

    public  int updateEmail(String oldEmail, String newEmail)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, newEmail);
        String[] args = {oldEmail};
        //if email in equal to arg passed through input delete
        int count = db.update(TABLE_NAME, contentValues, TITLE + " =?", args);
        //if count is +ive email is deleted, -ve not deleted
        return count;
    }

}
