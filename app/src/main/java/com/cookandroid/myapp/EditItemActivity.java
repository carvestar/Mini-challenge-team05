package com.cookandroid.myapp;
//식재료 수정/삭제 화면

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {
    private EditText etFoodName;
    private Button btnSelectDate, btnSave, btnDelete, btnCancel;
    private ImageButton btnBack;

    // 선택된 날짜와 수정 대상의 ID
    private String selectedDate = "";
    private int itemId = -1; // Room의 id로 식별

    private FoodItem originalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etFoodName = findViewById(R.id.etFoodName);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        // 전달받은 데이터
        itemId = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String expiry = getIntent().getStringExtra("expiry");

        // 전달받은 이름과 유통기한을 UI에 표시
        if (name != null) etFoodName.setText(name);
        if (expiry != null) {
            selectedDate = expiry;
            btnSelectDate.setText(expiry);
        }

        // 날짜 선택
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        // 저장
        btnSave.setOnClickListener(v -> updateItem());

        // 삭제
        btnDelete.setOnClickListener(v -> deleteItem());

        // 뒤로가기 / 취소
        btnCancel.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
    }

    // 날짜 선택 다이얼로그 표시
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format(Locale.KOREA, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    btnSelectDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    // 수정(Room DB 업데이트)
    private void updateItem() {
        String name = etFoodName.getText().toString().trim();

        if (name.isEmpty() || selectedDate.isEmpty()) {
            etFoodName.setError("모든 항목을 입력해주세요.");
            return;
        }

        if (itemId == -1) {
            Toast.makeText(this, "수정할 항목 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            // 수정할 항목 객체 생성 및 ID 설정
            FoodItem updated = new FoodItem(name, selectedDate);
            updated.id = itemId;
            // DB에 업데이트 요청
            db.foodItemDao().update(updated);
            // UI 스레드에서 액티비티 종료
            runOnUiThread(this::finish);
        }).start();
    }

    // 삭제 (Room DB 삭제)
    private void deleteItem() {
        if (itemId == -1) {
            Toast.makeText(this, "삭제할 항목 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            // 삭제할 항목 객체 생성
            FoodItem toDelete = new FoodItem("", "");
            toDelete.id = itemId;
            // DB에 삭제 요청
            db.foodItemDao().delete(toDelete);
            // UI 스레드에서 액티비티 종료
            runOnUiThread(this::finish);
        }).start();
    }
}
