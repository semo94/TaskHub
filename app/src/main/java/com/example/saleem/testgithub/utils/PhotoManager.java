package com.example.saleem.testgithub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PhotoManager {

    public static File createTempFile(long id) throws IOException {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File RPDir = new File(externalStoragePublicDirectory+File.separator + "Task Hup");
        RPDir.mkdir();
        if (id == -1){
            return new File(RPDir,"Temp_user_photo.jpg");
        }else{
            return new File(RPDir,"Temp_user_"+id+"_photo.jpg");
        }
    }

    public static Uri CreateImageFile(Bitmap bitmap){
        FileOutputStream out = null;
        try {
            File file = createTempFile(-1);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean CreateImageFile(Bitmap bitmap,Uri uri){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(uri.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
