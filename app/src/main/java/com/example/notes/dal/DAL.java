package com.example.notes.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAL {

    //Для наследования в DAL_Reminders
    protected DBManager dbManager;
    protected SQLiteDatabase db;

    public DAL(Context cnt)
    {
        dbManager = new DBManager(cnt,
                DBManager.DB_NAME,
                null,
                DBManager.DB_VER
        );

        db = dbManager.getWritableDatabase();
    }

}