package com.example.saleem.testgithub.utils;

import android.content.Context;
import android.database.Cursor;

import android.provider.ContactsContract;
import android.util.Log;


import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class UserContacts {

    private static void send2server(String str, final Context context) {
        StringEntity entity = new StringEntity(str, "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        HttpConnect.postData(Config.UploadContacts, entity, context, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("Contacts response", response.toString() + "  !");
            }
        });

        Log.e("Contacts", str);
    }

    public static void getContactList(Context context) {

        JSONArray phonesArray = new JSONArray();
        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);

        long phone = 0;
        if (phones != null) {
            while (phones.moveToNext()) {

//                String name = phones
//                        .getString(phones
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phoneNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                phone = phone2num(phoneNumber);

                if (phone != 0) {
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("PhoneNumber", phone);
                        phonesArray.put(jo);
                    } catch (JSONException e) {

                    }


                }

            }


            send2server(phonesArray.toString(), context);

        }
    }


    private static long phone2num(String str) {

        if (str == null) {
            return 0;
        }

        String str1 = str.replaceAll("\\D+", "");
        if (str1.length() > 15 || str1.length() < 4) {
            return 0;
        }

        long num = Long.parseLong(str1);

        if (str1.length() > 2) {
            if (!(str.substring(0, 1).equals("+") || str.subSequence(0, 2)
                    .equals("00"))) {

                str1 = "962" + num;

                if (str1.length() > 15) {
                    return 0;
                }

                num = Long.parseLong(str1);
            }
        }

        return num;
    }

}
