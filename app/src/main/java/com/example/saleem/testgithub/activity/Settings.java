package com.example.saleem.testgithub.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.BlockItems;
import com.example.saleem.testgithub.utils.DrawerInit;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.example.saleem.testgithub.utils.PrefManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Settings extends AppCompatActivity {

    private Drawer drawer;
    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private MaterialRippleLayout deactivateRipple, otherSettings;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();

        deactivateRipple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpConnect.getData(Config.Get_DeactivateAccount, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        logout();
                        Log.e("Deactivate Account", response.toString());
                        try {
                            if (response.has("IsSucceeded")) {
                                if (response.getBoolean("IsSucceeded")) {
                                    logout();
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }

                });

            }
        });

        otherSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, BlockList.class);
                startActivity(myIntent);
            }
        });
    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, Settings.class, "Settings"));
        setContentView(R.layout.settings);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        materialMenu.setColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Settings");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        drawer = new DrawerInit(drawer, 3).initDrawer(this, toolbar, materialMenu);

        animateControls();
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        deactivateRipple = (MaterialRippleLayout) findViewById(R.id.rippleDeactivate);
        otherSettings = (MaterialRippleLayout) findViewById(R.id.rippleOther);

        deactivateRipple.setVisibility(View.INVISIBLE);
        otherSettings.setVisibility(View.INVISIBLE);
    }

    private void animateControls() {

        deactivateRipple.postDelayed(new Runnable() {
            public void run() {
                deactivateRipple.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInLeft).playOn(deactivateRipple);
            }
        }, 1100);

        otherSettings.postDelayed(new Runnable() {
            public void run() {
                otherSettings.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInRight).playOn(otherSettings);
            }
        }, 1100);
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
            drawer.setSelection(3, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    private void logout() {
        pref = new PrefManager(Settings.this);
        pref.clearSession();

        Intent intent = new Intent(Settings.this, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
}
