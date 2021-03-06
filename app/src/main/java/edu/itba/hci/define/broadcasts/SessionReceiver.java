package edu.itba.hci.define.broadcasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.itba.hci.define.DefineApplication;


public class SessionReceiver extends BroadcastReceiver {

    static public final String REFRESH_ALARM = "edu.itba.hci.define.REFRESH_ALARM";
    private PendingIntent alarmPendingIntent;


    public SessionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("SessionReceiver", "Llamando broadcast");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent =
                new Intent(context, AlarmReceiver.class);

        if (intent.getAction().equals(REFRESH_ALARM)) {
            if (alarmPendingIntent != null) {
                alarmManager.cancel(alarmPendingIntent);
            }
        }
        if (conditions(context)) {
            Log.v("SessionReceiver", "Estableciendo alarma");
            alarmPendingIntent =
                    PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            DefineApplication app = (DefineApplication) context.getApplicationContext();

            long alarmInterval = Long.parseLong(app.getPreferences().getString("alarmInterval", "900000"));

            Log.v("SessionReceiver", "El alarm interval vale " + String.valueOf(alarmInterval));

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmInterval, alarmInterval, alarmPendingIntent);
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 30 * 1, 1000 * 30 * 1, alarmPendingIntent);
        }
    }

    private boolean conditions(Context context) {
        Log.v("SessionReceiver", "Verificando condiciones");

        DefineApplication app = (DefineApplication) context.getApplicationContext();

        String authToken = app.getPreferences().getString("authentication_token", null);
        boolean notification_enable = app.getPreferences().getBoolean("notifications_enable", true );


        if (authToken == null || !notification_enable) {
            Log.v("SessionReceiver", String.format("Condiciones invalidas %s %s", authToken, notification_enable));
            return false;
        }
        Log.v("SessionReceiver", "Condiciones validas");
        return true;
    }
}
