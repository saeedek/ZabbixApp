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

import com.vodafone.zabbixapp.zabbix.ZabbixScript;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 24-Sep-15.
 */
public class ScriptsAdapter extends ArrayAdapter<ZabbixScript.zabbixScriptRes.ScriptItem> {

    private ArrayList<ZabbixScript.zabbixScriptRes.ScriptItem> items;
    private int layoutResourceId;
    private Context context;

    public ScriptsAdapter(Context context, int resource, ArrayList<ZabbixScript.zabbixScriptRes.ScriptItem> objects) {
        super(context, resource, objects);
        layoutResourceId = resource;
        this.context = context;
        items = objects;
    }

    public static class ScriptHolder {
        TextView sname, scommand, sdesc;
        String sid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ScriptHolder holder = null;
        if (row != null)
            holder = (ScriptHolder) row.getTag();
        if (holder == null) {

            row = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, null);

            holder = new ScriptHolder();

            holder.sname = (TextView) row.findViewById(R.id.txt_script_name);
            holder.sname.setText(items.get(position).name);

            holder.scommand = (TextView) row.findViewById(R.id.txt_script_command);
            holder.scommand.setText(items.get(position).command);

            holder.sdesc = (TextView) row.findViewById(R.id.txt_script_description);
            holder.sdesc.setText(items.get(position).description);
            holder.sid = items.get(position).scriptid;

            holder.sname.setTag(items.get(position));
        }
        row.setTag(holder);
        return row;
    }
}
