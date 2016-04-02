package com.example.saleem.testgithub.listAdapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.model.Tasks;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewToDoListAdapter extends BaseAdapter  {

    private FragmentActivity activity;
    private LayoutInflater inflater;
    private List<Tasks> tasksItems;

    public NewToDoListAdapter(FragmentActivity activity, List<Tasks> tasksItems) {
        this.activity = activity;
        this.tasksItems = tasksItems;
    }

    @Override
    public int getCount() {
        return tasksItems.size();
    }

    @Override
    public Object getItem(int location) {
        return tasksItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.new_to_dos_list, null);
        


        CircularImageView thumbNail = (CircularImageView) convertView
                .findViewById(R.id.newSenderThumbnail);
        TextView name= (TextView) convertView.findViewById(R.id.newSender_name);
        TextView taskSubject = (TextView) convertView.findViewById(R.id.newToDo_title);
        TextView initiateStatus = (TextView) convertView.findViewById(R.id.initiate_task_status);

        // getting list data for the row
        Tasks t = tasksItems.get(position);
        //Users u = usersItems.get(position);

        // thumbnail image

        //thumbNail.setImageUrl(t.getThumbnailUrl(), imageLoader);
        Picasso.with(activity).load(t.getThumbnailUrl()).placeholder(activity.getResources().getDrawable(R.drawable.default_profile)).error(activity.getResources().getDrawable(R.drawable.default_profile)).into(thumbNail);

        // task owner name
        name.setText(t.getUserName());

        // task title
        taskSubject.setText(t.getTaskTitle());

        // initiate status

        initiateStatus.setText(t.getStatus());
        initiateStatus.setTextColor(Color.rgb(244, 67, 54));


        return convertView;
    }



}
