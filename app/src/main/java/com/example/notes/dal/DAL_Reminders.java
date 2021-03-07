package com.example.notes.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAL_Reminders extends DAL {

    public DAL_Reminders(Context cnt)
    {
        super(cnt);
    }

    public Cursor PeriodsAll() {

        return db.rawQuery("SELECT * FROM Periods ORDER BY _id ASC", null);

    }

    public long ReminderAdd(ContentValues recordValues)
    {
        return db.insert(
                "Reminders",
                null,
                recordValues
        );
    }

    public int ReminderDeleteByNoteId(int noteID) {

        return db.delete("reminders", "_id= " + noteID, null);

    }

}
