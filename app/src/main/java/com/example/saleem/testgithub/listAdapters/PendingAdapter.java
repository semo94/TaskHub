package com.example.saleem.testgithub.listAdapters;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.gson.items.PendingItems;
import com.example.saleem.testgithub.helper.CircularImageView;
import com.example.saleem.testgithub.model.Tasks;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingAdapter extends ArrayAdapter<PendingItems.PendingToDoList> {
    private Context context;
    private List<PendingItems.PendingToDoList> items;

    public PendingAdapter(Context context, int textViewResourceId,
                          List<PendingItems.PendingToDoList> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    public class viewHolder {
        ImageView priority_photo, thumbNail;
        TextView name, taskSubject;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PendingItems.PendingToDoList getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void restart(List<PendingItems.PendingToDoList> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.to_dos_list, null);

            holder = new viewHolder();
            holder.thumbNail = (ImageView) convertView
                    .findViewById(R.id.senderThumbnail);
            holder.name = (TextView) convertView.findViewById(R.id.sender_name);
            holder.taskSubject = (TextView) convertView.findViewById(R.id.toDo_title);
            holder.priority_photo = (ImageView) convertView.findViewById(R.id.priority_photo);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        //thumbNail.setImageUrl(t.getThumbnailUrl(), imageLoader);
        Picasso.with(context).load(items.get(position).getImageUrl()).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(holder.thumbNail);

        // task owner name
        holder.name.setText(items.get(position).getUserName()+"");

        // task title
        holder.taskSubject.setText(items.get(position).getTitle()+"");

        // priority label

        switch (items.get(position).getPriorityId()) {
            case 0:
                holder.priority_photo.setImageResource(R.drawable.low);
                break;
            case 1:
                holder.priority_photo.setImageResource(R.drawable.mid);
                break;
            case 2:
                holder.priority_photo.setImageResource(R.drawable.high);
                break;
            case 3:
                holder.priority_photo.setImageResource(R.drawable.critical);

        }


        return convertView;
    }

}