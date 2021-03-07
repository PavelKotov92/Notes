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
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notes.dal.DAL_Reminders;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private int noteID;
    private int periodID;
    private int reminderID;

    private TextView reminderNoteID;
    private EditText txtReminderContent;

    private Button btnReminderCancel;
    private Button btnReminderSave;
    private Button btnReminderDelete;

    private GridView gvPeriods;
    private TimePicker timeReminder;

    private Cursor cursor;

    private DAL_Reminders dataLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initViews();
        setOnClickHandlers();
    }

    private void setOnClickHandlers() {

        gvPeriods.setOnItemClickListener((parent, view, position, id) -> {

            //Сбросим подсветку у всех элементов грида
            for (int i = 0; i < gvPeriods.getChildCount(); i++) {
                gvPeriods.getChildAt(i).setBackgroundColor(parent.getResources().getColor(R.color.white));
            }
            //Если мы смогли переместиться на эту запись
            if (cursor.moveToPosition(position)) {
                periodID = cursor.getInt(0);
                Log.d("notesApp", " " + periodID);
            }

            view.setBackgroundColor(parent.getResources().getColor(R.color.purple_200));
        });
    }

    private void initDB() {

        dataLayer = new DAL_Reminders(this);
    }

    private void initViews() {

        reminderNoteID = findViewById(R.id.reminderNoteID);
        txtReminderContent = findViewById(R.id.txtReminderContent);

        timeReminder = findViewById(R.id.timeReminder);
        timeReminder.setIs24HourView(true);
        gvPeriods = findViewById(R.id.gvPeriods);

        btnReminderCancel = findViewById(R.id.btnReminderCancel);
        btnReminderSave = findViewById(R.id.btnReminderSave);
        btnReminderDelete = findViewById(R.id.btnReminderDelete);

        btnReminderCancel.setOnClickListener(this);
        btnReminderSave.setOnClickListener(this);
        btnReminderDelete.setOnClickListener(this);

    }


    @Override
    protected void onResume() {

        super.onResume();

        Intent myIntent = getIntent();

        noteID = myIntent.getIntExtra("noteID", 0);

        if (noteID == 0) {
            Toast.makeText(this, "Unable to set reminder for unsaved note!", Toast.LENGTH_LONG).show();
            finish();
        }
        //По - умолчанию напоминание 1 раз (periodID = 1 )

        periodID = 1;

        String reminderIDText = reminderNoteID.getText().toString();

        reminderNoteID.setText(reminderIDText + " " + noteID);

        initDB();
        readRemindersFromDB();

    }

    private void readRemindersFromDB() {


        //Маппинг (сопоставление) колонок таблицы и контролов (элементы управления (вьюшки))
        //Скажем какие колонки брать
        String[] columns = {
                "_id",
                "name"
        };

        //Массив айдишек в которые колонки из таблицы БД класть(по порядку)
        int[] views = {
                R.id.txtPeriodId,
                R.id.txtPeriodName

        };

        //читаем данные и создаем курсор на результаты чтения
        //при помощи курсора можно прочитать все извлеченные курсором данные
        cursor =  dataLayer.PeriodsAll();

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.period_item,
                cursor,
                columns,
                views,
                0
        );

        gvPeriods.setAdapter(cursorAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnReminderCancel: {
                finish();
                break;
            }
            case R.id.btnReminderSave: {
                saveReminder();
                break;
            }
            case R.id.btnReminderDelete: {
                deleteReminder();
                break;
            }
        }
    }

    private void saveReminder() {

        ContentValues recordValues = prepareReminderValues();
        //Сохранение изменений существующей заметки
        long newReminderID =  dataLayer.ReminderAdd(recordValues);

        Log.d("notesApp", "Напоминание с номером " + String.valueOf(newReminderID) + " добавлена!");
        Toast.makeText(this,
                "Заметка с номером " + String.valueOf(newReminderID) + " добавлена!",
                Toast.LENGTH_LONG).show();

        finish();

    }

    private void deleteReminder() {

        dataLayer.ReminderDeleteByNoteId(noteID);

        Toast.makeText(this, "Напоминание удалено", Toast.LENGTH_LONG).show();

        finish();
    }

    private ContentValues prepareReminderValues() {

        ContentValues recordValues = new ContentValues();

        recordValues.put("content", txtReminderContent.getText().toString());

        int currentHour = timeReminder.getCurrentHour();
        int currentMinutes = timeReminder.getCurrentMinute();

        String reminderTime = currentHour + ":" + currentMinutes;

        recordValues.put("content", txtReminderContent.getText().toString());
        recordValues.put("dateAt", reminderTime);
        recordValues.put("noteID", noteID);
        recordValues.put("periodID", periodID);

        return recordValues;

    }


}