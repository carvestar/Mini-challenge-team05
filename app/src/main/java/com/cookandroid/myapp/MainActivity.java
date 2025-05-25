package com.cookandroid.myapp;
// 앱의 메인 액티비티: 하단 탭으로 프래그먼트 전환 제어

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 하단 네비게이션 바

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 앱 시작 시 기본 프래그먼트 로드 (식재료 목록)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_list); // 홈을 기본 선택
        }

        // 메뉴 클릭 이벤트 처리
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new NotificationSettingsFragment();  // 알림 설정
            } else if (id == R.id.nav_list) {
                selectedFragment = new HomeFragment();                  // 식재료 목록
            } else if (id == R.id.nav_settings) {
                selectedFragment = new StatisticsFragment();            // 통계 화면
            } else {
                return false;
            }

            loadFragment(selectedFragment); // 프래그먼트 교체
            return true;
        });
    }

    // 선택된 프래그먼트를 fragment_container에 로드
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
