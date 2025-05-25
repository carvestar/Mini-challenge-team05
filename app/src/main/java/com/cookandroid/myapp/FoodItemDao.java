package com.cookandroid.myapp;
// Room 데이터베이스에 접근하기 위한 DAO 인터페이스

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodItemDao {
    // 식재료 하나 삽입
    @Insert
    void insert(FoodItem item);
    // 식재료 하나 삽입
    @Query("SELECT * FROM food_items")
    List<FoodItem> getAllItems();
    // 기존 식재료 수정
    @Update
    void update(FoodItem item);
    // 특정 식재료 삭제
    @Delete
    void delete(FoodItem item);
    // ID로 특정 식재료 삭제
    @Query("DELETE FROM food_items WHERE id = :id")
    void deleteById(int id);
    // 모든 식재료 삭제
    @Query("DELETE FROM food_items")
    void deleteAll();
}
