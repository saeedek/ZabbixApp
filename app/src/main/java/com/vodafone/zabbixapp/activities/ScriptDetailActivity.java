package com.vodafone.zabbixapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vodafone.zabbixapp.R;
import com.vodafone.zabbixapp.ZabbixApplication;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;
import com.vodafone.zabbixapp.zabbix.ZabbixHost;
import com.vodafone.zabbixapp.zabbix.ZabbixScript;

public class ScriptDetailActivity extends AppCompatActivity {

    private TextView txtCmd,txtRes,txtResponse;
    private Button btnExe;

    ZabbixScript.zabbixScriptExeRes r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_detail);
        txtCmd=(TextView)findViewById(R.id.txtCmd);
        txtResponse=(TextView)findViewById(R.id.txtResponse);
        txtRes=(TextView)findViewById(R.id.txt_exe_result);
        btnExe=(Button)findViewById(R.id.btnExe);

        final ProgressDialog ringProgressDialog = ProgressDialog.show(ScriptDetailActivity.this, "Please wait ...", "Downloading Data ...", true);
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
                        ZabbixApplication.getInstance().getZabbixCommunicator().SelectedScript = g.fromJson(json, ZabbixScript.zabbixScriptRes.class).SItems[0];
                        populateViews();
                        ringProgressDialog.dismiss();
                    }
                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(ScriptDetailActivity.this, err, Toast.LENGTH_SHORT).show();
                        ringProgressDialog.dismiss();
                    }
                });
            }
        }).start();

        btnExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog ringProgressDialog = ProgressDialog.show(ScriptDetailActivity.this, "Please wait ...", "Downloading Data ...", true);
                ringProgressDialog.setCancelable(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ZabbixScript.zabbixScriptExeReq req=new ZabbixScript.zabbixScriptExeReq("2.0", "script.execute", "1", ZabbixApplication.getInstance().getZabbixCommunicator().auth,ZabbixApplication.getInstance().getZabbixCommunicator().SelectedScript.scriptid,ZabbixApplication.getInstance().getZabbixCommunicator().SelectedHost.hostid);
                        final Gson g = new Gson();
                        final String jstring = g.toJson(req);

                        ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                            @Override
                            public void onSuccess(String json) {
                                r = g.fromJson(json, ZabbixScript.zabbixScriptExeRes.class);
                                PrintResults();
                                ringProgressDialog.dismiss();
                            }
                            @Override
                            public void onFailed(String err) {
                                Toast.makeText(ScriptDetailActivity.this, err, Toast.LENGTH_SHORT).show();
                                ringProgressDialog.dismiss();
                            }
                        });
                    }
                }).start();
            }
        });

    }

    private void populateViews() {
        ZabbixScript.zabbixScriptRes.ScriptItem mys = ZabbixApplication.getInstance().getZabbixCommunicator().SelectedScript;
        this.setTitle(mys.name);
        txtCmd.setText(mys.command);
    }

    private void PrintResults() {
        txtResponse.setText(r.result.response);
        txtRes.setText(r.result.value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_script_detail, menu);
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
