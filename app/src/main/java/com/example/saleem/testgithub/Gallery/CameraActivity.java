package com.example.saleem.testgithub.Gallery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.Profile;
import com.example.saleem.testgithub.activity.UserInfoActivity;
import com.example.saleem.testgithub.utils.MyExceptionHandler;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        Camera.ShutterCallback, Camera.PictureCallback {


    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton mTakePictureBtn;
    private ImageView switchcamera;

    private String typeOfUpload = "";
    private Image mainImage;
    private  int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private ImageView sendPicture, cancelPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, CameraActivity.class, "CameraActivity"));
        setContentView(R.layout.fragment_camera);



        Intent intent = getIntent();
        typeOfUpload = intent.getStringExtra("typeOfUpload");

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(CameraActivity.this);

        mTakePictureBtn = (ImageButton)
                findViewById(R.id.take_picture);

        switchcamera = (ImageView) findViewById(R.id.switchcamera);
        sendPicture = (ImageView) findViewById(R.id.send_picture);
        cancelPicture = (ImageView) findViewById(R.id.cancel_picture);
        int color = Color.parseColor("#FEFCFC"); // The color u want
        switchcamera.setColorFilter(color);
        switchcamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                switchCamera();
            }
        });

        mTakePictureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTakePictureBtn.isEnabled()) {
                    mTakePictureBtn.setEnabled(false);

                    YoYo.with(Techniques.FadeOutDown).playOn(mTakePictureBtn);
                    mTakePictureBtn.setVisibility(View.GONE);
                    sendPicture.setVisibility(View.VISIBLE);
                    cancelPicture.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInRight).playOn(sendPicture);
                    YoYo.with(Techniques.FadeInLeft).playOn(cancelPicture);
                    mCamera.takePicture(CameraActivity.this, null,
                            CameraActivity.this);
                }
            }
        });

//        PublicMethods.ChangeIconColor(cancelPicture, mActivity.getResources()
//                        .getDrawable(R.drawable.camera_cancel),
//                GlobalConstants.ColorPicker_int);
//
//        PublicMethods.ChangeIconColor(sendPicture, mActivity.getResources()
//                        .getDrawable(R.drawable.camera_send_icon),
//                GlobalConstants.ColorPicker_int);

        cancelPicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTakePictureBtn.setEnabled(true);
                mTakePictureBtn.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).playOn(mTakePictureBtn);
                sendPicture.setVisibility(View.GONE);
                cancelPicture.setVisibility(View.GONE);
                mCamera.startPreview();

            }
        });

        sendPicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mainImage != null) {
                    if (typeOfUpload.equals("profile")) {
                        Profile.profile.uploadPhoto(mainImage.mUri
                                .toString());

                    } else if (typeOfUpload.equals("Task")) {


                    } else if (typeOfUpload.equals("UserInfo")) {
                        UserInfoActivity.userInfoActivity.setPhoto(mainImage.mUri.toString());
                    }
                    back();
                }
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.setDisplayOrientation(90);


                mCamera.startPreview();

                mTakePictureBtn.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

    }

	/*
     * @Override public void onPreviewFrame(byte[] bytes, Camera camera) {
	 * if(!mTakePictureBtn.isEnabled()) mTakePictureBtn.setEnabled(true); }
	 */

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        try {

            mTakePictureBtn.setEnabled(true);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length, options);


            Matrix matrix = new Matrix();
            if (camId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                matrix.postRotate(90);
            } else if(camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                matrix.postRotate(-90);
            }
            picture = Bitmap.createBitmap(picture, 0, 0, picture.getWidth(),
                    picture.getHeight(), matrix, true);

            String path = MediaStore.Images.Media.insertImage(
                   getContentResolver(), picture, "", "");

            Uri contentUri = Uri.parse(path);
            mainImage = getImageFromContentUri(contentUri);

        } catch (OutOfMemoryError e) {

            System.gc();
        }

    }

    public Image getImageFromContentUri(Uri contentUri) {

        String[] cols = {MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION};
        // can post image
        Cursor cursor = getContentResolver().query(contentUri,
                cols, null, null, null);
        cursor.moveToFirst();
        Uri uri = Uri.parse(cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA)));
        int orientation = cursor.getInt(cursor
                .getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
        return new Image(uri, orientation, 0);
    }

    @Override
    public void onShutter() {

    }

    public void switchCamera() {

        mCamera.stopPreview();

        // NB: if you don't release the current camera before switching, you app
        // will crash
        mCamera.release();

        // swap the id of the camera to be used
        if (camId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            camId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        mCamera = Camera.open(camId);
        // Code snippet for this method from somewhere on android developers, i
        // forget where

        setCameraDisplayOrientation(this, camId, mCamera);
        try {
            // this step is critical or preview on new camera will no know where
            // to render to
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    public void setCameraDisplayOrientation(Activity activity, int cameraId,
                                            Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        super.onBackPressed();
    }
}