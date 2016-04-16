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
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.utils.PhotoManager;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

public class UserInfoActivity extends SetupUI {

    private EditText inputName, inputEmail;
    private TextInputLayout inputLayoutName, inputLayoutEmail;
    private Button btnSignUp;
    int REQUEST_CAMERA = 0,SELECT_FILE = 1, RESIZE_PICTURE_REQUEST_CODE = 2, VIEW_IMAGE_FULL_SCREEN = 3;
    private String UPLOAD_URL ="http://www.taskhub.tk/semo94/TaskHub/upload.php";
    private CircularImageView circularImageView;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private Bitmap bitmap;


    private boolean isPhotoSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setupUI(findViewById(R.id.user_info_activity));
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setTextColor(getResources().getColor(R.color.white));
        circularImageView = (CircularImageView) findViewById(R.id.imageView);


        circularImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                uploadImage();


            }
        });



    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Welcome to TaskHub!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
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

    public void selectImage() {
        if (isPhotoSelected){
            Intent intent = new Intent(this,FullScreenImageViewerActivity.class);
            intent.putExtra(FullScreenImageViewerActivity.PATH_ARG,Uri.fromFile(getFileStreamPath(PhotoManager.USER_PHOTO_FILE_NAME)));
            intent.putExtra(FullScreenImageViewerActivity.TITLE_ARG,"Your Photo");
            startActivityForResult(intent,VIEW_IMAGE_FULL_SCREEN);

        }else{
            final CharSequence[] items = { "Camera", "Library", "Cancel" };

            AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
            builder.setTitle("Choose one from this options:");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (items[item].equals("Library")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select App"), SELECT_FILE);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA) {
                assert data != null;
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                boolean isOK = PhotoManager.CreateImageFile(this,PhotoManager.USER_PHOTO_FILE_NAME,thumbnail);
                if (isOK){
                    openCropActivity(Uri.fromFile(getFileStreamPath(PhotoManager.USER_PHOTO_FILE_NAME)));
                }else {
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                }

            }

            if (requestCode == SELECT_FILE ){
                openCropActivity(data.getData());
            }

            if (requestCode == RESIZE_PICTURE_REQUEST_CODE){
                circularImageView.setImageURI(null);
                circularImageView.setImageURI(Uri.fromFile(getFileStreamPath(PhotoManager.USER_PHOTO_FILE_NAME)));
                isPhotoSelected = true;
            }

            if (requestCode == VIEW_IMAGE_FULL_SCREEN){
                boolean isDeleted = data.getBooleanExtra(FullScreenImageViewerActivity.IS_DELETED_ARG,false);
                boolean isChanged = data.getBooleanExtra(FullScreenImageViewerActivity.IS_CHANGED_ARG,false);

                if (isDeleted){
                    isPhotoSelected = false;
                    circularImageView.setImageResource(R.drawable.default_profile);
                    deleteFile(PhotoManager.USER_PHOTO_FILE_NAME);
                }

                if (isChanged && requestCode==RESIZE_PICTURE_REQUEST_CODE){
                    circularImageView.setImageURI(null);
                    circularImageView.setImageURI(Uri.fromFile(getFileStreamPath(PhotoManager.USER_PHOTO_FILE_NAME)));
                    isPhotoSelected = true;

                }
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(UserInfoActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(UserInfoActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = inputName.getText().toString().trim();

                //Creating parameters
                Map <String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void openCropActivity(Uri photoPath){
        Intent intent = new Intent(this,ResizeImageActivity.class);
        intent.putExtra(ResizeImageActivity.INPUT_PHOTO_URI_ARG, photoPath);
        intent.putExtra(ResizeImageActivity.INPUT_PHOTO_NAME, PhotoManager.USER_PHOTO_FILE_NAME);
        startActivityForResult(intent,RESIZE_PICTURE_REQUEST_CODE);

    }
}
