package com.example.saleem.testgithub.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import fr.ganfra.materialspinner.MaterialSpinner;


public class PendingTaskDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private GetResolver getResolver = new GetResolver();
    private Type listType;
    private TaskDetails items;

    String TaskId, UserName, UserPhoto;


    ImageView UserImage, Attach, priorityPhoto;
    TextView UserTxt, TaskTitle, TaskDesc, taskDeadLine;
    Button delete;
    MaterialSpinner pendingSpinner;

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

        String[] ITEMS = {"Accept", "Decline", "Block User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pendingSpinner = (MaterialSpinner) findViewById(R.id.pendingSpinner);
        pendingSpinner.setAdapter(adapter);
    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AboutUs.class, "AboutUs"));
        setContentView(R.layout.pending_details);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Pending Details");
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
        delete = (Button) findViewById(R.id.DeleteButton);
        taskDeadLine = (TextView) findViewById(R.id.taskDeadLine);
        priorityPhoto = (ImageView) findViewById(R.id.priority_photo);
        pendingSpinner = (MaterialSpinner) findViewById(R.id.pendingSpinner);
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


            Picasso.with(PendingTaskDetails.this).load(UserPhoto).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(UserImage);

            Picasso.with(PendingTaskDetails.this).load(items.getAttachedImgUrl()).fit().into(Attach);
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