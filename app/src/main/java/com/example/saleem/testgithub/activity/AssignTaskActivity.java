package com.example.saleem.testgithub.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saleem.testgithub.Gallery.CameraActivity;
import com.example.saleem.testgithub.Gallery.GalleryActivity;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AssignTaskActivity extends AppCompatActivity {

    private EditText dLine, taskTitle, desc, attachment;
    private TextInputLayout inputLayoutTitle, inputLayoutDeadLine;
    private Button assign;
    private Spinner priority;
    private int spnItem;
    private ArrayList<String> idsToSend = new ArrayList<>();
    private ImageView attachmentPhoto;
    public static AssignTaskActivity assignTaskActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.content_assign_task);


        idsToSend = getIntent().getStringArrayListExtra("idsToSend");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setTitleColor(Color.WHITE);
        assign = (Button) findViewById(R.id.btn_assignTask);
        desc = (EditText) findViewById(R.id.input_des);
        inputLayoutDeadLine = (TextInputLayout) findViewById(R.id.input_layout_cal);
        inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_task);
        dLine = (EditText) findViewById(R.id.deadLine);
        taskTitle = (EditText) findViewById(R.id.input_task);
        priority = (Spinner) findViewById(R.id.priority_level);
        attachment = (EditText) findViewById(R.id.input_attach);
        attachmentPhoto = (ImageView) findViewById(R.id.attachmentPhoto);

        taskTitle.addTextChangedListener(new MyTextWatcher(taskTitle));
        dLine.addTextChangedListener(new MyTextWatcher(dLine));


        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        //date setup
        showDateDialog();

        //spinner setup
        spinner();

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCameraGalleryDialog();
            }
        });

        assignTaskActivity = this;
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
                                myIntent = new Intent(AssignTaskActivity.this, CameraActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "Task");
                                startActivity(myIntent);

                                break;

                            case 1:
                                myIntent = new Intent(AssignTaskActivity.this, GalleryActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                myIntent.putExtra("typeOfUpload", "Task");
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

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateTitle()) {
            return;
        }

        if (!validateDeadLine()) {
            return;
        }

        if (!validatePriority()) {
            return;
        }

        JSONObject jsonParams = new JSONObject();
        JSONArray jsonArray = new JSONArray(idsToSend);
        try {
            jsonParams.put("Receivers", jsonArray);
            jsonParams.put("TaskTitle", "Title Test 1");
            jsonParams.put("TaskDesc", "Desc Test 1");
            jsonParams.put("Deadline", "17-05-2016 01:00:00");
            jsonParams.put("PriorityID", "1");
        } catch (JSONException e) {

        }
        StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        HttpConnect.postData("http://www.taskhub.tk/semo94/TaskHub/API/newTask.php", entity, AssignTaskActivity.this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    uploadPhoto(photo, response.getString("NewTaskId"));
                } catch (JSONException e) {

                }
            }
        });

        Toast.makeText(getApplicationContext(), "Your task has been assigned", Toast.LENGTH_SHORT).show();
    }

    private boolean validateTitle() {
        if (taskTitle.getText().toString().trim().isEmpty()) {
            inputLayoutTitle.setError(getString(R.string.err_msg_Title));
            requestFocus(taskTitle);
            return false;
        } else {
            inputLayoutTitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePriority() {
        if (priority.getSelectedItem().toString().equals("Select your task priority")) {
            Toast.makeText(getApplicationContext(), "You must select priority for your task", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }


    private boolean validateDeadLine() {
        if (dLine.getText().toString().trim().isEmpty()) {
            inputLayoutDeadLine.setError(getString(R.string.err_msg_Deadline));
            requestFocus(dLine);
            return false;
        } else {
            inputLayoutTitle.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_task:
                    validateTitle();
                    break;
                case R.id.deadLine:
                    validateDeadLine();
                    break;

            }
        }
    }

    public void spinner() {
        String[] items = new String[]{
                "Select your task priority",
                "Critical",
                "High",
                "Medium",
                "Low"
        };

        final List<String> priorityList = new ArrayList<>(Arrays.asList(items));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_text, priorityList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    switch (position) {
                        case 1:
                            tv.setTextColor(getResources().getColor(R.color.critical));
                            break;
                        case 2:
                            tv.setTextColor(getResources().getColor(R.color.high));
                            break;
                        case 3:
                            tv.setTextColor(getResources().getColor(R.color.mid));
                            break;
                        case 4:
                            tv.setTextColor(getResources().getColor(R.color.low));
                            break;
                    }
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_text);
        assert priority != null;
        priority.setAdapter(spinnerArrayAdapter);

        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // String selectedItemText = (String) parent.getItemAtPosition(position);

                // If user change the default selection, First item is disable and it is used for hint.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void showDateDialog() {


        dLine.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DatePickerDialog(AssignTaskActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                }
        );
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dLine.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


    String photo;

    public void setPhoto(String photoToUploadURL) {
        photo = photoToUploadURL;

        Picasso.with(AssignTaskActivity.this).load("file:" + photo).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().into(attachmentPhoto);
        attachmentPhoto.setVisibility(View.VISIBLE);

    }

    public void uploadPhoto(String photoToUploadURL, String TaskId) {

        Log.e("uploadPhoto", "uploadPhoto");
        //  photoToUploadURL = photoToUploadURL + ".jpg";
        HttpConnect.postUploadPhoto("http://www.taskhub.tk/semo94/TaskHub/API/uploadTaskAttach.php?=" + TaskId, photoToUploadURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("UploadResponse", response.toString() + " !");

            }
        });
    }
}
