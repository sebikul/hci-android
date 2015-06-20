package edu.itba.hci.define.broadcasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.itba.hci.define.DefineApplication;


public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("BootReceiver", "Iniciando broadcast");
        Intent alarmIntent =
                new Intent(context, AlarmReceiver.class);
        if (conditions(context, alarmIntent)) {
            Log.v("BootReceiver", "Estableciendo alarma");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent alarmPendingIntent =
                    PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            DefineApplication app = (DefineApplication) context.getApplicationContext();
            long alarmInterval = app.getPreferences().getLong("alarmInterval", AlarmManager.INTERVAL_FIFTEEN_MINUTES);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmInterval, alarmInterval,alarmPendingIntent);
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 1, 1000 * 60 * 1, alarmPendingIntent);
        }
    }

    private boolean conditions(Context context, Intent alarmIntent) {
        DefineApplication app = (DefineApplication) context.getApplicationContext();
        String authToken = app.getPreferences().getString("authentication_token", null);
        if (authToken == null)
            return false;
        return true;
    }
}
