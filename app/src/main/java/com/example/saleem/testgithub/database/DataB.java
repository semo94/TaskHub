package com.example.saleem.testgithub.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataB extends SQLiteOpenHelper {

    protected static String dBname = "farfachatdb.db";
    private static int v = 21;

    protected static DataB obj;
    public static SQLiteDatabase sqlDB = null;

    public DataB(Context context) {

        super(context, dBname, null, v);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS app(id TEXT PRIMARY KEY,val TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS cache(id TEXT PRIMARY KEY,val TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {

            db.execSQL("DROP TABLE IF EXISTS app");
            db.execSQL("DROP TABLE IF EXISTS cache");


            onCreate(db);

        } catch (SQLException e) {

        }

    }


}
