package com.jeet.digitalattendance.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jeet.digitalattendance.Activity.AttendanceActivity;
import com.jeet.digitalattendance.Activity.FingerScanner;
import com.jeet.digitalattendance.Common.Common;
import com.jeet.digitalattendance.R;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();
            final String title = data.get("title");
            final String message = data.get("message");
            if (title.equals("attendance")) {
//                showNotification(title, message);
                Intent intent = new Intent(this, FingerScanner.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("code", data.get("code"));
                intent.putExtra("teacher_id", data.get("teacher_id"));
                intent.putExtra("teacher_name", data.get("teacher_name"));
                Common.currentTeacherId=data.get("teacher_id");
                Common.currentTeacherName=data.get("teacher_name");
                Common.currentCode=data.get("code");
                startActivity(intent);
            }
        }
    }

    private void showNotification(String title, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "DA")
                        .setSmallIcon(R.drawable.leadinglogo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("DA", title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GRAY);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}
