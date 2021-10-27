package com.example.appcraftmaster.ui.addOffer;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.Occupation;

import java.util.List;

public class SelectCategoriesAdapter extends RecyclerView.Adapter<SelectCategoriesAdapter.SelectedCategoriesViewHolder> {
    private List<Category> categories;
    private List<Integer> addedCategoriesId;
    private OnCategoryClickListener onCategoryClickListener;
    private OnCategorySelectListener onCategorySelectListener;

    public SelectCategoriesAdapter(List<Category> categories, List<Integer> addedCategories) {
        this.categories = categories;
        this.addedCategoriesId = addedCategories;
    }

    interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    interface OnCategorySelectListener {
        void onCategorySelect(int position);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public void setOnCategorySelectListener(OnCategorySelectListener onCategorySelectListener) {
        this.onCategorySelectListener = onCategorySelectListener;
    }

    @NonNull
    @Override
    public SelectedCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_item, parent, false);
        return new SelectedCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedCategoriesViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.buttonSelectCategory.setText(Html.fromHtml("&#65291;", Html.FROM_HTML_MODE_LEGACY));
        holder.textViewCategoryName.setText(String.format("%s", category.getName()));
        if (category.getChild().isEmpty()) {
            holder.textViewCatItemCur.setVisibility(View.INVISIBLE);
        }
        if (addedCategoriesId.contains(category.getId())) {
            holder.buttonSelectCategory.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class SelectedCategoriesViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewCategoryName;
        private TextView textViewCatItemCur;
        private Button buttonSelectCategory;
        private ConstraintLayout constLayoutSelectCat;

        public SelectedCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewSelectCategoryName);
            textViewCatItemCur = itemView.findViewById(R.id.textViewSelectCatItemCur);
            buttonSelectCategory = itemView.findViewById(R.id.buttonSelectCategory);
            buttonSelectCategory.setOnClickListener(v -> {
                if (onCategorySelectListener != null) {
                    onCategorySelectListener.onCategorySelect(getAdapterPosition());
                }
            });
            constLayoutSelectCat = itemView.findViewById(R.id.constLayoutSelectCat);
            constLayoutSelectCat.setOnClickListener(v -> {
                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(getAdapterPosition());
                }
            });
        }
    }
}
