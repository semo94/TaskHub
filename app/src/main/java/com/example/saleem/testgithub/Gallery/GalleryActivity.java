package com.example.saleem.testgithub.Gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.AssignTaskActivity;
import com.example.saleem.testgithub.activity.Profile;
import com.example.saleem.testgithub.activity.UserInfoActivity;
import com.example.saleem.testgithub.utils.CircleTransform;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.MyExceptionHandler;
import com.example.saleem.testgithub.utils.PublicMethods;
import com.squareup.picasso.Picasso;


public class GalleryActivity extends AppCompatActivity {

    private GridView mGalleryGridView;
    private ImageGalleryAdapter mGalleryAdapter;
    private RelativeLayout sendRelative, cancelRelative;
    private TextView sendText, cancelText;
    private ImageView sendImage, cancelImage;
    private String typeOfUpload = "";
    private Image mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, GalleryActivity.class, "GalleryActivity"));
        setContentView(R.layout.fragment_gallery);


        Intent intent = getIntent();
        typeOfUpload = intent.getStringExtra("typeOfUpload");

        mGalleryAdapter = new ImageGalleryAdapter(GalleryActivity.this);
        mGalleryGridView = (GridView) findViewById(R.id.gallery_grid);
        sendRelative = (RelativeLayout)
                findViewById(R.id.sendRelative);
        cancelRelative = (RelativeLayout)
                findViewById(R.id.cancelRelative);
        sendText = (TextView) findViewById(R.id.sendTextView);
        cancelText = (TextView) findViewById(R.id.cancelText);
        sendImage = (ImageView) findViewById(R.id.sendImageView);
        cancelImage = (ImageView) findViewById(R.id.cancelImage);

        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION};
        final String orderBy = MediaStore.Images.Media._ID + " DESC";

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        while (imageCursor.moveToNext()) {
            Uri uri = Uri.parse(imageCursor.getString(imageCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA)));
            int orientation = imageCursor
                    .getInt(imageCursor
                            .getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            mGalleryAdapter.add(new Image(uri, orientation, 0));
        }
        imageCursor.close();

        mGalleryGridView.setAdapter(mGalleryAdapter);
        mGalleryGridView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {

                        for (int s = 0; s < mGalleryAdapter.getCount(); s++) {
                            Image image1 = mGalleryAdapter.getItem(s);
                            image1.isSend = 0;
                        }
                        Image image = mGalleryAdapter.getItem(i);
                        image.isSend = 1;

                        mainImage = image;

                        mGalleryAdapter.restart();
                    }
                });


        cancelRelative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        sendRelative.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mainImage != null) {
                    if (typeOfUpload.equals("profile")) {
                        Profile.profile.uploadPhoto(mainImage.mUri
                                .toString());

                    } else if (typeOfUpload.equals("Task")) {
                        AssignTaskActivity.assignTaskActivity.setPhoto(mainImage.mUri.toString());

                    } else if (typeOfUpload.equals("UserInfo")) {
                        UserInfoActivity.userInfoActivity.setPhoto(mainImage.mUri.toString());
                    }
                    back();
                }
            }
        });

    }


    class ViewHolder {
        ImageView mThumbnail, check;
    }

    private LayoutInflater inflater;

    public class ImageGalleryAdapter extends ArrayAdapter<Image> {

        Context context;

        public ImageGalleryAdapter(Context context) {
            super(context, 0);
            this.context = context;
        }

        Image image;

        public void restart() {

            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            image = getItem(position);
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(
                        R.layout.grid_item_gallery_thumbnail, null);

                holder = new ViewHolder();
                holder.mThumbnail = (ImageView) convertView
                        .findViewById(R.id.thumbnail_image);
                holder.check = (ImageView) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // "file://"


            Log.e("image.mUri", "file:" + image.mUri + "");
            Picasso.with(GalleryActivity.this).load("file:" + image.mUri).fit().into(holder.mThumbnail);

            if (image.isSend == 0) {
                PublicMethods.ChangeIconColor(holder.check, context
                                .getResources().getDrawable(R.drawable.enable_gray),
                        Color.GRAY);
            } else {
                PublicMethods.ChangeIconColor(holder.check, context
                                .getResources().getDrawable(R.drawable.enable_gray),
                        Color.GREEN);
            }

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        super.onBackPressed();
    }
}