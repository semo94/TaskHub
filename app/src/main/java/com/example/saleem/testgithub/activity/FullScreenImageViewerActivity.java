package com.example.saleem.testgithub.activity;

/**
 * Created by mo7ammed on 03/04/16.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.TouchImageView;
import com.squareup.picasso.Picasso;


public class FullScreenImageViewerActivity extends AppCompatActivity{
    public static final String TITLE_ARG = "title";
    public static final String PATH_ARG  = "path";
    public static final String IS_DELETED_ARG  = "is_deleted";
    TouchImageView image;

    private String activityTitle = null;
    private Uri imagePath     = null;

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
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.full_viewer_delete_item:
                intent.putExtra(IS_DELETED_ARG,true);
                setResult(RESULT_OK, intent);
                finish();
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