package com.example.apple.newsingit_project.service.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.apple.newsingit_project.MainActivity;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SplashActivity;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by apple on 2016. 9. 6..
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String FCM_NOTIFY_VALUE = "FCM_NOTIFY_VALUE";
    private static final String TAG = "MyGcmListenerService";

    /**
     * 공유 프래퍼런스 관련 변수
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//

    int badge_count;
    /**
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //해당 부분의 key값은 서버의 데이터 타입과 맞추어준다//
        String title = data.getString("title");
        String message = data.getString("body");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Body: " + message);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);

        set_alarm_badge(); //배지를 등록//
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
                .setSmallIcon(R.mipmap.logo_144)
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

        //배지의 카운트를 공유저장소로부터 가져온다.//
        badge_count = PropertyManager.getInstance().get_badge_number();
        badge_count++; //0으로 되어있기에 1로 만들어준다.//
        //패키지 이름과 클래그 이름설정.//

        intent.putExtra("badge_count", badge_count);

        //문자열로 대입 가능//
        intent.putExtra("badge_count_package_name", getApplicationContext().getPackageName()); //패키지 이름//
        //배지의 적용은 맨 처음 띄우는 화면을 기준으로 한다.//
        intent.putExtra("badge_count_class_name", SplashActivity.class.getName()); //맨 처음 띄우는 화면 이름//

        //변경된 값으로 다시 공유 저장소 값 초기화.//
        PropertyManager.getInstance().setBadge_number(badge_count);

        sendBroadcast(intent);
    }
}