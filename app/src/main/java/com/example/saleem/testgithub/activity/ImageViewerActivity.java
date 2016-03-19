package com.example.saleem.testgithub.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.TouchImageView;

public class ImageViewerActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TouchImageView fullScreen =  (TouchImageView) findViewById(R.id.full_image_screen);



        //Picasso.with(this).load(imagePath).into(fullScreen);

    }
}
