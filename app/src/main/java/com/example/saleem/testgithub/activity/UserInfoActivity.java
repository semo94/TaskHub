package com.example.saleem.testgithub.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.Gallery.CameraActivity;
import com.example.saleem.testgithub.Gallery.GalleryActivity;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.example.saleem.testgithub.utils.PhotoManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class UserInfoActivity extends AppCompatActivity {

    private EditText inputName, inputEmail;
    private TextInputLayout inputLayoutName, inputLayoutEmail;
    private Button btnSignUp;
    private ImageView circularImageView;
    public static UserInfoActivity userInfoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_user_info);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setTextColor(getResources().getColor(R.color.white));
        circularImageView = (ImageView) findViewById(R.id.imageView);


        circularImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showCameraGalleryDialog();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                uploadPhoto(photo);
            }
        });
        userInfoActivity = this;

    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (inputName.getText().toString().trim().length() < 1) {
            YoYo.with(Techniques.Shake)
                    .playOn(inputName);
            return;
        }
        if (inputEmail.getText().toString().trim().length() < 1) {
            YoYo.with(Techniques.Shake)
                    .playOn(inputEmail);
            return;
        }


        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }


        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("UserName", inputName.getText().toString().trim());
            jsonParams.put("UserEmail", inputEmail.getText().toString().trim());
            jsonParams.put("GCM", "sss");
        } catch (JSONException e) {

        }
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        HttpConnect.postData(Config.UserInfo, entity, UserInfoActivity.this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("Upload response", response.toString() + "  !");
            }
        });


        Toast.makeText(getApplicationContext(), "Welcome to TaskHub!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return TextUtils.isEmpty(email) || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    Intent myIntent;
    private android.support.v7.app.AlertDialog levelDialog;

    private void showCameraGalleryDialog() {

        CharSequence[] items_ = {
                "Camera",
                "Gallery"};
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog);
        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(wrapper);

        builder1.setItems(items_, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {

                            case 0:
                                myIntent = new Intent(UserInfoActivity.this, CameraActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "UserInfo");
                                startActivity(myIntent);

                                break;

                            case 1:
                                myIntent = new Intent(UserInfoActivity.this, GalleryActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "UserInfo");
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


    String photo;

    public void setPhoto(String photoToUploadURL) {
        photo = photoToUploadURL;

        Picasso.with(UserInfoActivity.this).load("file:" + photo).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(circularImageView);

    }
}
