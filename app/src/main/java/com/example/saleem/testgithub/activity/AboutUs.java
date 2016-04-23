package com.example.saleem.testgithub.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.utils.DrawerInit;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.mikepenz.materialdrawer.Drawer;

/**
 * Created by admin on 4/21/2016.
 */
public class AboutUs extends AppCompatActivity {

    private Drawer drawer;
    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ImageView imgLogo;
    private TextView txtCopyRight, txtVersion, name1, name2;
    private PackageInfo pInfo;
    private String versionST = "Default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();


        processActivityData();
    }

    private void processActivityData() {


        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionST = pInfo.versionName;
            txtVersion.setText("( " + "Version" + " " + versionST + " " + ")");
        } catch (PackageManager.NameNotFoundException e1) {

        }

    }

    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AboutUs.class, "AboutUs"));
        setContentView(R.layout.about_us);
        initControls();


        imgLogo.setVisibility(View.INVISIBLE);
        txtCopyRight.setVisibility(View.INVISIBLE);
        txtVersion.setVisibility(View.INVISIBLE);
        name1.setVisibility(View.INVISIBLE);
        name2.setVisibility(View.INVISIBLE);


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("About Us");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        drawer = new DrawerInit(drawer, 2).initDrawer(this, toolbar, materialMenu);

        animateControls();
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name1 = (TextView) findViewById(R.id.name1);
        name2 = (TextView) findViewById(R.id.name2);
        txtCopyRight = (TextView) findViewById(R.id.copyRight);
        txtVersion = (TextView) findViewById(R.id.version);
        imgLogo = (ImageView) findViewById(R.id.Logo);
    }

    private void animateControls() {
        imgLogo.postDelayed(new Runnable() {
            public void run() {
                imgLogo.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInDown).playOn(imgLogo);

            }
        }, 500);

        txtVersion.postDelayed(new Runnable() {
            public void run() {
                txtVersion.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).playOn(txtVersion);
            }
        }, 1000);

        txtCopyRight.postDelayed(new Runnable() {
            public void run() {
                txtCopyRight.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).playOn(txtCopyRight);
            }
        }, 1000);


        name1.postDelayed(new Runnable() {
            public void run() {
                name1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInLeft).playOn(name1);
            }
        }, 1100);

        name2.postDelayed(new Runnable() {
            public void run() {
                name2.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInRight).playOn(name2);
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
            drawer.setSelection(2, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
