package edu.itba.hci.define.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import edu.itba.hci.define.R;
import edu.itba.hci.define.activities.base.NavBasicActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 137;
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("NotificationReceiver", "Creando notificacion");
        // Create the intent to start Activity when notification in action bar is
        // clicked.
        Intent notificationIntent = new Intent(context, NavBasicActivity.class);
        notificationIntent.putExtra("action", NavBasicActivity.INTENT_PURCHASES);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NavBasicActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        // Create the pending intent granting the Operating System to launch activity
        // when notification in action bar is clicked.
        final PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(context.getResources().getString(R.string.notification_text))
                .setContentText(context.getResources().getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_action_shopping_basket)
                        // Cancel notification if action bar is clicked.
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Ignore deprecated warning. In newer devices SDK 16+ should use build() method.
        // getNotification() method should be used since minSdkVersion is set to 15.
        // getNotification() method internally calls build() method.
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());

    }
}
