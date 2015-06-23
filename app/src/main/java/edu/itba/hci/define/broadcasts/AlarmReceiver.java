package edu.itba.hci.define.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import edu.itba.hci.define.DefineApplication;
import edu.itba.hci.define.api.ApiError;
import edu.itba.hci.define.api.ApiManager;
import edu.itba.hci.define.api.Callback;
import edu.itba.hci.define.models.Order;
import edu.itba.hci.define.models.OrderList;

public class AlarmReceiver extends BroadcastReceiver {

    static public final String NOTIFICATION_BROADCAST = "edu.itba.hci.define.NOTIFICATION_BROADCAST";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.v("AlarmReceiver", "Manejando la alarma");
        DefineApplication app = (DefineApplication) context.getApplicationContext();
        if(app.getPreferences().getString("authentication_token", null)==null) {
            Log.v("AlarmReceiver", "Auth_token null");
            return;
        }
        final List<Order> cachedOrders = app.readFromCache("orders");
        ApiManager.getAllOrders(new Callback<OrderList>() {

            @Override
            public void onSuccess(OrderList response) {
                if(cachedOrders==null){
                    Log.v("AlarmReceiver", "No hay ordenes cargadas en cache");
                    return;
                }

                for(Order apiOrder:response.getOrders()){
                    for(Order cOrder:cachedOrders) {
                        if(cOrder.equals(apiOrder) && apiOrder.hasNotifications() &&
                                (cOrder.getStatus()!=apiOrder.getStatus() ||
                                 cOrder.getLatitude()!=apiOrder.getLatitude() ||
                                 cOrder.getLongitude()!=apiOrder.getLongitude())){
                            Log.v("AlarmReceiver", "Se modifico una compra");
                            Intent notificationIntent = new Intent(NOTIFICATION_BROADCAST);
                            context.sendOrderedBroadcast(notificationIntent, null);
                            return;
                        }
                    }
                }
                Log.v("AlarmReceiver", "No se modifico ninguna compra");
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
