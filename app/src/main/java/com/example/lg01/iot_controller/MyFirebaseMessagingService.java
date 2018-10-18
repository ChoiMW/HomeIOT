package com.example.lg01.iot_controller;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.StringCharacterIterator;
import java.util.Map;
import java.util.StringTokenizer;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG ="MyFirebase";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "new Token:" +token);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From : " + remoteMessage.getFrom());

        Log.d(TAG, "Message data payload: " + remoteMessage.getData());


        if (remoteMessage.getNotification()!= null) {
            Log.d(TAG, "Msg Notify Body : " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getData());

        }


    }


    private void sendNotification(String messageBody,Map<String, String>Data) {
        Intent intent = new Intent(this, PopupFindActivity.class);//이동할 액티비티
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification",messageBody);
        intent.putExtra("Device",Data.get("device"));
        intent.putExtra("Command",Data.get("command"));
        intent.putExtra("Time",Data.get("time"));
        intent.putExtra("Day",Data.get("day"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,"Notification")
                        .setSmallIcon(R.drawable.home_smallicon)
                        .setContentTitle("AI using AI")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_VIBRATE);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Notification","Notificaion",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("channel description");


            //알림에 대한 불빛
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);

            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});

            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(notificationChannel);
       }
        notificationManager.notify(0, notificationBuilder.build());

    }

}
