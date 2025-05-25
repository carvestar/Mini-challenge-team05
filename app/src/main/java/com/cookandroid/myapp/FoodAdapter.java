package com.cookandroid.myapp;
// 식재료 목록 RecyclerView에 표시

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FoodItem> foodList;

    // 생성자: context와 식재료 리스트 초기화
    public FoodAdapter(Context context, ArrayList<FoodItem> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    // 식재료 리스트 갱신 및 RecyclerView 갱신 알림
    public void setFoodList(ArrayList<FoodItem> newList) {
        this.foodList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder를 생성하고 item_food.xml과 연결
    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    // ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        // 식재료 이름 및 유통기한 표시
        holder.tvFoodName.setText(item.getName());
        holder.tvExpiry.setText("유통기한: " + item.getExpiry());

        // 유통기한 경고 여부
        if (item.isExpiringSoon()) {
            holder.tvWarning.setVisibility(View.VISIBLE); // "임박" 경고 표시
            holder.tvExpiry.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.tvWarning.setVisibility(View.GONE); // 경고 숨김
            holder.tvExpiry.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        // 기본 이미지 설정
        holder.ivFoodImage.setImageResource(item.getIconResId()); // 수정

        // 수정 버튼 클릭 시 EditItemActivity 호출
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditItemActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("name", item.getName());
            intent.putExtra("expiry", item.getExpiry());
            intent.putExtra("iconResId", item.getIconResId()); // 추가
            context.startActivity(intent);
        });
    }

    // 항목 개수 반환
    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvExpiry, tvWarning;
        Button btnEdit;
        ImageView ivFoodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            btnEdit = itemView.findViewById(R.id.btnEditFood);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
        }
    }
}
