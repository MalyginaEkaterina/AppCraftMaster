package com.example.appcraftmaster.ui.categories;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    private List<Category> categories;
    private OnCategoryClickListener onCategoryClickListener;

    public CategoriesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryName.setText(String.format("%s", category.getName()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewCategoryName;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(itemView.getResources().getColor(R.color.secondaryLightColor, itemView.getContext().getTheme()));
                    if (onCategoryClickListener != null) {
                        onCategoryClickListener.onCategoryClick(getAdapterPosition());
                    }
                    v.setBackgroundColor(itemView.getResources().getColor(R.color.white, itemView.getContext().getTheme()));
                }
            });
        }
    }
}
