package com.example.saleem.testgithub.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.app.VolleySkeleton;
import com.example.saleem.testgithub.helper.Country;
import com.example.saleem.testgithub.helper.PrefManager;
import com.example.saleem.testgithub.service.HttpService;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static String TAG = RegActivity.class.getSimpleName();
    private ViewPager viewPager;

    private EditText inputMobile, inputOtp,countryCodeInput;
    private ProgressBar progressBar;
    private PrefManager pref;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    private Spinner countrySpinner;

    private Map<String,Country> codeCountryMap = new HashMap<>();
    private Country currentCountry;
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        viewPager                   = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputMobile                 = (EditText) findViewById(R.id.inputMobile);
        inputOtp                    = (EditText) findViewById(R.id.inputOtp);
        countryCodeInput            = (EditText) findViewById(R.id.countryCode);
        Button btnRequestSms        = (Button) findViewById(R.id.btn_request_sms);
        Button btnVerifyOtp         = (Button) findViewById(R.id.btn_verify_otp);
        progressBar                 = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton btnEditMobile   = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile               = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile            = (LinearLayout) findViewById(R.id.layout_edit_mobile);
        countrySpinner              = (Spinner) findViewById(R.id.countrySpinner);

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        countrySpinner.setOnTouchListener(this);

        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = new PrefManager(this);

        // Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(RegActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }

        //get user country name and add it to country
        setUserCountry();



        //make mobile editText take the focus (cursor)
        inputMobile.setFocusableInTouchMode(true);
        inputMobile.requestFocus();


        prepareMap();

        countryCodeInput.addTextChangedListener(countryCodeTextWatcher);
    }

    private void prepareMap() {
        try {
            ArrayList<Country> countries = Country.countriesList(getApplicationContext());


            for (Country country:countries){
                codeCountryMap.put(country.getCode(),country);
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;
        }
    }


    /**
     * Validating user details form
     */
    private void validateForm() {
        Phonenumber.PhoneNumber number = isValidPhoneNumber();
        // validating mobile number
        if (number != null) {

            String E164PhoneNumber =   phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

            // request for sms
            progressBar.setVisibility(View.VISIBLE);

            // saving the mobile number in shared preferences
            pref.setMobileNumber(E164PhoneNumber);

            // requesting for sms
            requestForSMS(E164PhoneNumber);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method initiates the SMS request on the server
     *
     * @param mobile user valid mobile number
     */
    private void requestForSMS(final String mobile) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_REQUEST_SMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
//                    JSONObject responseObj = new JSONArray(response).getJSONObject(0);
                   JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        // boolean flag saying device is waiting for sms
                        pref.setIsWaitingForSms(true);

                        // moving the screen to next pager item i.e otp screen
                        viewPager.setCurrentItem(1);
                        txtEditMobile.setText(pref.getMobileNumber());
                        layoutEditMobile.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "error=true");
                    }

                    // hiding the progress bar
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "catch e");

                             progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);

                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        VolleySkeleton.getInstance().addToRequestQueue(strReq);
    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     */
    private Phonenumber.PhoneNumber isValidPhoneNumber() {
        try
        {
            String mobile   = inputMobile.getText().toString().trim();
            String code     = countryCodeInput.getText().toString().trim();
            Phonenumber.PhoneNumber number = phoneUtil.parse(mobile, ((Country)codeCountryMap.get(code)).getIso().toUpperCase());
            phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
            if (phoneUtil.isValidNumberForRegion(number, currentCountry.getIso().toUpperCase())){
                return number;
            }else {
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG,"OnTouchSpinner");
            startActivityForResult(new Intent(this, CountryPicker.class),CountryPicker.PICK_COUNTRY_REQ);
        }
        return false;
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CountryPicker.PICK_COUNTRY_REQ){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    currentCountry = bundle.getParcelable(CountryPicker.SELECTED_COUNTRY);
                    displayCountryInfo(currentCountry);
                    inputMobile.requestFocus();
                }
            }
        }
    }



    private void displayCountryInfo(Country selectedCountry) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.country_spinner_item, new String[]{selectedCountry.getName()}); //selected item will look like a spinner set from XML
        countrySpinner.setAdapter(spinnerArrayAdapter);
        countryCodeInput.setText(selectedCountry.getCode());
    }


    private void setUserCountry() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryISO =  telephonyManager.getSimCountryIso().toUpperCase();
        Locale local = new Locale("",countryISO);
        String countryNameString = local.getDisplayCountry();
        String countryCode = String.valueOf(phoneUtil.getCountryCodeForRegion(countryISO.toUpperCase()));
        currentCountry = new Country(countryISO,countryNameString,countryCode);
        displayCountryInfo(currentCountry);
    }

    private TextWatcher countryCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = s.toString();
            Country country = codeCountryMap.get(code);
            if (country != null){
                String countryName = country.getName();
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.country_spinner_item, new String[]{countryName}); //selected item will look like a spinner set from XML
                countrySpinner.setAdapter(spinnerArrayAdapter);
            }else{
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.country_spinner_item, new String[]{"Invalid Code"}); //selected item will look like a spinner set from XML
                countrySpinner.setAdapter(spinnerArrayAdapter);
            }
        }
    };

}
