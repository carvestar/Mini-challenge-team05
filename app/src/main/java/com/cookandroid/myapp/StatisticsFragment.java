package com.cookandroid.myapp;
// 통계 화면

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.cookandroid.myapp.AppDatabase;
import com.cookandroid.myapp.FoodItem;
import com.cookandroid.myapp.FoodItemDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StatisticsFragment extends Fragment {
    private TextView tvTotal, tvExpiring, tvSummary;
    private PieChart pieChart;

    private FoodItemDao foodItemDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // View 연결
        tvTotal = view.findViewById(R.id.tvTotalItems);
        tvExpiring = view.findViewById(R.id.tvExpiringItems);
        tvSummary = view.findViewById(R.id.tvSummary);
        pieChart = view.findViewById(R.id.pieChart);

        // DB 초기화
        foodItemDao = AppDatabase.getInstance(requireContext()).foodItemDao();

        // 데이터 업데이트
        loadStatistics();

        return view;
    }

    // DB에서 통계 데이터 불러오기
    private void loadStatistics() {
        new Thread(() -> {
            List<FoodItem> itemList = foodItemDao.getAllItems();
            int totalCount = itemList.size();
            int expiringSoonCount = 0;
            int expiringThisMonthCount = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();

            HashMap<String, Integer> categoryCountMap = new HashMap<>();

            for (FoodItem item : itemList) {
                // 임박 식품 판단
                try {
                    Date expiryDate = sdf.parse(item.getExpiry());
                    long diff = (expiryDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
                    if (diff >= 0 && diff <= 3) expiringSoonCount++;
                    if (diff >= 0 && isThisMonth(expiryDate)) expiringThisMonthCount++;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 카테고리 분류
                String category = getCategory(item.getName());
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
            }

            List<PieEntry> entries = new ArrayList<>();
            for (String category : categoryCountMap.keySet()) {
                entries.add(new PieEntry(categoryCountMap.get(category), category));
            }

            PieDataSet dataSet = new PieDataSet(entries, "카테고리별 분포");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData pieData = new PieData(dataSet);

            // final 변수로 람다에 전달
            final int finalTotal = totalCount;
            final int finalExpiringSoon = expiringSoonCount;
            final int finalExpiringMonth = expiringThisMonthCount;

            requireActivity().runOnUiThread(() -> {
                tvTotal.setText("전체 식재료: " + finalTotal + "개");
                tvExpiring.setText("유통기한 임박: " + finalExpiringSoon + "개");
                tvSummary.setText("이번 달 안에 소진해야 할 재료: " + finalExpiringMonth + "개");

                pieChart.setData(pieData);
                pieChart.invalidate();
            });
        }).start();
    }

    // 현재 월과 같은 달인지 확인
    private boolean isThisMonth(Date date) {
        Date now = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        return monthFormat.format(date).equals(monthFormat.format(now));
    }

    // 간단한 이름 기반 카테고리 분류
    private String getCategory(String name) {
        name = name.toLowerCase();
        if (name.contains("상추") || name.contains("양파") || name.contains("채소") || name.contains("오이")) return "채소류";
        if (name.contains("닭") || name.contains("소고기") || name.contains("돼지고기") || name.contains("고기")) return "육류";
        if (name.contains("우유") || name.contains("치즈") || name.contains("요거트")) return "유제품";
        return "기타";
    }
}
