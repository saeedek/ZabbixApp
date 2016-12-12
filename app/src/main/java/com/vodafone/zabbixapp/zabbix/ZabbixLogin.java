package com.vodafone.zabbixapp.zabbix;

/**
 * Created by saeed on 18-Sep-15.
 */
public class ZabbixLogin {
    public static class myparam {
        public String user, password;

        public myparam(String u, String pass) {
            this.password = pass;
            this.user = u;
        }
    }
    public static class zabbixLoginRes {
        public String result,jsonrpc,id;
        //public zabbixLoginResults(String r){
        // this.result=r;
        // }
    }
    public static class zabbixLoginReq{
        public String jsonrpc, method, id, auth;
        public ZabbixLogin.myparam params;
        public zabbixLoginReq(String j, String m, String i, String a,String u,String pass) {
            this.jsonrpc = j;
            this.method = m;
            this.id = i;
            this.auth = a;
            this.params =new myparam(u,pass);
        }
    }
}
