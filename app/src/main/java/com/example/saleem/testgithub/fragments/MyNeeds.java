package com.example.saleem.testgithub.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.MyNeedsItems;
import com.example.saleem.testgithub.listAdapters.MyNeedsListAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;


public class MyNeeds extends Fragment implements DataBaseAble, SwipeRefreshLayout.OnRefreshListener {

    private ApiHelper apiHelper;
    private Type listType;
    private MyNeedsItems items;
    private GetResolver getResolver = new GetResolver();
    private ListView listView;
    private MyNeedsListAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_needs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        swipeContainer.setRefreshing(true);
        GlobalConstants.db.GetCache(Config.Get_MyNeedsList, 0, this, apiHelper.App, apiHelper.Cache);

        HttpConnect.getData(Config.Get_MyNeedsList, getResolver);
    }


    private void initUI(View view) {
        this.apiHelper = new ApiHelper(activity);

        listType = new TypeToken<MyNeedsItems>() {
        }.getType();
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listView = (ListView) view.findViewById(R.id.myNeeds_list);
        listView.setVisibility(View.INVISIBLE);
        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        HttpConnect.getData(Config.Get_MyNeedsList, getResolver);
    }

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("My needs", response.toString() + " My needs response");
            swipeContainer.setRefreshing(false);
            items = (MyNeedsItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
            GlobalConstants.db.SetCache(Config.Get_MyNeedsList, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
        }

    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new MyNeedsListAdapter(activity,
                    R.layout.my_needs_list, items.getMyNeddsList());
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
            YoYo.with(Techniques.SlideInUp).playOn(listView);
        } else {
            listView.setVisibility(View.VISIBLE);
            adapter.restart(items.getMyNeddsList());
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
            items = (MyNeedsItems) GlobalConstants.gson.fromJson(Value, listType);
            setAdapterList();
        }
    }
}