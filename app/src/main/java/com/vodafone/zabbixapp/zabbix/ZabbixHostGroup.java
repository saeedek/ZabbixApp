package com.vodafone.zabbixapp.zabbix;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by saeed on 23-Sep-15.
 */
public class ZabbixHostGroup {
    public static class params{
        String output,groupids;
        public params(String o){
            this.output=o;
        }
        public params(String o,String gid){
            this.output=o;
            this.groupids=gid;
        }
    }
    public static class zabbixHostGroupReq{
        public String jsonrpc, method;
        public params params;
        public String id, auth;
        public zabbixHostGroupReq(String j, String m, String i, String a,String o) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o);
            this.auth = a;
            this.id = i;
        }
        public zabbixHostGroupReq(String j, String m, String i, String a,String o,String gid) {
            this.jsonrpc = j;
            this.method = m;
            this.params=new params(o,gid);
            this.auth = a;
            this.id = i;
        }
    }
    public static class zabbixHostGroupRes{
        public String jsonrpc,id;
         public static class HostGroupItem implements Serializable{
            public String name,groupid;
        }
        @SerializedName("result") public HostGroupItem[] HGItems;

    }

}
