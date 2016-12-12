package com.vodafone.zabbixapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.vodafone.zabbixapp.adapters.HostsAdapter;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;
import com.vodafone.zabbixapp.zabbix.ZabbixHost;
import com.vodafone.zabbixapp.zabbix.ZabbixHostGroup;

import java.util.ArrayList;

public class HostsActivity extends AppCompatActivity {

    private ZabbixHostGroup.zabbixHostGroupRes.HostGroupItem hg;

    private String selgroup;

    private ListView lstHosts;
    private HostsAdapter myhAdapt;
    ZabbixHost.zabbixHostRes r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosts);
        lstHosts = (ListView) findViewById(R.id.lstHosts);
        this.setTitle(ZabbixApplication.getInstance().getZabbixCommunicator().SelectedGroup.name);
        NotIntentProc();
        lstHosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZabbixApplication.getInstance().getZabbixCommunicator().SelectedHost = myhAdapt.getItem(i);
                startActivity(new Intent(getApplicationContext(), ScriptsActivity.class));
            }
        });
    }
    public void NotIntentProc() {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(HostsActivity.this, "Please wait ...", "Downloading Data ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZabbixHost.zabbixHostReq req = new ZabbixHost.zabbixHostReq("2.0", "host.get", "1", ZabbixApplication.getInstance().getZabbixCommunicator().auth, "extend", ZabbixApplication.getInstance().getZabbixCommunicator().SelectedGroup.groupid);
                final Gson g = new Gson();
                final String jstring = g.toJson(req);

                ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                    @Override
                    public void onSuccess(String json) {
                        r = g.fromJson(json, ZabbixHost.zabbixHostRes.class);
                        PopulateListView();
                        ringProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(HostsActivity.this, err, Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
    private void PopulateListView() {
        ArrayList<ZabbixHost.zabbixHostRes.HostItem> items = new ArrayList<>();
        for (ZabbixHost.zabbixHostRes.HostItem i : r.HItems) {
            items.add(i);
        }
        myhAdapt = new HostsAdapter(this, R.layout.host_item, items);
        lstHosts.setAdapter(myhAdapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hosts, menu);
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
