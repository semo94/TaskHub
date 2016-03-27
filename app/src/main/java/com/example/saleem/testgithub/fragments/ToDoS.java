package com.example.saleem.testgithub.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.saleem.testgithub.listAdapters.ToDoListAdapter;
import com.example.saleem.testgithub.model.Tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ToDoS extends Fragment{
    // Log tag
    private static final String TAG = ToDoS.class.getSimpleName();

    // Tasks json url
    private static final String url = "http://taskhub.net23.net/tasks.json";
    private ProgressDialog pDialog;
    private List<Tasks> tasksList = new ArrayList<Tasks>();
    private ListView listView;
    private TextView noTasks;
    private ToDoListAdapter adapter;


    public ToDoS() {
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
        FrameLayout ToDoLayout = (FrameLayout) inflater.inflate(R.layout.fragment_to_do, container, false);

        noTasks = (TextView) ToDoLayout.findViewById(R.id.no_task);
        listView = (ListView) ToDoLayout.findViewById(R.id.list);



        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        // Creating volley request obj
        JsonArrayRequest toDoReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Tasks task = new Tasks();
                                task.setUserName(obj.getString("name"));
                                task.setTaskTitle(obj.getString("title"));
                                task.setThumbnailUrl(obj.getString("image"));
                                task.setPriorityID(obj.getInt("priority"));

                                // adding task to tasks array
                                tasksList.add(task);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (tasksList.size()!=0)
                        {
                            noTasks.setVisibility(View.GONE);
                            adapter = new ToDoListAdapter(getActivity(), tasksList);
                            listView.setAdapter(adapter);
                        }

                        else
                        noTasks.setVisibility(View.VISIBLE);

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        VolleySkeleton.getInstance().addToRequestQueue(toDoReq);

        return ToDoLayout;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
