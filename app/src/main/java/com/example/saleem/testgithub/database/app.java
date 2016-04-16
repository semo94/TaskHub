package com.example.saleem.testgithub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class app {


    public app(final Context context) {

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

    public long AddApp(String id, String val) {


        ContentValues cv = new ContentValues();

        cv.put(DatabaseConstants.Id, id);
        cv.put(DatabaseConstants.Val, val);


        return DataB.sqlDB.insertWithOnConflict(DatabaseConstants.App, null, cv,
                SQLiteDatabase.CONFLICT_REPLACE);

    }


    public String SelectApp(String id) {

        String JSON = "";

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + DatabaseConstants.App + " WHERE " + DatabaseConstants.Id + " = '" + id + "'";

            cursor = DataB.sqlDB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {

                JSON = cursor.getString(1);

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
