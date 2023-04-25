package com.example.holidaydestinationsapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class RaiseNotificationService extends Service {
    public static final String EXTRA_NOTIF_TYPE = "notification_type";
    public static final String EXTRA_NOTIF_DELAY = "notification_delay";
    public static final String EXTRA_USER_DISPLAY_NAME = "notification_name";
    public static final int NOTIF_TYPE_NONE = 0;
    public static final int NOTIF_TYPE_SESSION_EXPIRED = 1;
    public static final String NOTIF_CHANNEL_ID_SERVICE = "service_channel";
    public static final String NOTIF_CHANNEL_ID_SESSION_EXPIRED = "session_expired_channel";

    private String userDisplayName;
    private Timer timer = null;
    private TimerTask timerTask;
    private Handler handler;  // Used to run the TimeTask.

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // Setting the NotificationManager.
        NotificationManager notifManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID_SERVICE, "SERVICE", NotificationManager.IMPORTANCE_MIN);
        notifManager.createNotificationChannel(channel);

        // Schedule the TimerTask.
        int notifType = intent.getIntExtra(EXTRA_NOTIF_TYPE, NOTIF_TYPE_NONE);
        if (notifType == NOTIF_TYPE_SESSION_EXPIRED) {
            int notifDelay = intent.getIntExtra(EXTRA_NOTIF_DELAY, 0);
            userDisplayName = intent.getStringExtra(EXTRA_USER_DISPLAY_NAME);
            timer = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    handler.post(() -> {
                        RaiseNotification();
                    });
                }
            };
            handler = new Handler();
            timer.schedule(timerTask, notifDelay * 1000L);
        }

        startForeground(1, getForegroundNotification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Stop the TimerTask if exists.
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    private void RaiseNotification() {
        // Setting the NotificationManager.
        NotificationManager notifManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID_SESSION_EXPIRED, "SESSION_EXPIRED", NotificationManager.IMPORTANCE_HIGH);
        notifManager.createNotificationChannel(channel);

        // Creating the notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "default_notif_id");
        builder.setContentTitle("User session expired");
        builder.setContentText("Hi, " + (userDisplayName != null ? userDisplayName : "user") + " your login session has expired.");
        Intent tmpIntent = new Intent(this.getApplicationContext(), SignInActivity.class);
        PendingIntent pageToOpen = PendingIntent.getActivity(this.getApplicationContext(), 0, tmpIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pageToOpen);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIF_CHANNEL_ID_SESSION_EXPIRED);
        Notification createdNotif = builder.build();

        // Raising the session expired notification.
        notifManager.notify(2, createdNotif);

        // Stop the service.
        stopForeground(true);
        stopSelf();
    }

    private Notification getForegroundNotification() {
        Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "default_channel_id");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("User session");
        builder.setContentText("Checking session status...");
        builder.setChannelId(NOTIF_CHANNEL_ID_SERVICE);
        return builder.build();
    }
}