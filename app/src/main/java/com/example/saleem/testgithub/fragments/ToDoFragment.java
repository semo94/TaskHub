package com.example.saleem.testgithub.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.MainActivity;
import com.example.saleem.testgithub.activity.PendingActivity;
import com.example.saleem.testgithub.activity.ToDoActivity;

import java.lang.reflect.Field;


public class ToDoFragment extends Fragment implements View.OnClickListener, MainActivity.SearchInterface {


    private RelativeLayout pendingRelative, toDoRelative;
    private Intent intent;
    private Activity activity;

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
                intent = new Intent(activity, ToDoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                break;

            case R.id.pendingRelative:
                intent = new Intent(activity, PendingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
                break;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTyping(String textTyped) {
        Log.d("ToDo Fragment", textTyped);
    }

}