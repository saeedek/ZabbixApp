package com.vodafone.zabbixapp.zabbix;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by saeed on 23-Sep-15.
 */
public class ZabbixHost {
    public static class params{
        String output,groupids,hostids;
        filter filter;
        public params(String o,String gid){
            this.output=o;
            this.groupids=gid;
        }
        public params(String o,String hid,int nothing){
            this.output=o;
            this.hostids=hid;
        }
        public params(String o,String hostname,String nothing){
            this.output=o;
            this.filter=new filter();
            this.filter.host=hostname;
        }
    }
    public static class filter{
        String host;
    }
    public static class zabbixHostReq{
        public String jsonrpc, method;
        public params params;
        public String id, auth;
        public zabbixHostReq(String j, String m, String i, String a,String o,String gid) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o,gid);
            this.auth = a;
            this.id = i;
        }
        public zabbixHostReq(String j, String m, String i, String a,String o,String hid,int nothing) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o,hid,0);
            this.auth = a;
            this.id = i;
        }
        public zabbixHostReq(String j, String m, String i, String a,String o,String hname,String nothing) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o,hname,"");
            this.auth = a;
            this.id = i;
        }
    }
    public static class zabbixHostRes{
        public String jsonrpc,id;
        public static class HostItem implements Serializable {
            public String name,hostid;
        }
        @SerializedName("result") public HostItem[] HItems;
    }
}
