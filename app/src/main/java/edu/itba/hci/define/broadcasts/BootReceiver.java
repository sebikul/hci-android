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
        Log.v("BootReceiver","Iniciando broadcast");
        if (conditions(context)) {
            Log.v("BootReceiver","Estableciendo alarma");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent =
                    new Intent(context, AlarmReceiver.class);
            PendingIntent alarmPendingIntent =
                    PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            // TODO: Se tiene que guardar la configuración de cada cuanto se quiere hacer la consulta.
            DefineApplication app= (DefineApplication) context.getApplicationContext();
            long alarmInterval = app.getPreferences().getLong("alarmInterval", AlarmManager.INTERVAL_FIFTEEN_MINUTES);
            /*alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    alarmInterval,alarmInterval, alarmPendingIntent);*/
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 2, 1000 * 60*2,alarmPendingIntent);

        }
    }

    private boolean conditions(Context context) {
        return true;
    }
}
