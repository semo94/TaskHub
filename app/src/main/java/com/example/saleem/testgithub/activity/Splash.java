package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;

public class Splash extends AppCompatActivity {


    private ImageView image1;
    private TextView txt1;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.splash);

        image1 = (ImageView) findViewById(R.id.image1);
        txt1 = (TextView) findViewById(R.id.txt1);


        image1.setVisibility(View.INVISIBLE);
        txt1.setVisibility(View.INVISIBLE);


        image1.postDelayed(new Runnable() {
            public void run() {
                image1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.RollIn).duration(1500).playOn(image1);
            }
        }, 400);

        txt1.postDelayed(new Runnable() {
            public void run() {
                txt1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.RotateIn).duration(1500).playOn(txt1);
            }
        }, 400);


        handler.postDelayed(masterRunnable, 2500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private Runnable masterRunnable = new Runnable() {
        @Override
        public void run() {
            Intent myIntent = new Intent(Splash.this, MainActivity.class);
            Splash.this.startActivity(myIntent);
            finish();
        }
    };
}