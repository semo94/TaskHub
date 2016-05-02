package com.example.saleem.testgithub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.Gallery.CameraActivity;
import com.example.saleem.testgithub.Gallery.GalleryActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by admin on 4/21/2016.
 */
public class Profile extends AppCompatActivity implements DataBaseAble {

    private Drawer drawer;
    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ImageView imgProfile, imgCover;
    private Type listType;
    private EditText EmailTxt, UserNameTxt;
    private LinearLayout UserNameLinear, EmailLinear;
    private Intent myIntent;
    private ProfileItems items;
    private TextDrawable coverDrawable;
    private GetSubscriberResolver getSubscriberResolver = new GetSubscriberResolver();
    private ApiHelper apiHelper;
    public static Profile profile;
    private Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        getProfileData();

        profile = this;
    }

    private void getProfileData() {
        listType = new TypeToken<ProfileItems>() {
        }.getType();
        this.apiHelper = new ApiHelper(Profile.this);
        GlobalConstants.db.GetCache(Config.Get_UserInfo, 0, this, apiHelper.App, apiHelper.Cache);
        HttpConnect.getData(Config.Get_UserInfo, getSubscriberResolver);
    }


    private void setUI() {
        if (items.getName() != null) {
            UserNameTxt.setText(items.getName());
        }
        if (items.getEmail() != null) {
            EmailTxt.setText(items.getEmail());
        }
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


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraGalleryDialog();
            }
        });
        imgCover.setImageDrawable(coverDrawable);
    }

    private AlertDialog levelDialog;

    private void showCameraGalleryDialog() {

        CharSequence[] items_ = {
                "Camera",
                "Gallery"};
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog);
        // Creating and Building the Dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(wrapper);

        builder1.setItems(items_, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {

                            case 0:


                                myIntent = new Intent(Profile.this, CameraActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "profile");
                                startActivity(myIntent);

                                break;

                            case 1:

                                myIntent = new Intent(Profile.this, GalleryActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "profile");
                                startActivity(myIntent);
                                break;

                        }

                        levelDialog.dismiss();
                    }
                }

        );
        levelDialog = builder1.create();
        levelDialog.show();
        levelDialog.setCanceledOnTouchOutside(true);

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
        toolbar.setTitleTextColor(Color.WHITE);
        materialMenu.setColor(Color.WHITE);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Send", "Send");
                if (UserNameTxt.getText().toString().trim().length() < 1) {
                    YoYo.with(Techniques.Shake)
                            .playOn(UserNameTxt);
                    return;
                }
                if (EmailTxt.getText().toString().trim().length() < 1) {
                    YoYo.with(Techniques.Shake)
                            .playOn(EmailTxt);
                    return;
                }

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("UserName", UserNameTxt.getText().toString().trim());
                    jsonParams.put("UserEmail", EmailTxt.getText().toString().trim());
                    jsonParams.put("GCM", "sss");
                } catch (JSONException e) {

                }
                StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                HttpConnect.postData(Config.UserInfo, entity, Profile.this, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Upload response", response.toString() + "  !");
                    }
                });
            }
        });
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        imgProfile = (ImageView) findViewById(R.id.profileImage);
        imgCover = (ImageView) findViewById(R.id.coverImage);
        EmailTxt = (EditText) findViewById(R.id.EmailTxt);
        UserNameTxt = (EditText) findViewById(R.id.UserNameTxt);
        UserNameLinear = (LinearLayout) findViewById(R.id.UserNameLinear);
        EmailLinear = (LinearLayout) findViewById(R.id.EmailLinear);
        Send = (Button) findViewById(R.id.Send);

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


    public void uploadPhoto(String photoToUploadURL) {
        Log.e("uploadPhoto", "uploadPhoto");
        //  photoToUploadURL = photoToUploadURL + ".jpg";
        HttpConnect.postUploadPhoto("http://www.taskhub.tk/semo94/TaskHub/API/uploadphoto.php", photoToUploadURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("UploadResponse", response.toString() + " !");

            }
        });
    }
}
