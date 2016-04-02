package com.example.saleem.testgithub.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.VolleySkeleton;
import com.example.saleem.testgithub.listAdapters.MyNeedsListAdapter;
import com.example.saleem.testgithub.model.Tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyNeeds extends Fragment {

    // Log tag
    private static final String TAG = ToDoS.class.getSimpleName();

    // Tasks json url
    private static final String url = "http://www.taskhub.tk/semo94/TaskHub/JSON/myneeds.json";
    private ProgressDialog pDialog;
    private List<Tasks> needsList = new ArrayList<Tasks>();
    private ListView listView;
    private TextView noNeeds;
    private MyNeedsListAdapter adapter;
    private SwipeRefreshLayout swipeContainer;


    public MyNeeds() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout MyNeedsLayout = (FrameLayout) inflater.inflate(R.layout.fragment_my_needs, container, false);
        swipeContainer = (SwipeRefreshLayout) MyNeedsLayout.findViewById(R.id.swipeContainer);
        noNeeds = (TextView) MyNeedsLayout.findViewById(R.id.no_needs);
        listView = (ListView) MyNeedsLayout.findViewById(R.id.myNeeds_list);




        fetchMyNeeds();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchMyNeeds();
                swipeContainer.setRefreshing(false);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorAccent);



        return MyNeedsLayout;
    }

    public void fetchMyNeeds () {

        // Creating volley request obj
        JsonArrayRequest myNeedReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        needsList.clear();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Tasks task = new Tasks();
                                task.setUserName(obj.getString("name"));
                                task.setTaskTitle(obj.getString("title"));
                                task.setThumbnailUrl(obj.getString("image"));
                                task.setStatus(obj.getString("status"));

                                // adding task to tasks array
                                needsList.add(task);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        if (needsList.size()!=0)
                        {
                            noNeeds.setVisibility(View.GONE);
                            adapter = new MyNeedsListAdapter(getActivity(), needsList);
                            listView.setAdapter(adapter);

                        }

                        else
                            noNeeds.setVisibility(View.VISIBLE);

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        VolleySkeleton.getInstance().addToRequestQueue(myNeedReq);


    }


}