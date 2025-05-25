package com.cookandroid.myapp;
// Room 데이터베이스 설정 클래스

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FoodItem.class}, version = 2) // DB에 포함될 엔티티와 버전 설정
public abstract class AppDatabase extends RoomDatabase {
    // 싱글톤 인스턴스를 저장할 변수
    private static AppDatabase INSTANCE;

    // DAO 추상 메서드 - FoodItem에 접근하기 위한 DAO 리턴
    public abstract FoodItemDao foodItemDao();

    // AppDatabase 인스턴스를 반환하는 메서드 (싱글톤 방식)
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            // RoomDatabase 빌더를 통해 DB 인스턴스 생성
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), // 앱 컨텍스트 사용 (메모리 누수 방지)
                            AppDatabase.class,               // DB 클래스 지정
                            "food_database")                 // DB 파일 이름 지정
                    .fallbackToDestructiveMigration()        // 스키마 변경 시 기존 데이터 삭제
                    .build();
        }
        return INSTANCE; // 생성된 DB 인스턴스 반환
    }
}
