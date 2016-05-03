package com.example.saleem.testgithub.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.fragments.Contacts;
import com.example.saleem.testgithub.fragments.MyNeeds;
import com.example.saleem.testgithub.fragments.ToDoFragment;
import com.example.saleem.testgithub.fragments.ViewPagerAdapter;
import com.example.saleem.testgithub.gcm.connection.HttpConnect;
import com.example.saleem.testgithub.utils.BadgeView;
import com.example.saleem.testgithub.utils.DrawerInit;
import com.example.saleem.testgithub.utils.GlobalConstants;
import com.example.saleem.testgithub.utils.PrefManager;
import com.example.saleem.testgithub.utils.UserContacts;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x11;
    String[] permissions = {"android.permission.READ_CONTACTS"};
    int Count;
    private PrefManager pref;
    private MaterialMenuDrawable materialMenu;
    private Toolbar toolbar;
    // without sdk version check
    private Drawer drawer;
    private ImageView notification;
    private BadgeView badgeViewNotification;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                UserContacts.getContactList(MainActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        notification = (ImageView) findViewById(R.id.notification);
        setSupportActionBar(toolbar);

        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);

        toolbar.setNavigationIcon(materialMenu);
        this.getSupportActionBar().setTitle("Task Hub");
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        drawer = new DrawerInit(drawer, 0).initDrawer(this, toolbar, materialMenu);

        // Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        // Checking if user session
        // if not logged in, take user to sms screen
        pref = new PrefManager(getApplicationContext());
       /* if (!pref.isLoggedIn()) {
            logout();
        } else {
            HashMap<String, String> userDetails = pref.getUserDetails();
            GlobalConstants.UserID = userDetails.get("id");
            /// Mobile = userDetails.get("mobile");
        } */

        GlobalConstants.UserID = "337";
        Log.e("UserId", GlobalConstants.UserID + " !");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(permissions, REQUEST_CODE);
        } else {
            UserContacts.getContactList(MainActivity.this);
        }

        // Run a task to fetch the notifications count
        //updateNotificationsBadge(mNotificationsCount);


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Count = 0;
                if (badgeViewNotification != null) {
                    badgeViewNotification.hide(true);
                    badgeViewNotification.setVisibility(View.GONE);
                }
                Intent myIntent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyNeeds(), "My Needs");
        adapter.addFragment(new ToDoFragment(), "To Do's");
        adapter.addFragment(new Contacts(), "Contacts");
        viewPager.setAdapter(adapter);
    }

    /**
     * Logging out user
     * will clear all user shared preferences and navigate to
     * sms activation screen
     */
    private void logout() {
        pref.clearSession();

        Intent intent = new Intent(MainActivity.this, RegActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }

    ////    @Override
////
////    // Inflate the menu; this adds items to the action bar if it is present.
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_main, menu);
////
////
////        // Associate searchable configuration with the SearchView
////        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        MenuItem searchItem = menu.findItem(R.id.menu_search);
////        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
////        searchView.setIconifiedByDefault(false);
////
////        // Get the notifications MenuItem and
////        // its LayerDrawable (layer-list)
////        MenuItem item = menu.findItem(R.id.notifications);
////        LayerDrawable icon = (LayerDrawable) item.getIcon();
////
////        // Update LayerDrawable's BadgeDrawable
////        Utils.setBadgeCount(this, icon, mNotificationsCount);
////
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        if (item.getItemId() == R.id.notifications) {
////            // TODO: display unread notifications.
////            return true;
////        }
////
////        if (id == R.id.action_deActivate) {
////            logout();
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//
//
//Updates the count of notifications in the ActionBar.
//    private void updateNotificationsBadge(int count) {
//        mNotificationsCount = count;
//
//        // force the ActionBar to relayout its MenuItems.
//        // onCreateOptionsMenu(Menu) will be called again.
//        invalidateOptionsMenu();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                onTyping(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;


            }
        };
        searchView.setOnQueryTextListener(textChangeListener);


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        HttpConnect.getData("http://www.taskhub.tk/semo94/TaskHub/API/CountOfNotif.php", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (response.has("NotificationsCont")) {
                        Count = response.getInt("NotificationsCont");
                        if (Count > 0) {
                            badgeViewNotification = new BadgeView(MainActivity.this, notification);
                            badgeViewNotification.setText(Count + "");
                            badgeViewNotification.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
                            badgeViewNotification.show(true);
                            badgeViewNotification.setVisibility(View.VISIBLE);
                        } else {
                            Count = 0;
                            if (badgeViewNotification != null) {
                                //badgeViewNotification.hide(true);
                                badgeViewNotification.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {

                }
            }
        });

        if (drawer != null) {
            drawer.setSelection(0, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    void onTyping(String textTyped) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        int index = viewPager.getCurrentItem();
        ViewPagerAdapter adapter = ((ViewPagerAdapter) viewPager.getAdapter());
        SearchInterface searchInterface = (SearchInterface) adapter.getItem(index);
        searchInterface.onTyping(textTyped);
    }

    public interface SearchInterface {
        void onTyping(String textTyped);
    }
}
