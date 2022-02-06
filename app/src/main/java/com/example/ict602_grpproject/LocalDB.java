package com.example.ict602_grpproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "loggedUser.db";
    private static final int DATABASE_VERSION = 1;

    public LocalDB(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }
    //to store logged-in user locally
    @Override
    public void onCreate(SQLiteDatabase localDB) {
        localDB.execSQL("create table login(id integer primary key autoincrement, userid text null, username text null, usertype text null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}