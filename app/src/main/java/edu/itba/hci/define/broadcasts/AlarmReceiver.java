package edu.itba.hci.define.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.List;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.PurchasesActivity;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Order;

public class AlarmReceiver extends BroadcastReceiver {

    private final String NOTIFICATION_BROADCAST = "edu.itba.hci.define.NOTIFICATION_BROADCAST";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("AlarmReceiver", "Manejando la alarma");
        // TODO: Acá va la verificación si las ordenes cambiaron
       // ApiManager.getAllOrders(/*username*/,/*authentication token*/, Callback < List< Order>>(){    });

        Intent notificationIntent = new Intent(NOTIFICATION_BROADCAST);
        context.sendOrderedBroadcast(notificationIntent,null);

    }
}
