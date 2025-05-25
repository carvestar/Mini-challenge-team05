package com.cookandroid.myapp;
// 홈 화면 프래그먼트: 식재료 목록을 보여주고 추가 버튼 제공

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;           // RecyclerView: 식재료 목록 표시
    private FoodAdapter adapter;                 // RecyclerView 어댑터
    private ArrayList<FoodItem> foodList;        // 표시할 식재료 리스트
    private AppDatabase db;                      // Room 데이터베이스 인스턴스

    // 기본 생성자
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Room DB 초기화
        db = AppDatabase.getInstance(requireContext());

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(getContext(), foodList);
        recyclerView.setAdapter(adapter);

        // 식재료 추가 버튼
        Button btnAddItem = view.findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddItemActivity.class);
            startActivity(intent);
        });

        // 데이터 로딩
        loadFoodItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFoodItems(); // 새로고침
    }

    // DB에서 식재료 불러오기
    private void loadFoodItems() {
        new AsyncTask<Void, Void, List<FoodItem>>() {
            @Override
            protected List<FoodItem> doInBackground(Void... voids) {
                return db.foodItemDao().getAllItems(); // DB에서 모든 항목 조회
            }

            @Override
            protected void onPostExecute(List<FoodItem> items) {
                foodList.clear();                     // 기존 목록 비우고
                foodList.addAll(items);               // 새로 불러온 항목 추가
                adapter.notifyDataSetChanged();       // RecyclerView 갱신
            }
        }.execute();
    }
}
