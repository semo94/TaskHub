package com.example.saleem.testgithub.listAdapters;

/**
 * Created by Mo7ammed on 13/03/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.Country;

import java.util.ArrayList;


public class CountryListAdapter extends BaseAdapter {

    ArrayList<Country> countries;
    LayoutInflater inflater;

    public CountryListAdapter(Context context, ArrayList<Country> countries) {
        super();
        Context context1 = context;
        this.countries = countries;
        inflater = (LayoutInflater) context1
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int arg0) {
        return countries.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * Return row for each country
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.country_row_layout, null);
            cell.nameView = (TextView) cellView.findViewById(R.id.row_title);
            cell.codeView = (TextView) cellView.findViewById(R.id.row_code);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }
        cell.nameView.setText(country.getName());
        cell.codeView.setText("+"+country.getCode());
        return cellView;
    }

    /**
     * Holder for the cell
     *
     */
    static class Cell {
        public TextView nameView;
        public TextView codeView;
    }

}