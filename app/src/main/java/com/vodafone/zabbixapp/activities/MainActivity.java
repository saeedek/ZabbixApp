package com.vodafone.zabbixapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vodafone.zabbixapp.R;
import com.vodafone.zabbixapp.interfaces.onZabbixRes;
import com.vodafone.zabbixapp.ZabbixApplication;
import com.vodafone.zabbixapp.zabbix.*;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtU,txtP;
    private int whereTo; //0 nothing,1 set g go to h,2 set h go to s,3 set s go to detail
    private String queryId;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtU = (EditText) findViewById(R.id.txtUser);
        txtP = (EditText) findViewById(R.id.txtPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckIP()) {
                    return;
                }
                if (txtU.getText().length() == 0 || txtP.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please Enter User&Pass", Toast.LENGTH_SHORT).show();
                    return;
                }
                ZabbixLogin.zabbixLoginReq mzLogin;
                final Gson g = new Gson();
                mzLogin = new ZabbixLogin.zabbixLoginReq("2.0", "user.login", "1", null, txtU.getText().toString(), txtP.getText().toString());
                final String jstring = g.toJson(mzLogin);
                ZabbixApplication.getInstance().getZabbixCommunicator().doRequest(jstring, new onZabbixRes() {
                    @Override
                    public void onSuccess(String json) {
                        ZabbixLogin.zabbixLoginRes r = g.fromJson(json, ZabbixLogin.zabbixLoginRes.class);
                        Toast.makeText(MainActivity.this, r.result, Toast.LENGTH_SHORT).show();
                        ZabbixApplication.getInstance().getZabbixCommunicator().auth = r.result;
                        switch (whereTo) {
                            case 0:
                                startActivity(new Intent(MainActivity.this, HostGroupsActivity.class));
                                finish();
                                break;
                            case 1:
                                ZabbixApplication.getInstance().getZabbixCommunicator().selGroup = queryId;
                                startActivity(new Intent(MainActivity.this, HostsActivity.class));
                                finish();
                                break;
                            case 2:
                                ZabbixApplication.getInstance().getZabbixCommunicator().selHost = queryId;
                                startActivity(new Intent(MainActivity.this, ScriptsActivity.class));
                                finish();
                                break;
                            case 3:
                                ZabbixApplication.getInstance().getZabbixCommunicator().selScript = queryId;
                                startActivity(new Intent(MainActivity.this, ScriptDetailActivity.class));
                                finish();
                                break;
                        }
                    }
                    @Override
                    public void onFailed(String err) {
                        Toast.makeText(MainActivity.this, err, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Uri data = getIntent().getData();
        whereTo = 0;
        if (data != null) {
            ZabbixApplication.getInstance().getZabbixCommunicator().fromIntent = true;
            switch (data.getQueryParameterNames().toArray()[0].toString()) {
                case "gid":
                    whereTo = 1;
                    queryId = data.getQueryParameter("gid");
                    break;
                case "hid":
                    whereTo = 2;
                    queryId = data.getQueryParameter("hid");
                    break;
                case "sid":
                    whereTo = 3;
                    queryId = data.getQueryParameter("sid");
                    break;
            }
        }
    }

    private boolean CheckIP() {
        if(ZabbixApplication.getInstance().getZabbixCommunicator().IP.equals("")){
            Toast.makeText(MainActivity.this, "Enter Zabbix IP in Setting Page", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
