package com.example.saleem.testgithub.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.saleem.testgithub.R;


public class ToDoFragment extends Fragment implements View.OnClickListener {


    private RelativeLayout pendingRelative, toDoRelative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.to_do_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        pendingRelative.setOnClickListener(ToDoFragment.this);
        toDoRelative.setOnClickListener(ToDoFragment.this);
    }

    private void initUI(View view) {
        pendingRelative = (RelativeLayout) view.findViewById(R.id.pendingRelative);
        toDoRelative = (RelativeLayout) view.findViewById(R.id.toDoRelative);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toDoRelative:
                Log.e("toDoRelative","toDoRelative");
                break;

            case R.id.pendingRelative:
                Log.e("pendingRelative","pendingRelative");
                break;
        }
    }
}