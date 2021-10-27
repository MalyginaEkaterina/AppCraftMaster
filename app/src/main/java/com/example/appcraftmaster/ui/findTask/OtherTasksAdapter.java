package com.example.appcraftmaster.ui.findTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.OtherTaskFull;

import java.util.List;

public class OtherTasksAdapter extends RecyclerView.Adapter<OtherTasksAdapter.OtherTasksViewHolder> {
    private List<OtherTaskFull> otherTasks;
    private OnItemClickListener onItemClickListener;

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OtherTasksAdapter(List<OtherTaskFull> otherTasks) {
        this.otherTasks = otherTasks;
    }

    @NonNull
    @Override
    public OtherTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_task_item, parent, false);
        return new OtherTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherTasksViewHolder holder, int position) {
        OtherTaskFull otherTask = otherTasks.get(position);
        holder.textViewOtherTaskTitle.setText(String.format("%s", otherTask.getTitle()));
        holder.textViewOtherTaskCat.setText(String.format("%s", otherTask.getOccupationName()));

    }

    @Override
    public int getItemCount() {
        return otherTasks.size();
    }

    class OtherTasksViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOtherTaskTitle;
        private TextView textViewOtherTaskCat;

        public OtherTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOtherTaskTitle = itemView.findViewById(R.id.textViewOtherTaskTitle);
            textViewOtherTaskCat = itemView.findViewById(R.id.textViewOtherTaskCat);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
