package com.example.saleem.testgithub.helper;

/**
 * Created by Mo7ammed on 13/03/2016.
 */


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Country implements Parcelable{
    private String iso;
    private String code;
    private String name;


    public Country() {
    }

    public Country(String iso, String name, String code) {
        this.iso = iso;
        this.name = name;
        this.code = code;
    }

    public Country(String name, String code) {
        this.iso = "!!";
        this.name = name;
        this.code = code;
    }


    public Country(Parcel in ){
        String[] data = new String[3];

        in.readStringArray(data);
        this.iso = data[0];
        this.name = data[1];
        this.code = data[2];
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "iso='" + iso + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.iso, this.name, this.code});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };


    public static ArrayList<Country> countriesList(Context context) throws XmlPullParserException,IOException
    {
        XmlPullParser parser = Xml.newPullParser();
        InputStream in_s = context.getAssets().open("Countries.xml");
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in_s, null);
        ArrayList<Country> countries = null;
        int eventType = parser.getEventType();

        Country currentCountry = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    countries = new ArrayList<>();
                    break;


                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("country")){
                        currentCountry = new Country();
                        currentCountry.setCode(parser.getAttributeValue(null,"phoneCode"));
                        currentCountry.setName(parser.getAttributeValue(null, "name"));
                        currentCountry.setIso(parser.getAttributeValue(null,"code"));
                    }

                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("country") && currentCountry != null){
                        countries.add(currentCountry);
                    }
            }
            eventType = parser.next();
        }
        return countries;
    }
}