package com.cookandroid.myapp;
// 식재료 목록 및 추가/수정 화면 이동 기능

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADD = 1;
    private static final int REQUEST_EDIT = 2;

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private ArrayList<FoodItem> foodList;
    private Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodList = new ArrayList<>();
        foodList.add(new FoodItem("우유", "2025-05-25"));
        foodList.add(new FoodItem("달걀", "2025-05-20"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FoodAdapter(this, foodList, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                FoodItem item = foodList.get(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("expiry", item.getExpiry());
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });
        recyclerView.setAdapter(adapter);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String expiry = data.getStringExtra("expiry");
            foodList.add(new FoodItem(name, expiry));
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_EDIT) {
            int position = data.getIntExtra("position", -1);
            if (position == -1) return;

            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String expiry = data.getStringExtra("expiry");
                foodList.set(position, new FoodItem(name, expiry));
                adapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_FIRST_USER) { // 삭제 처리
                foodList.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    }
}