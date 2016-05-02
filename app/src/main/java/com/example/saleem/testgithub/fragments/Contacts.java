package com.example.saleem.testgithub.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.AssignTaskActivity;
import com.example.saleem.testgithub.activity.BlockList;
import com.example.saleem.testgithub.activity.UserInfoActivity;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.listAdapters.ContactsAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class Contacts extends Fragment implements DataBaseAble, SwipeRefreshLayout.OnRefreshListener {

    private ApiHelper apiHelper;
    private ListView lstContacts;
    private Type listType;
    private ContactsItems items;
    private ContactsAdapter adapter;
    private GetResolver getResolver = new GetResolver();
    private Activity activity;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;

    int intPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        swipeContainer.setRefreshing(true);
        GlobalConstants.db.GetCache(Config.GetMyContactsList, 0, this, apiHelper.App, apiHelper.Cache);

        HttpConnect.getData(Config.GetMyContactsList, getResolver);

        lstContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                   @Override
                                                   public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                       intPosition = position;
                                                       new MaterialDialog.Builder(activity)
                                                               .title("Block List")
                                                               .content("Are you sure you want to block this user?")
                                                               .contentColor(Color.parseColor("#2a2a2a"))
                                                               .titleColor(Color.parseColor("#2a2a2a"))
                                                               .positiveColor(Color.parseColor("#2a2a2a"))
                                                               .negativeColor(Color.parseColor("#2a2a2a"))
                                                               .positiveText("Yes")
                                                               .negativeText("No")
                                                               .callback(new MaterialDialog.ButtonCallback() {
                                                                   @Override
                                                                   public void onPositive(MaterialDialog dialog) {

                                                                       JSONObject jsonParams = new JSONObject();
                                                                       try {
                                                                           jsonParams.put("UserID", items.getMyContactsList().get(intPosition).getId() + "");

                                                                       } catch (JSONException e) {

                                                                       }
                                                                       StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
                                                                       entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                                                                       HttpConnect.postData(Config.Post_BlockUser, entity, activity, new JsonHttpResponseHandler() {
                                                                           @Override
                                                                           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                               super.onSuccess(statusCode, headers, response);
                                                                               HttpConnect.getData(Config.GetMyContactsList, getResolver);
                                                                           }
                                                                       });

                                                                   }

                                                               }).cancelable(false)
                                                               .show();
                                                       return false;
                                                   }
                                               }
        );
    }

    private void initUI(View view) {
        this.apiHelper = new ApiHelper(activity);

        listType = new TypeToken<ContactsItems>() {
        }.getType();

        lstContacts = (ListView) view.findViewById(R.id.contacts_list);
        lstContacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Floating", "Assign");
                Intent intent = new Intent(activity, AssignTaskActivity.class);
                activity.startActivity(intent);

            }
        });
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
            items = (ContactsItems) GlobalConstants.gson.fromJson(Value, listType);
            setAdapterList();
        }
    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new ContactsAdapter(activity,
                    R.layout.contacts_items, items.getMyContactsList());
            lstContacts.setAdapter(adapter);
        } else {
            adapter.restart(items.getMyContactsList());
        }
    }


    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("Contacts", response.toString() + " Contacts response");
            swipeContainer.setRefreshing(false);
            items = (ContactsItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
            GlobalConstants.db.SetCache(Config.GetMyContactsList, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
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
    public void onRefresh() {
        HttpConnect.getData(Config.GetMyContactsList, getResolver);
    }


}