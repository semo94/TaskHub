package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.TaskDetails;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;


public class UnderProgressTaskDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private GetResolver getResolver = new GetResolver();
    private Type listType;
    private TaskDetails items;

    String TaskId, UserName, UserPhoto;


    ImageView UserImage, Attach, priorityPhoto, call, email;
    TextView UserTxt, TaskTitle, TaskDesc, taskDeadLine;
    Button feedback;
    SeekBar progressbar;
    TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();
        listType = new TypeToken<TaskDetails>() {
        }.getType();

        Intent intent = getIntent();
        TaskId = intent.getStringExtra("TaskId");
        UserName = intent.getStringExtra("UserName");
        UserPhoto = intent.getStringExtra("UserPhoto");

        HttpConnect.getData(Config.Get_TaskDetails + TaskId, getResolver);


        int color = 0xFF00ACC1;

        progressbar.incrementProgressBy(10);
        progressbar.setMax(100);
        progressbar.setProgress(0);
        progressbar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressbar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));

        progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
               // progressText.setText("Progress: " + "Not Yet");
                if(progress == 0)
                {
                    progressText.setText("Progress: " + "Not Yet");
                }
                 else if(progress==100 )
                {
                    progressText.setText("Progress: " + "Done");
                }
                else
                    progressText.setText("Progress: " + String.valueOf(progress) + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AboutUs.class, "AboutUs"));
        setContentView(R.layout.under_progress_details);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Under Progress Details");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                finish();
            }
        });
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitleTextColor(Color.WHITE);
        materialMenu.setColor(Color.WHITE);
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        UserImage = (ImageView) findViewById(R.id.userImage);
        Attach = (ImageView) findViewById(R.id.attachment_image);
        UserTxt = (TextView) findViewById(R.id.UserName);
        TaskTitle = (TextView) findViewById(R.id.TaskTitle);
        TaskDesc = (TextView) findViewById(R.id.taskDescription);
        feedback = (Button) findViewById(R.id.UpdateProgress);
        taskDeadLine = (TextView) findViewById(R.id.taskDeadLine);
        priorityPhoto = (ImageView) findViewById(R.id.priority_photo);
        call = (ImageView) findViewById(R.id.call_sender);
        email = (ImageView) findViewById(R.id.email_sender) ;
        progressbar = (SeekBar) findViewById(R.id.ProgressBar);
        progressText = (TextView) findViewById(R.id.progressText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("TaskDetails", response.toString() + " TaskDetails response");
            items = (TaskDetails) GlobalConstants.gson.fromJson(response.toString(), listType);


            Picasso.with(UnderProgressTaskDetails.this).load(UserPhoto).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(UserImage);

            Picasso.with(UnderProgressTaskDetails.this).load(items.getAttachedImgUrl()).fit().into(Attach, new Callback() {
                @Override
                public void onSuccess() {
                    Attach.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    Attach.setVisibility(View.GONE);
                }
            });
            UserTxt.setText(UserName);
            TaskTitle.setText(items.getTitle());
            TaskDesc.setText(items.getDescreption());
            taskDeadLine.setText(items.getDLine());


            switch (items.getPriority()) {
                case 0:
                    priorityPhoto.setImageResource(R.drawable.low);
                    break;
                case 1:
                    priorityPhoto.setImageResource(R.drawable.mid);
                    break;
                case 2:
                    priorityPhoto.setImageResource(R.drawable.high);
                    break;
                case 3:
                    priorityPhoto.setImageResource(R.drawable.critical);

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
