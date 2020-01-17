package org.fhi360.lamis.mobile.cparp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_APPOINTMENT;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_DEFAULTER;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.ACTION_SYNC;

/**
 * Created by aalozie on 10/22/2017.
 */

public class ServiceScheduler {
    private Context context;
    private PendingIntent pendingIntentDefaulter;
    private AlarmManager alarmManagerDefaulter;
    private PendingIntent pendingIntentAppointment;
    private AlarmManager alarmManagerAppointment;
    private PendingIntent pendingIntentSync;
    private AlarmManager alarmManagerSync;

    public ServiceScheduler(Context context) {
        this.context = context;
    }

    public void start() {
        syncAlarm();
        defaulterAlarm();
        appointmentAlarm();
    }

    private void syncAlarm() {
        //Create a pending intent for the SyncNotificationService
        Intent intent = new Intent(context, IntentServiceHandler.class);
        intent.setAction(ACTION_SYNC);
        pendingIntentSync = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Create an Alarm Manager to trigger every 3 hours
        long currentTime = System.currentTimeMillis();                  // Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
        long triggerFirst = currentTime + 1 * 60 * 60 * 1000;          // Alarm should go off for the first time one hour from the current time
        long repeatInterval = 3 * 60 * 60 * 1000;                     // Interval in milliseconds (3 hours) between subsequent repeats of the alarm
        //long triggerFirst = currentTime + 60 * 1000;
        //long repeatInterval = 60 * 1000;
        alarmManagerSync = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerSync.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentSync);
    }

    private void defaulterAlarm() {
        //Create a pending intent for the DefaulterNotificationService
        Intent intent = new Intent(context, IntentServiceHandler.class);
        intent.setAction(ACTION_DEFAULTER);
        pendingIntentDefaulter = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Create an Alarm Manager to trigger every 12 hours
        long currentTime = System.currentTimeMillis();
        long triggerFirst = currentTime + 1 * 60 * 60 * 1000;// Alarm should go off for the first time one hour from the current time
        long repeatInterval = 12 * 60 * 60 * 1000; // Interval in milliseconds (12 hours) between subsequent repeats of the alarm

        alarmManagerDefaulter = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerDefaulter.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentDefaulter);
    }


    private void appointmentAlarm() {
        //Create a pending intent for the AppointmentNotificationService
        Intent intent = new Intent(context, IntentServiceHandler.class);
        intent.setAction(ACTION_APPOINTMENT);
        pendingIntentAppointment = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Create an Alarm Manager to trigger every 12 hours
        long currentTime = System.currentTimeMillis();
        long triggerFirst = currentTime + 1 * 60 * 60 * 1000;
        long repeatInterval = 12 * 60 * 60 * 1000;
        alarmManagerAppointment = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerAppointment.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentAppointment);
    }

    public void cancel() {
        alarmManagerSync.cancel(pendingIntentSync);
        alarmManagerDefaulter.cancel(pendingIntentDefaulter);
        alarmManagerAppointment.cancel(pendingIntentAppointment);
    }

/*
    private void defaulterAlarm() {
        //Create a pending intent for the DefaulterNotificationReceiver
        Intent intent = new Intent(context, BroadcastReceiverHandler.class);
        intent.setAction(ACTION_DEFAULTER);
        pendingIntentDefaulter = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Create an Alarm Manager to trigger every 1 hours
        long currentTime = System.currentTimeMillis();        // Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
        long triggerFirst = currentTime + 60 * 1000;          // Alarm should go off for the first time one minute from the current time
        long repeatInterval = 1 * 60 * 60 * 1000;           // Interval in milliseconds (1 hours) between subsequent repeats of the alarm
        //long repeatInterval = 60 * 1000;
        alarmManagerDefaulter = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManagerDefaulter.setRepeating(AlarmManager.RTC_WAKEUP, triggerFirst, repeatInterval, pendingIntentDefaulter);
    }
*/

}
