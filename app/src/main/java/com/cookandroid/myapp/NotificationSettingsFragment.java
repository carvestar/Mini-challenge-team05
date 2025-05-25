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
        });

        return view;
    }
}