package com.example.saleem.testgithub.activity;

/**
 * Created by Mo7ammed on 13/03/2016.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.Country;
import com.example.saleem.testgithub.listAdapters.CountryListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CountryPicker extends AppCompatActivity implements Comparator<Country> {

    public static final int    PICK_COUNTRY_REQ = 1;
    public static final String SELECTED_COUNTRY  = "selected_country";


    private List<Country> allCountriesList;
    private ArrayList<Country> searchedCounters ;

    private CountryListAdapter countryListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.country_picker_layout);


        ListView countryListView = (ListView) findViewById(R.id.country_picker_listview);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchedCounters    = new ArrayList<>();
        try {
            allCountriesList = Country.countriesList(getApplicationContext());
            searchedCounters = new ArrayList<>(allCountriesList);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }


        countryListAdapter = new CountryListAdapter(this,searchedCounters);
        countryListView.setAdapter(countryListAdapter);
        countryListView.setOnItemClickListener(onItemClickListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int compare(Country country1, Country country2) {
        return country1.getName().compareTo(country2.getName());
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent result = new Intent();
            result.putExtra(SELECTED_COUNTRY,(Country) adapterView.getItemAtPosition(i));
            setResult(RESULT_OK, result);
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_picker_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.country_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                searchedCounters.clear();
                for (Country country : allCountriesList) {
                    if (country.getName().toLowerCase().contains(newText.toLowerCase())) {
                        searchedCounters.add(country);
                    }
                }
                countryListAdapter.notifyDataSetChanged();
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}