package com.example.saleem.testgithub.listAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.gson.items.ContactsItems;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 4/21/2016.
 */
public class ContactsAdapter extends ArrayAdapter<ContactsItems.MyContactsList> {
    private Context context;
    private List<ContactsItems.MyContactsList> items;


    public ContactsAdapter(Context context, int textViewResourceId,
                           List<ContactsItems.MyContactsList> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    public class viewHolder {
        ImageView Image;
        TextView Name;
        LinearLayout contactLinear;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ContactsItems.MyContactsList getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void restart(List<ContactsItems.MyContactsList> items) {
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
            holder.contactLinear = (LinearLayout) convertView
                    .findViewById(R.id.contactLinear);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        holder.Name.setText(items.get(position).getName() + "");
        holder.Name.setTextColor(context.getResources().getColor(R.color.colorDarkAccent));
        Picasso.with(context).load(items.get(position).getImage()).placeholder(R.drawable.default_profile).error(R.drawable.default_profile).fit().transform(new CircleTransform()).into(holder.Image);

        if (items.get(position).getisSelected()) {
            holder.contactLinear.setBackgroundResource(R.color.colorLightAccent);
        } else {
            holder.contactLinear.setBackgroundResource(R.color.white);
        }
        return (convertView);
    }
}