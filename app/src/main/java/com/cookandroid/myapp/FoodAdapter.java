package com.cookandroid.myapp;
// RecyclerView에서 식재료 항목을 표시하고 수정 버튼 처리하는 어댑터

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FoodItem> foodList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
    }

    public FoodAdapter(Context context, ArrayList<FoodItem> foodList, OnItemClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.tvFoodName.setText(item.getName());
        holder.tvExpiry.setText("유통기한: " + item.getExpiry());

        // 유통기한 임박 여부 표시 (예: 3일 이내 빨간 경고)
        if (item.isExpiringSoon()) {
            holder.tvWarning.setVisibility(View.VISIBLE);
        } else {
            holder.tvWarning.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvExpiry, tvWarning;
        Button btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            btnEdit = itemView.findViewById(R.id.btnEditFood);
        }
    }
}
