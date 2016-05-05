package com.example.saleem.testgithub.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;


public class Alarm_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);


        String sound_id = intent.getExtras().getString("quote id");
        Log.e("Sound quote is" , sound_id);

        long alarm_sound_id = intent.getExtras().getLong("sound id");

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra("extra", state);
        serviceIntent.putExtra("quote id", sound_id);

        context.startService(serviceIntent);
    }

}
