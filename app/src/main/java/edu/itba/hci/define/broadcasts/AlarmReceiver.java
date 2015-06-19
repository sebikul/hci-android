package edu.itba.hci.define.broadcasts;

import android.app.AlarmManager;
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

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.PurchasesActivity;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.OrderList;

public class AlarmReceiver extends BroadcastReceiver {

    private final String NOTIFICATION_BROADCAST = "edu.itba.hci.define.NOTIFICATION_BROADCAST";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.v("AlarmReceiver", "Manejando la alarma");

        DefineApplication app = (DefineApplication) context.getApplicationContext();
        ApiManager.getAllOrders(new Callback<OrderList>() {

            @Override
            public void onSuccess(OrderList response) {
                // TODO: Verificar que haya actualizaciones en las ordenes

                Intent notificationIntent = new Intent(NOTIFICATION_BROADCAST);
                context.sendOrderedBroadcast(notificationIntent, null);
            }

            @Override
            public void onError(ApiError error) {
                Log.v("AlarmReceiver", "Error en getAllOrders");
            }

            @Override
            public void onErrorConnection() {
                Log.v("AlarmReceiver", "Error de conexion en getAllOrders");
            }
        });
    }
}
