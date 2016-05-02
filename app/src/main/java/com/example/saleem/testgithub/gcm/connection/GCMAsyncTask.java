package com.example.saleem.testgithub.gcm.connection;

import android.content.Context;
import android.os.AsyncTask;

import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.IOException;


public class GCMAsyncTask extends AsyncTask<String, Integer, String> implements
        DataBaseAble {

    private Context context;
    private String registrationId;
    private ApiHelper apiHelper;
    private String gcmID = "980504178442";

    public GCMAsyncTask(Context context) {
        this.context = context;
        this.apiHelper = new ApiHelper(context);
    }

    public GCMResultsListener listener;

    public void setOnResultsListener(GCMResultsListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try {

            gcm.unregister();
            registrationId = gcm.register(gcmID);

            GlobalConstants.db.SetApp("registrationId", registrationId, 0,
                    GCMAsyncTask.this, apiHelper.App, apiHelper.Cache);

        } catch (IOException e1) {

            e1.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onGCMResultsSucceeded(registrationId);
    }

    @Override
    public void SetApp_db(String Key, int tag) {

    }

    @Override
    public void SetCache_db(String Key, int tag) {

    }

    @Override
    public void GetApp_db(String Key, String Value, int tag) {

    }

    @Override
    public void GetCache_db(String Key, String Value, int tag) {

    }


}
