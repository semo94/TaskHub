package com.example.saleem.testgithub.gcm.connection;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class GcmMessageHandler extends IntentService {

    private String Sub, Body, trg, tid, logo, serverDate;
    private long time;
    private Bitmap icon;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        //       String messageType = gcm.getMessageType(intent);

        for (String key : extras.keySet()) {

            Log.e("Bundle Debug", key + " = \"" + extras.get(key) + "\"");
        }

        if (extras.getString("msg").equals(null)) {

            return;
        }

        Body = extras.getString("msg");
        trg = extras.getString("trg");
        tid = extras.getString("tid");
        logo = extras.getString("logo");
        Sub = extras.getString("sub");
        if (Sub.matches("")) {
            Sub = this.getResources().getString(R.string.app_name);
        }
        serverDate = extras.getString("time");


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // I assume d-M, you may refer to M-d for month-day instead.
        try {
            Date date = formatter.parse(serverDate.trim());
            time = date.getTime();
            TimeZone tz = TimeZone.getDefault();
            time = time + tz.getRawOffset();
        } catch (ParseException e) {
            time = System.currentTimeMillis();
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);


        showNotification();


    }

    private static int peepLastCall = 0;

    private void showNotification() {
        icon = getBitmapFromURL(logo);
        int now = (int) (System.currentTimeMillis() / 1000);
        long[] vib;
        if (peepLastCall + 1 >= now) {
            vib = new long[]{0, 0, 0, 0};
        } else {
            vib = new long[]{0, 100, 200, 300};
        }
        peepLastCall = now;

        Intent launchIntent;
        try {
            launchIntent = new Intent(GcmMessageHandler.this, MainActivity.class);

            if (trg.equals("1") || trg.equals("3") || trg.equals("6")) { //MyCoins
                //  launchIntent.putExtra(UIConstanst.GCM.GCM, UIConstanst.GCM.MyCoins);
            } else if (trg.equals("2")) {//Dealer Details
                //   launchIntent.putExtra(UIConstanst.GCM.GCM, UIConstanst.GCM.Dealers);
            } else if (trg.equals("4")) {//Channel Details
                //   launchIntent.putExtra(UIConstanst.GCM.GCM, UIConstanst.GCM.Channel);
            } else if (trg.equals("5")) {//System Admin
                //  launchIntent.putExtra(UIConstanst.GCM.GCM, UIConstanst.GCM.System);
            }
            // launchIntent.putExtra(UIConstanst.GCM.GCMID, tid);

            launchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pIntent = PendingIntent.getActivity(
                    GcmMessageHandler.this, 0, launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            android.app.Notification n = new NotificationCompat.Builder(this)
                    .setContentTitle(Sub).setContentText(Body)
                    .setWhen(time)
                    .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(icon)
                    .setContentIntent(pIntent).setVibrate(vib)
                    .setAutoCancel(true).build();

            //    n.contentView.setImageViewResource(android.R.id.icon, R.drawable.logo_icon);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            n.flags |= android.app.Notification.FLAG_SHOW_LIGHTS;
            n.ledARGB = 0xff660099;
            n.ledOnMS = 300;
            n.ledOffMS = 1000;
            n.defaults |= android.app.Notification.DEFAULT_SOUND;
            notificationManager.notify(0, n);
        } catch (Exception e) {

        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}