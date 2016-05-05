package com.example.saleem.testgithub.reminder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.saleem.testgithub.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Random;


public class RBTime extends Activity implements AdapterView.OnItemSelectedListener {

    AlarmManager alarm_manager;
    TimePicker alarmTimePicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    Spinner spinner;
    int alarm_quote=0;



    private GoogleApiClient client;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.rbstime);

        this.context = this;

        //Alarm Manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //init time picker
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);



        //Text Update
        update_text = (TextView) findViewById(R.id.update_text);


        //Intent
        final Intent myIntent = new Intent(this.context, Alarm_Receiver.class);


        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner_sound);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_sounds, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        //calender
        final Calendar calendar = Calendar.getInstance();


        //Button s & e

        Button start_alarm = (Button) findViewById(R.id.start_alarm);

        start_alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                int hour = alarmTimePicker.getCurrentHour();
                int minute = alarmTimePicker.getCurrentMinute();

                String minute_string = String.valueOf(minute);
                String hour_string = String.valueOf(hour);




                myIntent.putExtra("extra", "yes");
                myIntent.putExtra("quote id", String.valueOf(alarm_quote));
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);





                //24 to 12
                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12) ;
                }

                //update text
                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);


                pending_intent = PendingIntent.getBroadcast(RBTime.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);





            }
        });

        Button end_alarm = (Button) findViewById(R.id.end_alarm);


        end_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent.putExtra("extra", "no");
                myIntent.putExtra("quote id", String.valueOf(alarm_quote));

                alarm_manager.cancel(pending_intent);
                set_alarm_text("Alarm Cancelled !");






                //update text

            }
        });
    }


    private void set_alarm_text(String output) {
        update_text.setText(output);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(parent.getContext(), "Spinner item 3!" + id, Toast.LENGTH_SHORT).show();
        alarm_quote = (int) id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}