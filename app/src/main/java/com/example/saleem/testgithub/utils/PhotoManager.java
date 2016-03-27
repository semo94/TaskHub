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

    public static File createImageFile(long id) throws IOException {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File RPDir = new File(externalStoragePublicDirectory+File.separator + "Reminder Plus");
        RPDir.mkdir();
        if (id == -1){
            File imageFile = new File(RPDir,"RP_user_photo.jpg");
            return imageFile;
        }else{
            File imageFile = new File(RPDir,"RP_user_"+id+"_photo.jpg");
            return imageFile;
        }
    }


    public static File createTempImageFile(long id) throws IOException {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File RPDir = new File(externalStoragePublicDirectory+File.separator + "Reminder Plus");
        RPDir.mkdir();
        if (id == -1){
            File imageFile = new File(RPDir,"Temp_RP_user_photo.jpg");
            return imageFile;
        }else{
            File imageFile = new File(RPDir,"Temp_RP_user_"+id+"_photo.jpg");
            return imageFile;
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


    public static void savePhotoAndDeleteTemp(Context context,Uri tempFileUri,File dest) {
        try {
            InputStream iStream =  context.getContentResolver().openInputStream(tempFileUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }


            OutputStream out = new FileOutputStream(dest); // create non-temp photo
            out.write(byteBuffer.toByteArray());

            new File(tempFileUri.getPath()).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Uri savePhotoFromGallary(Context context,Uri photoUri,File dest) {
        try {
            InputStream iStream =  context.getContentResolver().openInputStream(photoUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            OutputStream out = new FileOutputStream(dest);
            out.write(byteBuffer.toByteArray());

            return Uri.fromFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
