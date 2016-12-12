package com.vodafone.zabbixapp.zabbix;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vodafone.zabbixapp.ZabbixApplication;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saeed on 23-Sep-15.00
 */
public class ZabbixCommunicator {
    public String auth;
    public String IP;
    public Context mContext;
    public ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem SelectedGroup;
    public ZabbixHost.zabbixHostRes.HostItem SelectedHost;
    public ZabbixScript.zabbixScriptRes.ScriptItem SelectedScript;

    public String selGroup,selHost,selScript;
    public boolean fromIntent;
    public ZabbixCommunicator(Context c){
        this.mContext=c;
    }

    public void doRequest(String Json2Send,final onZabbixRes onZabbixRes){
        JSONObject obj= null;
        try {
            obj = new JSONObject(Json2Send);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest("http://"+IP+"/api_jsonrpc.php",obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onZabbixRes.onSuccess(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onZabbixRes.onFailed(error.toString());
            }
        });
        ZabbixApplication.getInstance().getRequestQueue().add(request);
    }
}
