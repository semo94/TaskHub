package com.example.saleem.testgithub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoManager {


    public static final String USER_PHOTO_FILE_NAME = "Temp_user_photo";

    public static File createTempFile(long id) throws IOException {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File RPDir = new File(externalStoragePublicDirectory+File.separator + "TaskHub");
        RPDir.mkdir();
        if (id == -1){
            return new File(RPDir,"Temp_user_photo.jpg");
        }else{
            return new File(RPDir,"Temp_user_"+id+"_photo.jpg");
        }



    }

    public static boolean CreateImageFile(Context context,Bitmap bitmap){
        FileOutputStream out = null;
        try {
            //File file = createTempFile(-1);

            out = context.openFileOutput(USER_PHOTO_FILE_NAME, Context.MODE_PRIVATE);
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
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

    public static boolean CreateImageFile(Bitmap bitmap,Uri uri){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(uri.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
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
