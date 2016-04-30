package com.example.saleem.testgithub.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.AssignTaskActivity;
import com.example.saleem.testgithub.activity.MainActivity;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.listAdapters.ContactsAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class Contacts extends Fragment implements DataBaseAble, SwipeRefreshLayout.OnRefreshListener, MainActivity.SearchInterface {

    private ApiHelper apiHelper;
    private ListView lstContacts;
    private Type listType;
    private ContactsItems items;
    private ContactsItems searchedItems;
    private ContactsAdapter adapter;
    private GetResolver getResolver = new GetResolver();
    private Activity activity;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;

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
    }

    private void initUI(View view) {
        this.apiHelper = new ApiHelper(activity);

        listType = new TypeToken<ContactsItems>() {
        }.getType();

        lstContacts = (ListView) view.findViewById(R.id.contacts_list);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AssignTaskActivity.class);
                startActivity(intent);
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
            items = GlobalConstants.gson.fromJson(Value, listType);
            searchedItems = GlobalConstants.gson.fromJson(Value, listType);
            setAdapterList();
        }
    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new ContactsAdapter(activity, R.layout.contacts_items, searchedItems.getMyContactsList());
            lstContacts.setAdapter(adapter);
        } else {
            adapter.restart(searchedItems.getMyContactsList());
        }
    }

    @Override
    public void onTyping(String textTyped) {
        Log.d("Contacts Fragment", textTyped);
        searchedItems.getMyContactsList().clear();
        List<ContactsItems.MyContactsList> myContactsLists = new ArrayList<>();
        for (ContactsItems.MyContactsList myContactsList : items.getMyContactsList()) {
            try {
                if (myContactsList.getName().toLowerCase().contains(textTyped.toLowerCase())) {
                    myContactsLists.add(myContactsList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        searchedItems.setMyContactsList(myContactsLists);
        setAdapterList();
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

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("Contacts", response.toString() + " Contacts response");
            swipeContainer.setRefreshing(false);
            items = GlobalConstants.gson.fromJson(response.toString(), listType);
            searchedItems = GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
            GlobalConstants.db.SetCache(Config.GetMyContactsList, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
        }

    }


}