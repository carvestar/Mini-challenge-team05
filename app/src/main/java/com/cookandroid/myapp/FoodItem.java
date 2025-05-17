package com.cookandroid.myapp;
// 식재료 객체 클래스 (이름, 유통기한 및 임박 여부 판단)

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class FoodItem {
    private String name;
    private String expiry;

    public FoodItem(String name, String expiry) {
        this.name = name;
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public String getExpiry() {
        return expiry;
    }

    public boolean isExpiringSoon() {
        // 유통기한까지 3일 이하 남았는지 확인
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            Date expiryDate = sdf.parse(expiry);
            long diff = expiryDate.getTime() - System.currentTimeMillis();
            return diff <= 3 * 24 * 60 * 60 * 1000L;
        } catch (Exception e) {
            return false;
        }
    }
}

