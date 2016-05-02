package com.example.saleem.testgithub.database;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;


public class DataBaseAdapter {


    final byte CACHE_TABLE = 0;
    final byte APP_TABLE = 1;
    final byte GET_OP = 0;
    final byte SET_OP = 1;
    final byte Delete_OP = 2;


    public void SetApp(String Key, String Value, int tag,
                        DataBaseAble dbAble, app app, cache cache) {
            new SetAsyncTask(APP_TABLE, Key, tag, Value, dbAble, app, cache)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void SetCache(String Key, String Value, int tag,
                          DataBaseAble dbAble, app app, cache cache) {

            new SetAsyncTask(CACHE_TABLE, Key, tag, Value, dbAble, app, cache)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void GetApp(final String Key, final int tag,
                       final DataBaseAble dbAble, app app, cache cache) {

            new GetAsyncTask(APP_TABLE, Key, tag, dbAble, app, cache)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void GetCache(String Key, int tag,
                         DataBaseAble dbAble, app app, cache cache) {

            new GetAsyncTask(CACHE_TABLE, Key, tag, dbAble, app, cache)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    class HandlerDB {
        DataBaseAble dbAbl;
        byte table;
        String key;
        String result;
        int tag;
        byte op;

        HandlerDB(DataBaseAble dbAbl, byte table, String key, String result,
                  int tag, byte op) {
            this.dbAbl = dbAbl;
            this.table = table;
            this.key = key;
            this.result = result;
            this.tag = tag;
            this.op = op;
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            HandlerDB h = (HandlerDB) msg.obj;

            switch (h.op) {
                case GET_OP:
                    if (h.table == APP_TABLE) {
                        h.dbAbl.GetApp_db(h.key, h.result, h.tag);
                    } else {
                        h.dbAbl.GetCache_db(h.key, h.result, h.tag);
                    }
                    break;
                case SET_OP:
                    if (h.table == APP_TABLE) {

                        h.dbAbl.SetApp_db(h.key, h.tag);

                    } else {

                        h.dbAbl.SetCache_db(h.key, h.tag);
                    }
                    break;
                case Delete_OP:
//                    h.dbAbl.RemoveApp_db(h.key, h.tag);
//                    h.dbAbl.RemoveCache_db(h.key, h.tag);
                    break;
            }

        }
    };

    class GetAsyncTask extends AsyncTask<String, Integer, String> {

        byte table;
        String Key;
        int tag;
        DataBaseAble dbAble;
        app app;
        cache cache;

        public GetAsyncTask(byte table, final String Key, final int tag,
                             final DataBaseAble dbAble, app app, cache cache) {

            this.table = table;
            this.Key = Key;
            this.tag = tag;
            this.dbAble = dbAble;
            this.app = app;
            this.cache = cache;
        }

        @Override
        protected String doInBackground(String... params) {

            if (table == APP_TABLE) {

                // app_ ap = new app_(context);
                return app.SelectApp(Key);

            } else {

                // cache_ cache = new cache_(context);

                String response=cache.SelectFromCache(Key);
                return response;

            }

            // return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dbAble != null) {

                Message msg = handler.obtainMessage();
                msg.obj = new HandlerDB(dbAble, table, Key, result, tag, GET_OP);
                handler.sendMessage(msg);

				/*
                 * if (table == APP_TABLE) { dbAble.GetApp_db(Key, result, tag);
				 * } else{ dbAble.GetCache_db(Key, result, tag); }
				 */
            }
        }
    }

    class SetAsyncTask extends AsyncTask<String, Integer, String> {

        String Key;
        int tag;
        String value;
        DataBaseAble dbAble;
        app app;
        cache cache;
        byte table;

        public SetAsyncTask(byte table, String Key, int tag, String value,
                             DataBaseAble dbAble, app app, cache cache) {
            this.table = table;
            this.Key = Key;
            this.tag = tag;
            this.value = value;
            this.app = app;
            this.cache = cache;
            this.dbAble = dbAble;
        }

        @Override
        protected String doInBackground(String... params) {

            // if (Key == null) {
            // return null;
            // }

            if (table == APP_TABLE) {

                app.AddApp(Key, value);

            } else {

                cache.AddCache(Key, value);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dbAble != null) {

                Message msg = handler.obtainMessage();
                msg.obj = new HandlerDB(dbAble, table, Key, result, tag, SET_OP);
                handler.sendMessage(msg);

            }

        }

    }


}
