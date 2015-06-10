package edu.itba.hci.define;

import android.app.Application;
import android.content.SharedPreferences;

import edu.itba.hci.define.api.ApiManager;

public class DefineApplication extends Application {

    private static DefineApplication singleton;

    static private final String PREFERENCES_BUCKET = "DEFINE_PREFERENCES";

    private SharedPreferences preferences;

    public DefineApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;


        preferences = getSharedPreferences(PREFERENCES_BUCKET, MODE_PRIVATE);
        ApiManager.initialize(preferences);


        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("username", "janedoe");
        editor.putString("authentication_token", "a8c0d2a9d332574951a8e4a0af7d516f");

        editor.apply();

    }
}