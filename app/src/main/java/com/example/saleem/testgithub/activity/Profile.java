package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.example.saleem.testgithub.utils.Connectivity;
import com.example.saleem.testgithub.utils.DrawerInit;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 4/21/2016.
 */
public class Profile extends AppCompatActivity {

    private Drawer drawer;
    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ImageView imgProfile, imgCover;
    private Type listType;
    private TextView EmailTxt, PhoneTxt, CountryTxt, UserNameTxt;
    private LinearLayout UserNameLinear, CountryLinear, PhoneLinear, EmailLinear;
    private Intent myIntent;
    // private ProfileItems items;
    private TextDrawable coverDrawable;
    private GetSubscriberResolver getSubscriberResolver = new GetSubscriberResolver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();


        getProfileData();

    }

    private void getProfileData() {
//        listType = new TypeToken<ProfileItems>() {
//        }.getType();
//        HttpConnect.getData("", HttpConnect.ConnectionType.TempSessionToken, getSubscriberResolver);
        setUI();
    }


    private void setUI() {


        UserNameLinear.setVisibility(View.VISIBLE);
        CountryLinear.setVisibility(View.VISIBLE);
        PhoneLinear.setVisibility(View.VISIBLE);

//                UserNameTxt.setText(items.getName().toString());
//                firstLetter = items.getName().toString().substring(0, 1);


//                CountryTxt.setText(items.getCountryName().toString());


//                PhoneTxt.setText(items.getMobile().toString());

//                EmailTxt.setText(items.getEmail().toString());


        coverDrawable = TextDrawable.builder()
                .buildRect("", Profile.this.getResources().getColor(R.color.colorPrimary));


        Picasso.with(Profile.this).load(R.drawable.sal).fit().transform(new CircleTransform()).into(imgProfile);
        imgProfile.setVisibility(View.INVISIBLE);
        imgProfile.postDelayed(new Runnable() {
            public void run() {
                imgProfile.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.RollIn).playOn(imgProfile);
            }
        }, 1000);

        imgCover.setImageDrawable(coverDrawable);


    }


    private void initUI() {
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
        PhoneTxt = (TextView) findViewById(R.id.PhoneTxt);
        CountryTxt = (TextView) findViewById(R.id.CountryTxt);
        UserNameTxt = (TextView) findViewById(R.id.UserNameTxt);
        UserNameLinear = (LinearLayout) findViewById(R.id.UserNameLinear);
        CountryLinear = (LinearLayout) findViewById(R.id.CountryLinear);
        PhoneLinear = (LinearLayout) findViewById(R.id.PhoneLinear);
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
            //   items = (ProfileItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setUI();
        }

    }

}
