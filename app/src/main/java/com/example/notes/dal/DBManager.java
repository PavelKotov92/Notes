package com.example.notes.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {

    public final static String DB_NAME = "Notes.db";
    public final static int DB_VER = 1;

    public DBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Notes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject TEXT, " +
                "content TEXT, " +
                "date TEXT" +
                ")");
    }

    private void upgrade2Ver(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE Reminders(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT, " +
                "dateAt TEXT," +
                "noteID INTEGER" +
                ")");
    }

    private void upgrade3Ver(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE Periods(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");

        db.execSQL("INSERT INTO Periods('name') VALUES('Once')");
        db.execSQL("INSERT INTO Periods('name') VALUES('Daily')");
        db.execSQL("INSERT INTO Periods('name') VALUES('Weekly')");
        db.execSQL("INSERT INTO Periods('name') VALUES('Monthly')");

        db.execSQL("ALTER TABLE Reminders ADD PeriodID INTEGER NOT NULL Default 1");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion == 1 && newVersion == 2)
        {
            upgrade2Ver(db);
        }
        else if(oldVersion == 1 && newVersion == 3)
        {
            upgrade2Ver(db);
            upgrade3Ver(db);
        }
        else if(oldVersion == 2 && newVersion == 3)
        {
            upgrade3Ver(db);
        }
        else if (oldVersion < 1 && newVersion == 3)
        {
            upgrade2Ver(db);
            upgrade3Ver(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
        if(oldVersion == 2 && newVersion == 1)
        {
            db.execSQL("DROP TABLE Reminders");
        }
    }
}