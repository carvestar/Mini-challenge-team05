package com.cookandroid.myapp;
// 알림 설정

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.cookandroid.myapp.utils.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

// Fragment 상단 import 추가
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;

public class NotificationSettingsFragment extends Fragment {
    private SwitchCompat switchNotification; // 알림 스위치
    private RadioGroup radioGroup;           // 며칠 전 알림 보낼지 선택
    private Button btnSave;                  // 저장 버튼

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);

        // 뷰 참조 가져오기
        switchNotification = view.findViewById(R.id.switch_notification);
        radioGroup = view.findViewById(R.id.radio_group_days);
        btnSave = view.findViewById(R.id.btn_save);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        // 저장된 설정 불러오기
        boolean enabled = PreferenceManager.isNotificationEnabled(requireContext());
        int days = PreferenceManager.getDaysBefore(requireContext());

        // 불러온 값으로 UI 상태 설정
        switchNotification.setChecked(enabled);
        if (days == 3) radioGroup.check(R.id.radio_3);
        else if (days == 5) radioGroup.check(R.id.radio_5);
        else radioGroup.check(R.id.radio_1); // 기본값 1일 전

        // 저장 버튼 클릭 시 설정 저장
        btnSave.setOnClickListener(v -> {
            boolean newEnabled = switchNotification.isChecked();
            int selectedDays = 1;
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_3) selectedDays = 3;
            else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_5) selectedDays = 5;

            PreferenceManager.saveNotificationSettings(requireContext(), newEnabled, selectedDays);
            Toast.makeText(getContext(), "설정이 저장되었습니다", Toast.LENGTH_SHORT).show();

            // 테스트용: 3초 뒤 알림 예약
            if (newEnabled) {
                AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(requireContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        requireContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                long triggerTime = System.currentTimeMillis() + 3 * 1000; // 3초 뒤
                // alarmManager는 이미 선언되어 있다고 했으니, 다시 선언하지 않고 사용
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    } else {
                        // intent도 이미 있다면 이름 바꾸기 (예: requestIntent)
                        Intent requestIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(requestIntent);
                    }
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }

            }
        });

        return view;
    }
}