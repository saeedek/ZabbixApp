package com.vodafone.zabbixapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vodafone.zabbixapp.R;
import com.vodafone.zabbixapp.ZabbixApplication;
import com.vodafone.zabbixapp.adapters.HostGroupsAdapter;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;
import com.vodafone.zabbixapp.zabbix.*;

import java.util.ArrayList;

public class HostGroupsActivity extends AppCompatActivity {

    private ListView lstHostGroups;
    private HostGroupsAdapter myhgAdapt;
    ZabbixHostGroup.zabbixHostGroupRes r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_groups);
        lstHostGroups=(ListView)findViewById(R.id.lstHostGroups);

        final ProgressDialog ringProgressDialog = ProgressDialog.show(HostGroupsActivity.this, "Please wait ...", "Downloading Data ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZabbixHostGroup.zabbixHostGroupReq req=new ZabbixHostGroup.zabbixHostGroupReq("2.0", "hostgroup.get", "1",ZabbixApplication.getInstance().getZabbixCommunicator().auth,"extend");
                final Gson g = new Gson();
                final String jstring = g.toJson(req);

                ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                    @Override
                    public void onSuccess(String json) {
                        r = g.fromJson(json, ZabbixHostGroup.zabbixHostGroupRes.class);
                        PopulateListView();
                        ringProgressDialog.dismiss();
                    }
                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(HostGroupsActivity.this, err, Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                    }
                });
            }
        }).start();
        lstHostGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZabbixApplication.getInstance().getZabbixCommunicator().SelectedGroup =myhgAdapt.getItem(i);
                startActivity(new Intent(getApplicationContext(),HostsActivity.class));
            }
        });
    }

    private void PopulateListView() {
        ArrayList<ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem> items= new ArrayList<>();
        for(ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem i:r.HGItems){
            items.add(i);
        }
        myhgAdapt=new HostGroupsAdapter(this,R.layout.host_group_item,items);
        lstHostGroups.setAdapter(myhgAdapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_host_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
