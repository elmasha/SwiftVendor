package com.gas.swiftel.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.gas.swiftel.R;

public class OreoAndAboveNotification extends ContextWrapper {


    private static final String ID = "some_id";
    private static final  String  NAME = "FirebaseAPP";

    private NotificationManager notificationManager;


    public OreoAndAboveNotification(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel notificationChannel =new NotificationChannel(ID,NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManger().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManger(){

        if (notificationManager == null){

            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification (String title,
                                                 String body,
                                                 PendingIntent pIntent,
                                                 Uri soundUri,
                                                 String icon){
     return new Notification.Builder(this,getString(R.string.default_notification_channel_id))
     .setContentIntent(pIntent)
     .setContentTitle(title)
     .setContentText(body)
     .setSound(soundUri)
     .setAutoCancel(true)
     .setSmallIcon(Integer.parseInt(icon));
    }
}
