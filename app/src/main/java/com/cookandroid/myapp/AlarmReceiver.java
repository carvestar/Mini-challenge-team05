package com.cookandroid.myapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return; // 권한 없으면 알림 안 보냄
        }

        // 알림 채널 설정 (Android 8.0 이상 필요)
        String channelId = "default_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // 알림 만들기
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.noti) // 알림 아이콘 (없으면 기본 아이콘으로 대체)
                .setContentTitle("테스트 알림")
                .setContentText("3초 뒤 울리는 테스트 알림입니다!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(1, builder.build());
            }
        } catch (SecurityException e) {
            e.printStackTrace(); // 로그 확인용
        }
    }
}
