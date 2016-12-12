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
import com.vodafone.zabbixapp.adapters.HostsAdapter;
import com.vodafone.zabbixapp.adapters.ScriptsAdapter;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;
import com.vodafone.zabbixapp.zabbix.ZabbixHost;
import com.vodafone.zabbixapp.zabbix.ZabbixHostGroup;
import com.vodafone.zabbixapp.zabbix.ZabbixScript;

import java.util.ArrayList;

public class ScriptsActivity extends AppCompatActivity {
    private ListView lstScripts;
    private ScriptsAdapter mysAdapt;
    ZabbixScript.zabbixScriptRes r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripts);
        lstScripts=(ListView)findViewById(R.id.lstScripts);
        if(!ZabbixApplication.getInstance().getZabbixCommunicator().fromIntent){
            this.setTitle(ZabbixApplication.getInstance().getZabbixCommunicator().SelectedHost.name);
            NotIntentProc();
        }
        else{
            fromIntentPre();
        }

        lstScripts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZabbixApplication.getInstance().getZabbixCommunicator().SelectedScript=mysAdapt.getItem(i);
                startActivity(new Intent(getApplicationContext(),ScriptDetailActivity.class));
            }
        });
    }
    private void fromIntentPre() {
        //check if group exist,if not go back to group screen
        final ProgressDialog ringProgressDialog = ProgressDialog.show(ScriptsActivity.this, "Please wait ...", "Checking Data ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZabbixHost.zabbixHostReq req = new ZabbixHost.zabbixHostReq("2.0", "host.get", "1", ZabbixApplication.getInstance().getZabbixCommunicator().auth, "extend", ZabbixApplication.getInstance().getZabbixCommunicator().selHost,"");
                final Gson g = new Gson();
                final String jstring = g.toJson(req);

                ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                    @Override
                    public void onSuccess(String json) {
                        ZabbixHost.zabbixHostRes myres=g.fromJson(json, ZabbixHost.zabbixHostRes.class);
                        if(myres.HItems.length==0){
                            ringProgressDialog.dismiss();
                            Toast.makeText(ScriptsActivity.this, "Host "+ZabbixApplication.getInstance().getZabbixCommunicator().selHost+" Does Not Exist,Check URL", Toast.LENGTH_LONG).show();
                            ZabbixApplication.getInstance().getZabbixCommunicator().fromIntent=false;
                            startActivity(new Intent(ScriptsActivity.this,HostGroupsActivity.class));
                            finish();
                        }
                        else{
                            ZabbixApplication.getInstance().getZabbixCommunicator().SelectedHost= myres.HItems[0];
                            ScriptsActivity.this.setTitle(myres.HItems[0].name);
                            ringProgressDialog.dismiss();
                            NotIntentProc();
                        }

                    }
                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(ScriptsActivity.this, err, Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
    public void NotIntentProc(){
        final ProgressDialog ringProgressDialog = ProgressDialog.show(ScriptsActivity.this, "Please wait ...", "Downloading Data ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ZabbixScript.zabbixScriptReq req=new ZabbixScript.zabbixScriptReq("2.0", "script.get", "1", ZabbixApplication.getInstance().getZabbixCommunicator().auth,"extend",ZabbixApplication.getInstance().getZabbixCommunicator().SelectedHost.hostid);
                final Gson g = new Gson();
                final String jstring = g.toJson(req);

                ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                    @Override
                    public void onSuccess(String json) {
                        r = g.fromJson(json, ZabbixScript.zabbixScriptRes.class);
                        PopulateListView();
                        ringProgressDialog.dismiss();
                    }
                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(ScriptsActivity.this, err, Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
    private void PopulateListView() {
        ArrayList<ZabbixScript.zabbixScriptRes.ScriptItem> items= new ArrayList<>();
        for(ZabbixScript.zabbixScriptRes.ScriptItem i:r.SItems){
            items.add(i);
        }
        mysAdapt=new ScriptsAdapter(this,R.layout.script_item,items);
        lstScripts.setAdapter(mysAdapt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scripts, menu);
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
