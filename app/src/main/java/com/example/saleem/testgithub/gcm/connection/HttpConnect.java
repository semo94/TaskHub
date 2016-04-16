package com.example.saleem.testgithub.gcm.connection;

import android.content.Context;


import com.example.saleem.testgithub.utils.GlobalConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.entity.StringEntity;

// used to communicate with the server in order to get the response data
public class HttpConnect {

    public static AsyncHttpClient client = new AsyncHttpClient();

//    public static void addHeaders(ConnectionType connectionType) {
//        client.setTimeout(10000);
//        client.removeAllHeaders();
//        switch (connectionType) {
//
//            case TempSessionToken:
//
//                if (UIConstanst.TempSessionToken != null) {
//                    client.addHeader("TempSessionToken", String.valueOf(UIConstanst.TempSessionToken));
//                } else if (UIConstanst.SessionToken != null) {
//                    client.addHeader("SessionToken", String.valueOf(UIConstanst.SessionToken));
//                }
//
//                break;
//
//            case SessionToken:
//                if (UIConstanst.SessionToken != null) {
//                    client.addHeader("SessionToken", String.valueOf(UIConstanst.SessionToken));
//                }
//
//                break;
//
//        }
//
//
//        if (UIConstanst.UserLanguage != null) {
//            if (UIConstanst.UserLanguage.equals("ar")) {
//                client.addHeader("UserLanguage", "1");
//            } else if (UIConstanst.UserLanguage.equals("en")) {
//                client.addHeader("UserLanguage", "2");
//            }
//        }
//
//        client.addHeader("UserDevicePlatform", "2");
//
//
//    }

    //Get
    public static void getData(String url, ConnectionType connectionType, AsyncHttpResponseHandler responseHandler) {
        //addHeaders(connectionType);
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    //Put
    public static void putData(String url, StringEntity entity, Context context, ConnectionType connectionType, AsyncHttpResponseHandler responseHandler) {
       // addHeaders(connectionType);
        client.put(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    //Post
    public static void postData(String url, StringEntity entity, Context context, ConnectionType connectionType, AsyncHttpResponseHandler responseHandler) {

        //addHeaders(connectionType);
        client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return GlobalConstants.serverURI + relativeUrl;
    }

    public enum ConnectionType {
        SessionToken, TempSessionToken, noHeader
    }
}
