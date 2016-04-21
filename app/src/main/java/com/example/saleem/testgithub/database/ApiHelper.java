package com.example.saleem.testgithub.database;

import android.content.Context;


public class ApiHelper {

    public app App;
    public cache Cache;
    public Context Context;

    public ApiHelper(Context context) {
        this.Context = context;
        this.App = new app(context);
        this.Cache = new cache(context);
    }


}
