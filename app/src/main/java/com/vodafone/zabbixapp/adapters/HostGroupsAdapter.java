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
import com.vodafone.zabbixapp.activities.HostsActivity;
import com.vodafone.zabbixapp.zabbix.ZabbixHostGroup;

import java.util.ArrayList;

/**
 * Created by saeed on 24-Sep-15.
 */
public class HostGroupsAdapter extends ArrayAdapter<ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem> {

    private ArrayList<ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem> items;
    private int layoutResourceId;
    private Context context;

    public HostGroupsAdapter(Context context, int resource, ArrayList<ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem> objects) {
        super(context, resource, objects);
        layoutResourceId=resource;
        this.context=context;
        items=objects;
    }

    public static class HostGroupHolder {
        TextView hgtext;
        String hgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HostGroupHolder holder = null;
        if (row != null)
            holder = (HostGroupHolder) row.getTag();
        if (holder == null) {

            row = ((Activity)context).getLayoutInflater().inflate(layoutResourceId,null);

            holder = new HostGroupHolder();

            holder.hgtext = (TextView) row.findViewById(R.id.txt_hg);
            holder.hgtext.setText(items.get(position).name);
            holder.hgid=items.get(position).groupid;

            holder.hgtext.setTag(items.get(position));
            row.setTag(holder);
        }
        return row;
    }
}
