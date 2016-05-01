package com.example.saleem.testgithub.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.database.ApiHelper;
import com.example.saleem.testgithub.database.DataBaseAble;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.PendingItems;
import com.example.saleem.testgithub.listAdapters.PendingAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ToDoActivity extends AppCompatActivity implements DataBaseAble, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ListView listView;
    private GetResolver getResolver = new GetResolver();
    private Type listType;
    private PendingItems items;
    private PendingItems searchedItems;
    private PendingAdapter adapter;
    private ApiHelper apiHelper;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();

        swipeContainer.setRefreshing(true);
        listType = new TypeToken<PendingItems>() {
        }.getType();
        this.apiHelper = new ApiHelper(ToDoActivity.this);
        GlobalConstants.db.GetCache(Config.Get_UnderProgressList, 0, this, apiHelper.App, apiHelper.Cache);
        HttpConnect.getData(Config.Get_UnderProgressList, getResolver);
    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AboutUs.class, "AboutUs"));
        setContentView(R.layout.to_do_activity);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("To Do Tasks");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                finish();
            }
        });
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitleTextColor(Color.WHITE);
        materialMenu.setColor(Color.WHITE);
    }

    private void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.to_do_list);
        listView.setVisibility(View.INVISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new PendingAdapter(ToDoActivity.this,
                    R.layout.to_dos_list, searchedItems.getPendingToDoList());
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
            YoYo.with(Techniques.SlideInUp).playOn(listView);
        } else {
            listView.setVisibility(View.VISIBLE);
            adapter.restart(searchedItems.getPendingToDoList());
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
            items = GlobalConstants.gson.fromJson(Value, listType);
            searchedItems = GlobalConstants.gson.fromJson(Value, listType);
            setAdapterList();
        }
    }

    @Override
    public void onRefresh() {
        HttpConnect.getData(Config.Get_UnderProgressList, getResolver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_picker_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.country_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                searchedItems.getPendingToDoList().clear();
                ArrayList<PendingItems.PendingToDoList> pendingToDoLists = new ArrayList<>();
                for (PendingItems.PendingToDoList pendingToDoList : items.getPendingToDoList()) {
                    try {
                        if (pendingToDoList.getUserName().toLowerCase().contains(newText.toLowerCase())) {
                            pendingToDoLists.add(pendingToDoList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                searchedItems.setPendingToDoList(pendingToDoLists);
                setAdapterList();
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

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("ToDO", response.toString() + " ToDO response");
            swipeContainer.setRefreshing(false);
            items = GlobalConstants.gson.fromJson(response.toString(), listType);
            searchedItems = GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
            GlobalConstants.db.SetCache(Config.Get_UnderProgressList, response.toString(), 0, null, apiHelper.App, apiHelper.Cache);
        }

    }
}
