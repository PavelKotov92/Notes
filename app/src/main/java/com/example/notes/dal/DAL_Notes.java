package com.example.notes.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAL_Notes extends DAL {

    public DAL_Notes(Context cnt)
    {
        super(cnt);
    }

    public Cursor NotesAll() {

        return db.rawQuery("SELECT * FROM Notes ORDER BY _id DESC", null);

    }

    public Cursor NoteById(int noteID) {

        return db.rawQuery("SELECT * FROM Notes WHERE _id = " + noteID, null);

    }

    public int NoteDeleteById(int noteID) {

        return db.delete("notes","_id= " + noteID,null);

    }

    public long NoteAdd(ContentValues recordValues)
    {
        return db.insert(
                "Notes",
                null,
                recordValues
        );
    }

    public int NoteUpdateById(int noteID, ContentValues recordValues)
    {
        return db.update("notes",
                recordValues,
                "_id= " + noteID,
                null
        );
    }

}
