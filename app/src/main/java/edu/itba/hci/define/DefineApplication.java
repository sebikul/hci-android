package edu.itba.hci.define;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.models.User;

public class DefineApplication extends Application {

    static private final String PREFERENCES_BUCKET = "DEFINE_PREFERENCES";
    static private final String LOG_TAG = "DefineApplication";

    private static DefineApplication singleton;
    private SharedPreferences preferences;
    private User session;

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

    public User getSession() {
        return session;
    }

    public void setSession(User session) {

        Log.v(LOG_TAG, "Guardando sesion");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", session.getUsername());
        editor.putString("authentication_token", "a8c0d2a9d332574951a8e4a0af7d516f");

        this.session = session;

    }

    public boolean isLoggedIn() {
        return session != null;
    }


}