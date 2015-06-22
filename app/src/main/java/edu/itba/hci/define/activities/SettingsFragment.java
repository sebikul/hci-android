package edu.itba.hci.define.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.broadcasts.SessionReceiver;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    static private final String LOG_TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(LOG_TAG, "Fragmento de preferencias creado");

        DefineApplication app = (DefineApplication) getActivity().getApplicationContext();

        if (app.getPreferences().getString("authentication_token", null) == null) {

            addPreferencesFromResource(R.xml.pref_notification_guest);

        } else {
            addPreferencesFromResource(R.xml.pref_notification);
        }

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("alarmInterval")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(R.string.notification_interval_summary);

            Log.v(LOG_TAG, "El nuevo tiempo de actualizacion es " + sharedPreferences.getString(key, "nada puto"));
        }

        Log.v(LOG_TAG, "Las actualizaciones estan " + sharedPreferences.getBoolean("notifications_enable", false));

        Intent intent = new Intent(SessionReceiver.REFRESH_ALARM);
        getActivity().sendOrderedBroadcast(intent, null);
        DefineApplication app = (DefineApplication) getActivity().getApplicationContext();
        app.getPreferences().edit().putBoolean("notifications_enable", sharedPreferences.getBoolean("notifications_enable", false)).commit();
        app.getPreferences().edit().putString("alarmInterval", sharedPreferences.getString("alarmInterval","900000")).commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
