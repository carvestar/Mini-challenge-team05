package com.cookandroid.myapp;
// 식재료 객체 클래스 (이름, 유통기한 및 임박 여부 판단)

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "food_items") // Room 데이터베이스의 테이블 이름 지정
public class FoodItem {
    @PrimaryKey(autoGenerate = true) // 기본 키 자동 증가 설정
    public int id;

    @ColumnInfo(name = "name")
    private String name; // 식재료 이름

    @ColumnInfo(name = "expiry")
    private String expiry; // 유통기한 (문자열로 저장)

    @ColumnInfo(name = "iconResId") // 아이콘 리소스 ID 추가
    private int iconResId;


    // 생성자: 이름과 유통기한을 받아 초기화
    public FoodItem(String name, String expiry, int iconResId) {
        this.name = name;
        this.expiry = expiry;
        this.iconResId = iconResId;
    }

    // ID 반환
    public int getId() {
        return id;
    }

    // 이름 반환
    public String getName() {
        return name;
    }

    // 유통기한 반환
    public String getExpiry() {
        return expiry;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isExpiringSoon() {
        // 유통기한까지 3일 이하 남았는지 확인
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            Date expiryDate = sdf.parse(expiry);
            long diff = expiryDate.getTime() - System.currentTimeMillis();
            return diff <= 3 * 24 * 60 * 60 * 1000L; // 3일
        } catch (Exception e) {
            return false;
        }
    }
}

