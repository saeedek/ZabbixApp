package com.vodafone.zabbixapp;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.vodafone.zabbixapp.zabbix.ZabbixCommunicator;

/**
 * Created by saeed on 18-Sep-15.
 */
public class ZabbixApplication extends Application {
    private static ZabbixApplication sInstance;
    private ZabbixCommunicator mCommunicator;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);

        mCommunicator=new ZabbixCommunicator(this.getApplicationContext());

        sInstance = this;
        mCommunicator.IP=getApplicationContext().getSharedPreferences("SharedFile", Context.MODE_PRIVATE).getString("ZabbixIP","");

    }

    public synchronized static ZabbixApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
    public ZabbixCommunicator getZabbixCommunicator() {
        return mCommunicator;
    }

}
