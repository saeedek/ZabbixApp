package com.vodafone.zabbixapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by saeed on 18-Sep-15.
 */
public class MyAsyncRunner extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {
        Log.d("MyAsyncTask","Task Running");
        return null;
    }
    protected void onPostExecute(String result) {
        //
    }
}
