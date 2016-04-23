package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.PendingItems;
import com.example.saleem.testgithub.gson.items.ProfileItems;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.example.saleem.testgithub.utils.DrawerInit;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 4/21/2016.
 */
public class Profile extends AppCompatActivity implements DataBaseAble {

    private Drawer drawer;
    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ImageView imgProfile, imgCover;
    private Type listType;
    private TextView EmailTxt, UserNameTxt;
    private LinearLayout UserNameLinear, EmailLinear;
    private Intent myIntent;
    private ProfileItems items;
    private TextDrawable coverDrawable;
    private GetSubscriberResolver getSubscriberResolver = new GetSubscriberResolver();
    private ApiHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        getProfileData();
    }

    private void getProfileData() {
        listType = new TypeToken<ProfileItems>() {
        }.getType();
        this.apiHelper = new ApiHelper(Profile.this);
        GlobalConstants.db.GetCache(Config.Get_UserInfo, 0, this, apiHelper.App, apiHelper.Cache);
        HttpConnect.getData(Config.Get_UserInfo, getSubscriberResolver);
    }


    private void setUI() {
        UserNameTxt.setText(items.getName().toString());

        EmailTxt.setText(items.getEmail().toString());

        coverDrawable = TextDrawable.builder()
                .buildRect("", Profile.this.getResources().getColor(R.color.colorDarkAccent));

        Picasso.with(Profile.this).load(items.getImage()).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(imgProfile);
        imgProfile.setVisibility(View.INVISIBLE);
        imgProfile.postDelayed(new Runnable() {
            public void run() {
                imgProfile.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.RollIn).playOn(imgProfile);
            }
        }, 500);

        imgCover.setImageDrawable(coverDrawable);

    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, Profile.class, "Profile"));
        setContentView(R.layout.profile);

        initControls();

        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Profile");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        drawer = new DrawerInit(drawer, 1).initDrawer(this, toolbar, materialMenu);

    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        imgProfile = (ImageView) findViewById(R.id.profileImage);
        imgCover = (ImageView) findViewById(R.id.coverImage);
        EmailTxt = (TextView) findViewById(R.id.EmailTxt);
        UserNameTxt = (TextView) findViewById(R.id.UserNameTxt);
        UserNameLinear = (LinearLayout) findViewById(R.id.UserNameLinear);
        EmailLinear = (LinearLayout) findViewById(R.id.EmailLinear);
    }


    @Override
    public void onBackPressed() {
        back();
    }

    public void back() {

        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null) {
            drawer.setSelection(1, false);
        }

    }

    class GetSubscriberResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            items = (ProfileItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setUI();
            GlobalConstants.db.SetCache(Config.Get_UserInfo, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
        }
    }


    @Override
    public void SetApp_db(String Key, int tag) {

    }

    @Override
    public void SetCache_db(String Key, int tag) {

    }

    @Override
    public void GetApp_db(String Key, String Value, int tag) {

    }

    @Override
    public void GetCache_db(String Key, String Value, int tag) {
        if (Value != null) {
            items = (ProfileItems) GlobalConstants.gson.fromJson(Value, listType);
            setUI();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
