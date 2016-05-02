package com.example.saleem.testgithub.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.example.saleem.testgithub.R;
import com.example.saleem.testgithub.activity.AboutUs;
import com.example.saleem.testgithub.activity.MainActivity;
import com.example.saleem.testgithub.activity.Profile;
import com.example.saleem.testgithub.activity.Settings;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class DrawerInit {
    private Drawer drawer;
    private int selectedItem;

    public DrawerInit(Drawer drawer, int selectedItem) {
        this.selectedItem = selectedItem;
        this.drawer = drawer;
    }

    public Drawer initDrawer(final Activity activity, final Toolbar toolbar, final MaterialMenuDrawable materialMenu) {

        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                 .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        home.withSetSelected(true), profile, aboutUs, sittings
                ).withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (position) {
                            case 0:
                                myIntent = new Intent(activity, MainActivity.class);
                                break;
                            case 1:
                                myIntent = new Intent(activity, Profile.class);
                                break;
                            case 2:
                                myIntent = new Intent(activity, AboutUs.class);
                                break;

                            case 3:
                                myIntent = new Intent(activity, Settings.class);
                                break;

                        }

                        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        if (position == 0) {
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        activity.startActivity(myIntent);

                        return false;
                    }
                }).withSelectedItem(selectedItem)
                .build();
        drawer.getDrawerLayout().setDrawerShadow(null, GravityCompat.START);
        return drawer;
    }

    private Intent myIntent;
    private PrimaryDrawerItem home = new PrimaryDrawerItem().withName("Home").withIdentifier(0).withSelectedColorRes(R.color.material_drawer_background).withIcon(R.drawable.home).withTextColor(Color.parseColor("#FFFFFF")).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.material_drawer_selected_text).withIconColorRes(R.color.white);
    private PrimaryDrawerItem profile = new PrimaryDrawerItem().withName("Profile").withIdentifier(1).withSelectedColorRes(R.color.material_drawer_background).withIcon(R.drawable.profile).withTextColor(Color.parseColor("#FFFFFF")).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.material_drawer_selected_text).withIconColorRes(R.color.white);
    private PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().withName("About Us").withIdentifier(2).withSelectedColorRes(R.color.material_drawer_background).withIcon(R.drawable.about_us).withTextColor(Color.parseColor("#FFFFFF")).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.material_drawer_selected_text).withIconColorRes(R.color.white);
    private PrimaryDrawerItem sittings = new PrimaryDrawerItem().withName("Sittings").withIdentifier(3).withSelectedColorRes(R.color.material_drawer_background).withIcon(R.drawable.settings).withTextColor(Color.parseColor("#FFFFFF")).withIconTintingEnabled(true).withSelectedIconColorRes(R.color.material_drawer_selected_text).withIconColorRes(R.color.white);

}
