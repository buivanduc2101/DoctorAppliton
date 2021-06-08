package com.sh.doctorapplication.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sh.doctorapplication.model.Hospital;
import com.sh.doctorapplication.model.UserDetail;

public class MySharedPreferences {
    private static final String MY_SHARE_PREFERENCES = "MySharedPreferences";
    private Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putINT(String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, Long value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getINT(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public Long getLong(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, -1L);
    }

    public String getString(String key) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public void putUserDetailLogin(String key, UserDetail userDetail) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(userDetail);
        editor.putString(key, value);
        editor.apply();
    }

    public UserDetail getUserDetailLogin(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, UserDetail.class);
    }

    public void putHospital(String key, Hospital hospital) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(hospital);
        editor.putString(key, value);
        editor.apply();
    }

    public Hospital getHospital(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, Hospital.class);
    }

    public void clearAllData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void clearDataByKey(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }
}
