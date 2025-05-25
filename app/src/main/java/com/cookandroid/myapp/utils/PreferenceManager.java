package com.cookandroid.myapp.utils;
// 알림 설정 저장하고 불러오기

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "noti_prefs";           // SharedPreferences 파일 이름
    private static final String KEY_ENABLED = "enabled";            // 알림 사용 여부 키
    private static final String KEY_DAYS_BEFORE = "days_before";    // 며칠 전 알림 설정 키

    // 알림 설정 저장
    public static void saveNotificationSettings(Context context, boolean enabled, int daysBefore) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_ENABLED, enabled)
                .putInt(KEY_DAYS_BEFORE, daysBefore)
                .apply();
    }

    // 알림 사용 여부 불러오기 (기본값: true)
    public static boolean isNotificationEnabled(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_ENABLED, true);
    }

    // 며칠 전 알림 설정 불러오기 (기본값: 1)
    public static int getDaysBefore(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_DAYS_BEFORE, 1);
    }
}
