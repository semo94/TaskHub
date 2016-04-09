package com.example.saleem.testgithub.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.TouchImageView;

import java.io.File;


public class FullScreenImageViewerActivity extends AppCompatActivity{
    public static final String TITLE_ARG = "title";
    public static final String PATH_ARG  = "path";
    public static final String IS_DELETED_ARG  = "is_deleted";
    public static final String IS_CHANGED_ARG  = "is_changed";
    int SELECT_FILE =1, REQUEST_CAMERA =0;

    TouchImageView image;

    private String activityTitle = null;
    private Uri imagePath     = null;


    private void editImage() {
        final CharSequence[] items = { "Take a photo", "Choose from library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(FullScreenImageViewerActivity.this);
        builder.setTitle("Change your profile picture:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image_layout);


        image = (TouchImageView) findViewById(R.id.full_image_image);

        if (getIntent() != null){
            activityTitle = getIntent().getStringExtra(TITLE_ARG);
            imagePath     = (Uri)getIntent().getParcelableExtra(PATH_ARG);
        }else finish();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(activityTitle);
        image.setImageURI(imagePath);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case android.R.id.home:
                intent.putExtra(IS_DELETED_ARG,false);
                intent.putExtra(IS_CHANGED_ARG,false);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.full_viewer_delete_item:
                intent.putExtra(IS_DELETED_ARG,true);
                intent.putExtra(IS_CHANGED_ARG,false);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.full_viewer_change_item:
                intent.putExtra(IS_DELETED_ARG,false);
                intent.putExtra(IS_CHANGED_ARG, true);
                editImage();
                setResult(RESULT_OK, intent);
                //finish();
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_screen_viewer_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}