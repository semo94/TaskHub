package com.example.saleem.testgithub.database;


public interface DataBaseAble {

    void SetApp_db(String Key, int tag);

    void SetCache_db(String Key, int tag);

    void GetApp_db(String Key, String Value, int tag);

    void GetCache_db(String Key, String Value, int tag);

}
