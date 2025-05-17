package com.cookandroid.myapp;
//식재료 추가 화면

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
    private EditText etFoodName;
    private Button btnSelectDate, btnSave, btnCancel;
    private ImageButton btnBack;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etFoodName = findViewById(R.id.etFoodName);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveFoodItem());
        btnCancel.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
    }

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

    private void saveFoodItem() {
        String name = etFoodName.getText().toString().trim();

        if (name.isEmpty() || selectedDate.isEmpty()) {
            etFoodName.setError("모든 항목을 입력해주세요.");
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("expiry", selectedDate);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
