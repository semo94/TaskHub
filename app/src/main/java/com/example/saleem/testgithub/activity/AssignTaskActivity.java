package com.example.saleem.testgithub.activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.saleem.testgithub.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AssignTaskActivity extends AppCompatActivity {

    private EditText dLine, taskTitle,desc;
    private TextInputLayout inputLayoutTitle, inputLayoutDeadLine;
    private ImageView vRec;
    private Button assign;
    private Spinner priority;
    private int spnItem;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vRec = (ImageView) findViewById(R.id.voice_rec_icon);
        assign = (Button) findViewById(R.id.btn_assignTask);
        desc = (EditText) findViewById(R.id.input_des);
        inputLayoutDeadLine = (TextInputLayout) findViewById(R.id.input_layout_cal);
        inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_task);
        dLine = (EditText)findViewById(R.id.deadLine);
        taskTitle = (EditText) findViewById(R.id.input_task);
        priority = (Spinner) findViewById(R.id.priority_level);


        taskTitle.addTextChangedListener(new MyTextWatcher(taskTitle));
        dLine.addTextChangedListener(new MyTextWatcher(dLine));







        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        vRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        //date setup
        showDateDialog();

        //spinner setup
        spinner();
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
    public void spinner(){
        String[] items = new String []{
                "Select your task priority",
                "Critical",
                "High",
                "Medium",
                "Low"
        };

        final List<String > priorityList = new ArrayList<>(Arrays.asList(items));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_text,priorityList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    switch(position){
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

    public void showDateDialog(){


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


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    desc.setText(result.get(0));
                }
                break;
            }

        }
    }


}
