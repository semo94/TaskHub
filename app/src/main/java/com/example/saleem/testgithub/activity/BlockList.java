package com.example.saleem.testgithub.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.Config;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.gson.items.BlockItems;
import com.example.saleem.testgithub.listAdapters.BlockAdapter;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class BlockList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private MaterialMenuDrawable materialMenu;
    private ListView listView;
    private GetResolver getResolver = new GetResolver();
    private Type listType;
    private BlockItems items;
    private BlockAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    int intPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initUI();
        swipeContainer.setRefreshing(true);
        listType = new TypeToken<BlockItems>() {
        }.getType();


        HttpConnect.getData(Config.Get_BlockList, getResolver);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intPosition = position;
                new MaterialDialog.Builder(BlockList.this)
                        .title("Block List")
                        .content("Are you sure you want to Unblock this user?")
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
                                    jsonParams.put("UserID", items.getBlockedList().get(intPosition).getID() + "");

                                } catch (JSONException e) {

                                }
                                StringEntity entity = new StringEntity(jsonParams.toString(), "UTF-8");
                                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                                HttpConnect.postData(Config.Post_UnBlockUser, entity, BlockList.this, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        HttpConnect.getData(Config.Get_BlockList, getResolver);
                                    }
                                });

                            }

                        }).cancelable(false)
                        .show();
            }
        });
    }


    private void initUI() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AboutUs.class, "AboutUs"));
        setContentView(R.layout.block_list);
        initControls();


        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Block List");
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
        listView = (ListView) findViewById(R.id.block_list);
        listView.setVisibility(View.INVISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class GetResolver extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.e("Pending", response.toString() + " Pending response");
            swipeContainer.setRefreshing(false);
            items = (BlockItems) GlobalConstants.gson.fromJson(response.toString(), listType);
            setAdapterList();
        }
    }

    private void setAdapterList() {
        if (adapter == null) {
            adapter = new BlockAdapter(BlockList.this,
                    R.layout.to_dos_list, items.getBlockedList());
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
            YoYo.with(Techniques.SlideInUp).playOn(listView);
        } else {
            listView.setVisibility(View.VISIBLE);
            adapter.restart(items.getBlockedList());
        }
    }


    @Override
    public void onRefresh() {
        HttpConnect.getData(Config.Get_BlockList, getResolver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
