package com.vodafone.zabbixapp.zabbix;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by saeed on 23-Sep-15.
 */
public class ZabbixScript {
    public static class params{
        //String[] hostids,groupids;
        String output,hostids;
        public params(String o,String shid){
            // this.hostids=h;
            //this.groupids=g;
            this.output=o;
            this.hostids=shid;
        }
    }
    public static class zabbixScriptExeParams{
        String scriptid,hostid;
        public zabbixScriptExeParams(String sid,String hid){
            this.scriptid=sid;
            this.hostid=hid;
        }
    }
    public static class zabbixScriptReq{
        public String jsonrpc, method;
        public params params;
        public String id, auth;
        public zabbixScriptReq(String j, String m, String i, String a,String o,String shid) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o,shid);
            this.auth = a;
            this.id = i;
        }
    }
    public static class zabbixScriptRes{
        public String jsonrpc,id;
        public static class ScriptItem implements Serializable {
            public String name,scriptid,command,description;
        }
        @SerializedName("result") public ScriptItem[] SItems;
    }
    public static class zabbixScriptExeReq{
        public String jsonrpc, method;
        public zabbixScriptExeParams params;
        public String id, auth;
        public zabbixScriptExeReq(String j, String m, String i, String a,String sid,String hid) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new zabbixScriptExeParams(sid,hid);
            this.auth = a;
            this.id = i;
        }
    }
    public static class zabbixScriptExeRes{
        public String jsonrpc,id;
        public static class ExeResult implements Serializable{
            public String response,value;
        }
        public ExeResult result;
    }
}
