package com.example.saleem.testgithub.listAdapters;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.app.VolleySkeleton;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.model.Tasks;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ToDoListAdapter extends BaseAdapter {
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private List<Tasks> tasksItems;

    //private List<Users> usersItems;
    ImageLoader imageLoader = VolleySkeleton.getInstance().getImageLoader();

    public ToDoListAdapter(FragmentActivity activity, List<Tasks> tasksItems) {
        this.activity = activity;
        this.tasksItems = tasksItems;
        //this.usersItems = usersItems;
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
            convertView = inflater.inflate(R.layout.to_dos_list, null);

        CircularImageView thumbNail = (CircularImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView name= (TextView) convertView.findViewById(R.id.user_name);
        TextView taskSubject = (TextView) convertView.findViewById(R.id.task_title);
        ImageView priority_photo = (ImageView) convertView.findViewById(R.id.priority_photo);

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

        // priority label

        switch (t.getPriorityID()){
            case 0: priority_photo.setImageResource(R.drawable.low);
                break;
            case 1: priority_photo.setImageResource(R.drawable.mid);
                break;
            case 2: priority_photo.setImageResource(R.drawable.high);
                break;
            case 3: priority_photo.setImageResource(R.drawable.critical);

        }



        return convertView;
    }

}