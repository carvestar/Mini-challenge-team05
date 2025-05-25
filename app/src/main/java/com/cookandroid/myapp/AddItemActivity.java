package com.cookandroid.myapp;
// 식재료 추가 화면 - 사용자가 식재료 이름과 유통기한을 입력하여 Room DB에 저장하는 액티비티

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    // UI 요소 선언
    private EditText etFoodName;
    private Button btnSelectDate, btnSave, btnCancel;
    private ImageButton btnBack;

    // 사용자가 선택한 날짜를 저장할 변수
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // XML에서 정의한 뷰들을 연결
        etFoodName = findViewById(R.id.etFoodName);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        // XML에서 정의한 뷰들을 연결
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        // 저장 버튼 클릭 시 DB에 저장
        btnSave.setOnClickListener(v -> saveFoodItem());
        // 취소 및 뒤로가기 버튼 클릭 시 액티비티 종료
        btnCancel.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
    }

    // 날짜 선택 다이얼로그를 보여주는 메서드
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        // DatePickerDialog 생성 및 설정
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // 선택된 날짜를 "yyyy-MM-dd" 형식으로 저장
                    selectedDate = String.format(Locale.KOREA, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    btnSelectDate.setText(selectedDate); // 버튼에 선택한 날짜 표시
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show(); // 날짜 선택 다이얼로그 표시
    }

    // 사용자가 입력한 식재료 정보를 DB에 저장하는 메서드
    private void saveFoodItem() {
        String name = etFoodName.getText().toString().trim();

        // 이름 또는 날짜가 비어 있는 경우 오류 메시지 표시
        if (name.isEmpty() || selectedDate.isEmpty()) {
            etFoodName.setError("모든 항목을 입력해주세요.");
            return;
        }

        // Room을 통한 데이터 저장
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            FoodItem item = new FoodItem(name, selectedDate);
            db.foodItemDao().insert(item); // 식재료 DB에 삽입

            runOnUiThread(() -> {
                setResult(Activity.RESULT_OK); // 결과 설정 (생략 가능)
                finish();
            });
        }).start();
    }
}
