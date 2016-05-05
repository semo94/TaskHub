package com.example.saleem.testgithub.gcm.connection;

import android.content.Context;


import com.example.saleem.testgithub.utils.GlobalConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.entity.StringEntity;

// used to communicate with the server in order to get the response data
public class HttpConnect {

    public static AsyncHttpClient client = new AsyncHttpClient();


    //Get
    public static void getData(String url, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("UserId", GlobalConstants.UserID);
        client.get(url, responseHandler);
    }

    //Put
    public static void putData(String url, StringEntity entity, Context context, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("UserId", GlobalConstants.UserID);
        client.put(context, url, entity, "application/json", responseHandler);
    }

    //Post
    public static void postData(String url, StringEntity entity, Context context, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("UserId", GlobalConstants.UserID);
        client.post(context, url, entity, "application/json", responseHandler);
    }


    public static void postUploadPhoto(String url, String FilePath, AsyncHttpResponseHandler responseHandler) {
        File myFile = new File(FilePath);
        final RequestParams params = new RequestParams();
        try {
            params.put("img", myFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.addHeader("UserId", GlobalConstants.UserID);
        client.post(url, params, responseHandler);
    }




}
