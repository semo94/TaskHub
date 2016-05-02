package com.example.saleem.testgithub.utils;

import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by admin on 5/1/2016.
 */
public class PublicMethods  {

    public static void ChangeIconColor(ImageView imageview, Drawable myIcon,
                                       int Color_) {
        /** Change ICON Color */

        myIcon = myIcon.getConstantState().newDrawable().mutate();
        ColorFilter filter = new LightingColorFilter(Color_, Color_);
        myIcon.setColorFilter(filter);
        imageview.setImageDrawable(myIcon);

    }

    public static Drawable ChangeIconColor(Drawable myIcon, int Color_) {
        /** Change ICON Color */

        myIcon = myIcon.getConstantState().newDrawable().mutate();

        ColorFilter filter = new LightingColorFilter(Color_, Color_);

        myIcon.setColorFilter(filter);

        return myIcon;

    }
}
