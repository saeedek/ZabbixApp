package com.vodafone.zabbixapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vodafone.zabbixapp.R;
import com.vodafone.zabbixapp.ZabbixApplication;

public class SettingActivity extends AppCompatActivity {

    private EditText txtZbxIP;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        txtZbxIP=(EditText)findViewById(R.id.txt_zbxIP);
        btnSave=(Button)findViewById(R.id.btn_SavenExit);

        final SharedPreferences sharedPref = this.getSharedPreferences("SharedFile", Context.MODE_PRIVATE);
        if(!sharedPref.getString("ZabbixIP","").equals("")){
            txtZbxIP.setText(sharedPref.getString("ZabbixIP",""));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putString("ZabbixIP",txtZbxIP.getText().toString()).commit();
                ZabbixApplication.getInstance().getZabbixCommunicator().IP=txtZbxIP.getText().toString();
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
