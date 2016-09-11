package com.example.apple.newsingit_project.service.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.apple.newsingit_project.MainActivity;
import com.example.apple.newsingit_project.R;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by apple on 2016. 9. 6..
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String FCM_NOTIFY_VALUE = "FCM_NOTIFY_VALUE";
    private static final String TAG = "MyGcmListenerService";

    /**
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);
        set_alarm_badge();
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     *
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_image)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void set_alarm_badge() {
        Log.d("json control", "notify receive");

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        //패키지 이름과 클래그 이름설정.//
        intent.putExtra("bage_count", 1);
        intent.putExtra("badge_count_package_name", getApplication().getPackageName());
        intent.putExtra("badge_count_class_name", getApplication().getClass().getName());

        getApplication().sendBroadcast(intent);
    }
}