package com.example.saleem.testgithub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class cache {


    public cache(final Context context) {

        DataB.obj = new DataB(context);

        try {
            openDataBase(context);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }

    }


    public void openDataBase(final Context context) throws SQLException {

        if (context == null) {

            return;
        }
        if (DataB.sqlDB == null) {
            DataB.sqlDB = DataB.obj.getWritableDatabase();
        }

    }

//    public void deleteCache() {
//        DataB.sqlDB.delete("cache", null, null);
//    }

    public long AddCache(String id, String val) {

        ContentValues cv = new ContentValues();

        cv.put(DatabaseConstants.Id, id);
        cv.put(DatabaseConstants.Val, val);

        return DataB.sqlDB.insertWithOnConflict(DatabaseConstants.Cache, null, cv,
                SQLiteDatabase.CONFLICT_REPLACE);

    }


    public String SelectFromCache(String id) {

        String JSON = "";

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + DatabaseConstants.Cache + " WHERE " + DatabaseConstants.Id + " = '" + id + "'";

            cursor = DataB.sqlDB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                JSON = cursor.getString(1);

                if (cursor != null) {
                    cursor.close();
                }
                return JSON;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;

    }

}

