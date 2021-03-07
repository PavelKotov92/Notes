package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.example.notes.dal.DAL_Notes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Cursor cursor;

    private Button btnAddNote;
    private GridView gvNotes;

    private DAL_Notes dataLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }


    @Override
    protected void onResume() {

        super.onResume();

        initDB();
        readNotesFromDB();

    }

    private void readNotesFromDB() {
        //Маппинг (сопоставление) колонок таблицы и контролов (элементы управления (вьюшки))
        //Скажем какие колонки брать
        String[] columns = {
                "subject",
                "date"
        };

        //Массив айдишек в которые колонки из таблицы БД класть(по порядку)
        int[] views = {
                R.id.txtNoteSubject,
                R.id.txtNoteDate

        };

        //читаем данные и создаем курсор на результаты чтения
        //при помощи курсора можно прочитать все извлеченные курсором данные

        cursor = dataLayer.NotesAll();

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.note_item,
                cursor,
                columns,
                views,
                0
        );

        gvNotes.setAdapter(cursorAdapter);
    }

    private void initViews() {

        btnAddNote = findViewById(R.id.btnAddNote);
        gvNotes = findViewById(R.id.gvNotes);

        btnAddNote.setOnClickListener(this);

        gvNotes.setOnItemClickListener((parent, view, position,id) ->{

            Log.d("notes","Id from gridView" + id);

            //Если мы смогли переместиться на эту запись
            if (cursor.moveToPosition(position))
            {
                int noteID = cursor.getInt(0);

                Intent noteEditorIntent = new Intent(getBaseContext(),NoteEditorActivity.class);
                noteEditorIntent.putExtra("noteID",noteID);

                startActivity(noteEditorIntent);

                String noteSubject = cursor.getString(1);

                Log.d("notes", "noteID: "  + noteID);
                Log.d("notes", "Subject: " + noteSubject);
            }

        });

    }

    private void initDB() {

        dataLayer = new DAL_Notes(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnAddNote:
                openNoteEditor();
                break;
        }
    }

    private void openNoteEditor() {

        Intent editorIntent = new Intent(this,NoteEditorActivity.class);

        startActivity(editorIntent);
    }
}