package com.vodafone.zabbixapp.interfaces;

/**
 * Created by saeed on 23-Sep-15.
 */
public interface onZabbixRes {
    void onSuccess(String Json);
    void onFailed(String err);
}
