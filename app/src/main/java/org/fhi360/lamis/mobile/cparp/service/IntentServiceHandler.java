package org.fhi360.lamis.mobile.cparp.service;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.fhi360.lamis.mobile.cparp.DefaulterListActivity;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_APPOINTMENT;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_DEFAULTER;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_SYNC;

public class IntentServiceHandler extends IntentService {
    public IntentServiceHandler() {
        super("IntentServiceHandler");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC.equals(action)) {
                handleActionSync();
            }
            else {
                if (ACTION_APPOINTMENT.equals(action)) {
                    handleActionAppointment();
                }
                else {
                    if (ACTION_DEFAULTER.equals(action)) {
                        if(new PatientDAO(this).defaulters()) handleActionDefaulter();
                    }
                }

            }
        }
    }

    /**
     * Handle action sync in the provided background thread.
     */

    private void handleActionSync() {
        Log.v("IntentServiceHandler", "Syncing....");
        new DataSynchronizer(this).sync();
    }

    private void handleActionDefaulter() {
        int NOTIFICATION_ID = 100;
        Intent intent = new Intent(this, DefaulterListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DefaulterListActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText("You have clients who have defaulted for ARV refill")
                .build();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }


    private void handleActionAppointment() {
        // TODO: Handle action
     }

}
