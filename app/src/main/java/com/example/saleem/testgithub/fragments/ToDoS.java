package com.example.saleem.testgithub.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.saleem.testgithub.R;

import com.example.saleem.testgithub.app.VolleySkeleton;
import com.example.saleem.testgithub.listAdapters.NewToDoListAdapter;
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
    private static final String url = "http://www.taskhub.tk/semo94/TaskHub/JSON/todos.json";
    private ProgressDialog pDialog;
    private List<Tasks> toDosList = new ArrayList<>();
    private List<Tasks> newToDoList = new ArrayList<>();
    private ListView underProgressListView, pendingListView;
    private TextView noTasks;
    private NewToDoListAdapter newAdapter;
    static public ToDoListAdapter adapter;


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
        View ToDoLayout = inflater.inflate(R.layout.fragment_to_do, container, false);

        noTasks = (TextView) ToDoLayout.findViewById(R.id.no_task);
        underProgressListView = (ListView) ToDoLayout.findViewById(R.id.toDo_list);
        pendingListView = (ListView) ToDoLayout.findViewById(R.id.newToDo_list);


            fetchUnderProgressToDos();



        return ToDoLayout;
    }



    public void fetchInitiateToDos(){
    }


    public void fetchUnderProgressToDos(){

        // Creating volley request obj
        JsonArrayRequest toDoReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Tasks task = new Tasks();
                                task.setUserName(obj.getString("name"));
                                task.setTaskTitle(obj.getString("title"));
                                task.setThumbnailUrl(obj.getString("image"));


                                // adding task to tasks array
                                if (obj.getString("status").equalsIgnoreCase("pending")){

                                    task.setStatus(obj.getString("status"));
                                    newToDoList.add(task);
                                }
                                else {
                                    task.setPriorityID(obj.getInt("priority"));
                                    toDosList.add(task);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (toDosList.size() !=0 || newToDoList.size()!=0)
                        {
                            noTasks.setVisibility(View.GONE);

                            adapter = new ToDoListAdapter(getActivity(), toDosList);
                            underProgressListView.setAdapter(adapter);

                            newAdapter = new NewToDoListAdapter(getActivity(), newToDoList);
                            pendingListView.setAdapter(newAdapter);
                        }

                        else
                            noTasks.setVisibility(View.VISIBLE);

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                        newAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        VolleySkeleton.getInstance().addToRequestQueue(toDoReq);

    }





}
