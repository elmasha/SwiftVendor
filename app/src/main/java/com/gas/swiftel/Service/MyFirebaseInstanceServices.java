package com.gas.swiftel.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gas.swiftel.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseInstanceServices extends FirebaseMessagingService {

    String click_ation;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getTitle();
        String  title2 = remoteMessage.getData().get("title");
        String  message = remoteMessage.getData().get("message");
        String  ord_id = remoteMessage.getData().get("order_id");


         click_ation = remoteMessage.getNotification().getClickAction();



        if (remoteMessage.getData().isEmpty()){


            ShowNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),click_ation,ord_id);

        }else {


            ShowNotification2(remoteMessage.getData(),click_ation);
        }



    }

    private void ShowNotification2(Map<String, String> data,String click_ation) {

        String title = data.get("title").toString();
        String message = data.get("message").toString();
        String orderID = data.get("order_id").toString();



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.vendorlogo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.DEFAULT_SOUND)
                .setCategory(Notification.CATEGORY_EMAIL)
                .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.popglass))
                .setColor(getResources().getColor(R.color.colorPrimary1));
        int notificationId = (int) System.currentTimeMillis();



        Intent resultintent = new Intent(click_ation);
        resultintent.putExtra("order_id",orderID);
        PendingIntent  resultPendingintent =  PendingIntent.getActivity(
                this,
                0,
                resultintent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingintent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,mBuilder.build());


        if (notificationManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                        "El_Tech_CH_01",
                        NotificationManager.IMPORTANCE_HIGH);

                AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();


                channel.setShowBadge(true);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setDescription(message);
                channel.setSound(defaultSoundUri,audioAttributes);
                channel.setVibrationPattern(new long[]{0,100});
                notificationManager.createNotificationChannel(channel);

            }

            notificationManager.notify(notificationId, mBuilder.build());
        }

    }

    private void ShowNotification(String title, String body,String click_ation,String ord_id) {


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.vendorlogo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.DEFAULT_SOUND)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.popglass))
                .setColor(getResources().getColor(R.color.colorPrimary1));
        int notificationId = (int) System.currentTimeMillis();



        Intent resultintent = new Intent(click_ation);
        resultintent.putExtra("order_id",ord_id);
        PendingIntent  resultPendingintent =  PendingIntent.getActivity(
                this,
                0,
                resultintent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingintent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,mBuilder.build());



    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {


        String OrdID = remoteMessage.getData().get("order_id");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("message");


        int notificationId = (int) System.currentTimeMillis();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.vendorlogo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.DEFAULT_SOUND)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.popglass))
                .setColor(getResources().getColor(R.color.colorPrimary1));



        Intent resultintent = new Intent(click_ation);
        resultintent.putExtra("order_id",OrdID);
        PendingIntent  resultPendingintent =  PendingIntent.getActivity(
                this,
                0,
                resultintent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingintent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,mBuilder.build());










    }




}
