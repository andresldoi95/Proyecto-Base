package com.techtraining.cosechasapp.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;

import com.techtraining.cosechasapp.DBManager;
import com.techtraining.cosechasapp.MainActivity;
import com.techtraining.cosechasapp.R;
import com.techtraining.cosechasapp.db.AppDatabase;
import com.techtraining.cosechasapp.db.Cosecha;

import java.util.List;

/**
 * Broadcast receiver for the alarm, which delivers the notification.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Deliver the notification.
        deliverNotification(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(final Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = DBManager.getInstance(context);
                List<Cosecha> cosechas = appDatabase.cosechaDao().getFinalizadas();
                if(cosechas.size() > 0){
                    Intent contentIntent = new Intent(context, MainActivity.class);
                    PendingIntent contentPendingIntent = PendingIntent.getActivity
                            (context, NOTIFICATION_ID, contentIntent, PendingIntent
                                    .FLAG_UPDATE_CURRENT);
                    // Build the notification
                    NotificationCompat.Builder builder = new NotificationCompat.Builder
                            (context, PRIMARY_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                            .setColor(context.getResources().getColor(R.color.colorAccent))
                            .setContentTitle(context.getString(R.string.notification_title))
                            .setContentText(context.getString(R.string.notification_text))
                            .setContentIntent(contentPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL);
                    // Deliver the notification
                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());
                }
            }
        });
    }
}