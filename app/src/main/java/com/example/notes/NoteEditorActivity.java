package com.example.notes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.dal.DAL_Notes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNoteSubject;
    private EditText txtNoteContent;

    private Button btnCancel;
    private Button btnSaveNote;
    private Button btnReminder;
    private Button btnDeleteNote;

    private Cursor cursor;
    private DAL_Notes dataLayer;

    private int noteID;
    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        initViews();
    }

    @Override
    protected void onResume() {

        super.onResume();

        initDB();

        Intent myIntent = getIntent();
        noteID = myIntent.getIntExtra("noteID",0);

        if (noteID > 0)
        {
            readNote();
        }
    }

    private void readNote() {

        cursor =  dataLayer.NoteById(noteID);

        if (cursor.moveToFirst())
        {
            txtNoteSubject.setText(cursor.getString(1));
            txtNoteContent.setText(cursor.getString(2));

            isEditMode = true;
        }
    }

    private void initDB() {

        dataLayer = new DAL_Notes(this);
    }

    private void initViews() {

        txtNoteContent = findViewById(R.id.txtNoteContent);
        txtNoteSubject = findViewById(R.id.txtNoteSubject);

        btnCancel = findViewById(R.id.btnCancel);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnReminder = findViewById(R.id.btnReminder);

        btnCancel.setOnClickListener(this);
        btnSaveNote.setOnClickListener(this);
        btnDeleteNote.setOnClickListener(this);
        btnReminder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCancel:
                cancelEdit();
                break;

            case R.id.btnSaveNote:
                saveNote();
                break;

            case R.id.btnDeleteNote:
                deleteNote();
                break;

            case R.id.btnReminder:
                openAddReminder();
                break;
        }

    }

    private void openAddReminder() {

        if (isEditMode) {
            Intent reminderIntent = new Intent(this, ReminderActivity.class);
            reminderIntent.putExtra("noteID", noteID);
            startActivity(reminderIntent);

        }
        else
        {
            Toast.makeText(this, "Unable to set reminder for unsaved note!",Toast.LENGTH_LONG).show();
        }
    }

    private void deleteNote() {

        if (isEditMode)
        {
            dataLayer.NoteDeleteById(noteID);

            Toast.makeText(this, "Заметка удалена",Toast.LENGTH_LONG).show();

            Log.d("notesApp","notes =" + noteID +  "Заметка удалена");
        }

        else
        {
            Toast.makeText(this, "Невозможно удалить не сохраненую заметку",Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void saveNote() {

        ContentValues recordValues = new ContentValues();

        recordValues.put("subject", txtNoteSubject.getText().toString());
        recordValues.put("content", txtNoteContent.getText().toString());

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String noteDateTime = currentDate + " " + currentTime;

        recordValues.put("date", noteDateTime);

        //Сохранение изменений существующей заметки
        if (isEditMode)
        {
            dataLayer.NoteUpdateById(noteID, recordValues);

            Toast.makeText(this,
                    "Заметка с номером " + String.valueOf(noteID) + " Сохранена",
                    Toast.LENGTH_LONG).show();
        }
        else {

            long newNoteID = dataLayer.NoteAdd(recordValues);

            Log.d("notes", "Заметка с номером " + String.valueOf(newNoteID) + " добавлена!");
            Toast.makeText(this,
                    "Заметка с номером " + String.valueOf(newNoteID) + " добавлена!",
                    Toast.LENGTH_LONG).show();
        }
        finish();

    }

    private void cancelEdit() {

        finish();

    }
}