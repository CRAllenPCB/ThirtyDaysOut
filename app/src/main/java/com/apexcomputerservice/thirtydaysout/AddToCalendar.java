package com.apexcomputerservice.thirtydaysout;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddToCalendar extends AppCompatActivity {

    EditText title, startTime, endTime, descrip, emails;
    TextView dateTV;
    CheckBox allDay;
    Button setAppt;
    Boolean allDayChecked;
    Calendar dateStartTime, dateEndTime;
    SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm aa");
    long dateInMillisecs;
    int passedYear,passedMonth,passedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_calendar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (EditText) findViewById(R.id.etTitle);
        startTime = (EditText) findViewById(R.id.etStartTime);
        endTime = (EditText) findViewById(R.id.etEndTime);
        descrip =(EditText)findViewById(R.id.etDescrip);
        emails = (EditText) findViewById(R.id.etEmails);
        dateTV = (TextView)findViewById(R.id.tvDate);
        allDay = (CheckBox) findViewById(R.id.cbAllDay);
        setAppt=(Button) findViewById(R.id.bSetAppt);


        AllDayTrue();

        allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (allDay.isChecked()) {
                    AllDayTrue();
                } else {
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    allDayChecked = false;
                }
            }
        });


        Bundle MainActivity = getIntent().getExtras();
        if (MainActivity == null) {
            return;
        }
        else {
            dateTV.setText(MainActivity.getString("endDate"));
            dateInMillisecs = (MainActivity.getLong("endInMillisecs"));
            passedYear = MainActivity.getInt("passYear");
            passedMonth = MainActivity.getInt("passMonth");
            passedDay = MainActivity.getInt("passDay");
        }

        setAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassToCalendar();
            }
        });




        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTimePicker(v);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEndTimePicker(v);
            }
        });
    }

    public void getStartTimePicker(View view){
        int hour = 8;
        int minute = 0;

        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateStartTime = Calendar.getInstance();
                dateStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateStartTime.set(Calendar.MINUTE, minute);
                dateStartTime.set(Calendar.SECOND, 0);
                startTime.setText(sdfTime.format(dateStartTime.getTime()));
                dateEndTime=(Calendar) dateStartTime.clone();
                dateEndTime.set(Calendar.HOUR_OF_DAY,dateStartTime.get(Calendar.HOUR_OF_DAY) + 1 );
                endTime.setText(sdfTime.format(dateEndTime.getTime()));
            }
        },hour, minute, false);
        mTimePicker.setTitle("Set Start Time");
        mTimePicker.show();
    }

    public void getEndTimePicker(View view){
        if (dateStartTime == null){
            dateStartTime = Calendar.getInstance();
            dateStartTime.set(Calendar.HOUR_OF_DAY, 8);
            dateStartTime.set(Calendar.MINUTE, 0);
            dateStartTime.set(Calendar.SECOND, 0);
            startTime.setText(sdfTime.format(dateStartTime.getTime()));
        }
        int hour = dateStartTime.get(Calendar.HOUR_OF_DAY) + 1;  //Add one hour after the start time
        int minute = 0;

        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateEndTime = Calendar.getInstance();
                dateEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateEndTime.set(Calendar.MINUTE, minute);
                dateEndTime.set(Calendar.SECOND, 0);
                endTime.setText(sdfTime.format(dateEndTime.getTime()));

            }
        },hour, minute, false);
        mTimePicker.setTitle("Set End Time");
        mTimePicker.show();
    }

    public void AllDayTrue(){
        startTime.setEnabled(false);
        endTime.setEnabled(false);
        allDayChecked = true;
    }

    public void PassToCalendar(){
        Calendar calBeginTime = Calendar.getInstance();
        Calendar calEndTime = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTimeInMillis(dateInMillisecs);
        if(dateStartTime==null){
            dateStartTime = Calendar.getInstance();
            dateStartTime.set(Calendar.HOUR_OF_DAY, 8);
            dateStartTime.set(Calendar.MINUTE, 0);
            dateStartTime.set(Calendar.SECOND, 0);
        }
        if(dateEndTime==null){
            dateEndTime = Calendar.getInstance();
            dateEndTime.set(Calendar.HOUR_OF_DAY, 9);
            dateEndTime.set(Calendar.MINUTE, 0);
            dateEndTime.set(Calendar.SECOND, 0);
        }

         calBeginTime.set(passedYear, passedMonth, passedDay,
                dateStartTime.get(Calendar.HOUR_OF_DAY), dateStartTime.get(Calendar.MINUTE), 0);


        calEndTime.set(passedYear, passedMonth, passedDay,
                dateEndTime.get(Calendar.HOUR_OF_DAY), dateEndTime.get(Calendar.MINUTE), 0);


        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,allDayChecked)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calBeginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calEndTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, descrip.getText().toString())
                .putExtra(Intent.EXTRA_EMAIL, emails.getText().toString());
        startActivity(intent);
    }


}
