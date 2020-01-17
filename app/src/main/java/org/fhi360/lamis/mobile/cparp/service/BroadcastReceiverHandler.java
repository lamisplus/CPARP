package org.fhi360.lamis.mobile.cparp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.fhi360.lamis.mobile.cparp.DefaulterListActivity;
import org.fhi360.lamis.mobile.cparp.R;

public class BroadcastReceiverHandler extends BroadcastReceiver {
    private Context context;

    public BroadcastReceiverHandler() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                handleActionDefaulter();
            }
        }
    }

    private void handleActionDefaulter() {
        int NOTIFICATION_ID = 100;
        Intent intent = new Intent(context, DefaulterListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DefaulterListActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText("You have clients who have defaulted for ARV refill")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
