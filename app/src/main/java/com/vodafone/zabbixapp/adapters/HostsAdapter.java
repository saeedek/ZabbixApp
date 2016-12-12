package com.vodafone.zabbixapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vodafone.zabbixapp.R;
import com.vodafone.zabbixapp.ZabbixApplication;
import com.vodafone.zabbixapp.activities.ScriptsActivity;
import com.vodafone.zabbixapp.zabbix.ZabbixHost;

import java.util.ArrayList;

/**
 * Created by saeed on 24-Sep-15.
 */
public class HostsAdapter extends ArrayAdapter<ZabbixHost.zabbixHostRes.HostItem> {
    private ArrayList<ZabbixHost.zabbixHostRes.HostItem> items;
    private int layoutResourceId;
    private Context context;

    public HostsAdapter(Context context, int resource, ArrayList<ZabbixHost.zabbixHostRes.HostItem> objects) {
        super(context, resource, objects);
        layoutResourceId=resource;
        this.context=context;
        items=objects;
    }
    public static class HostHolder {
        TextView htext;
        String hid;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HostHolder holder = null;
        if (row != null)
            holder = (HostHolder) row.getTag();
        if (holder == null) {

            row = ((Activity)context).getLayoutInflater().inflate(layoutResourceId,null);

            holder = new HostHolder();

            holder.htext = (TextView) row.findViewById(R.id.txt_h);
            holder.htext.setText(items.get(position).name);
            holder.hid=items.get(position).hostid;
            holder.htext.setTag(items.get(position));
            row.setTag(holder);
        }
        return row;
    }
}
