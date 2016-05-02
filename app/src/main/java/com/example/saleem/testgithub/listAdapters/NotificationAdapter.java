package com.example.saleem.testgithub.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.gson.items.NotificationsItems;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 5/2/2016.
 */
public class NotificationAdapter extends ArrayAdapter<NotificationsItems.AllNotification> {
    private Context context;
    private List<NotificationsItems.AllNotification> items;


    public NotificationAdapter(Context context, int textViewResourceId,
                           List<NotificationsItems.AllNotification> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    public class viewHolder {
        ImageView Image;
        TextView Name;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public NotificationsItems.AllNotification getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void restart(List<NotificationsItems.AllNotification> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.contacts_items, null);

            holder = new viewHolder();
            holder.Image = (ImageView) convertView
                    .findViewById(R.id.icon);
            holder.Name = (TextView) convertView
                    .findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        holder.Name.setText(items.get(position).getBody()+"");
        holder.Name.setTextColor(context.getResources().getColor(R.color.colorDarkAccent));
        Picasso.with(context).load(items.get(position).getImageUrl()).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(holder.Image);

        return (convertView);
    }
}