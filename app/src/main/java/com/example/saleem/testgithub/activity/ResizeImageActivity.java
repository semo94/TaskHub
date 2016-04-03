package com.example.saleem.testgithub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.utils.PhotoManager;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ResizeImageActivity extends AppCompatActivity {

    public static final String INPUT_PHOTO_URI_ARG = "input_photo_uri_arg";
    public static final String OUTPUT_PHOTO_URI_ARG = "output_photo_uri_arg";


    private CropImageView cropImageView;
    Uri inputPhotoUri ;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize_image);

        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(640, 640);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);



        if (getIntent() != null){
            inputPhotoUri = getIntent().getParcelableExtra(INPUT_PHOTO_URI_ARG);
            if (inputPhotoUri == null){
                setResult(RESULT_CANCELED);
                finish();
            }else{
                cropImageView.setImageUriAsync(inputPhotoUri);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resize_menu, menu);
        return true;
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress));
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.done_menu_done_item:
                showProgressDialog();
                Uri imageUriAfterCropping = saveImage();
                if (imageUriAfterCropping != null){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    intent.putExtra(OUTPUT_PHOTO_URI_ARG,imageUriAfterCropping);
                    dialog.dismiss();
                    finish();
                }else{
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                }

                break;
        }

        return false;
    }

    private Uri saveImage() {
        return PhotoManager.CreateImageFile(cropImageView.getCroppedImage());
    }
}

