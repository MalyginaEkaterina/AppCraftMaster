package com.example.appcraftmaster.ui.myTasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.TaskFull;

import java.util.List;

public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.MyTasksViewHolder> {
    private List<TaskFull> myTasks;
    private OnCloseBtnClickListener onCloseBtnClickListener;
    private OnRateBtnClickListener onRateBtnClickListener;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;

    interface OnCloseBtnClickListener {
        void onCloseBtnClick(int position);
    }

    interface OnRateBtnClickListener {
        void onRateBtnClick(int position);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnCloseBtnClickListener(OnCloseBtnClickListener onCloseBtnClickListener) {
        this.onCloseBtnClickListener = onCloseBtnClickListener;
    }

    public void setOnRateBtnClickListener(OnRateBtnClickListener onRateBtnClickListener) {
        this.onRateBtnClickListener = onRateBtnClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyTasksAdapter(List<TaskFull> myTasks, Fragment fragment) {
        this.myTasks = myTasks;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_task_item, parent, false);
        return new MyTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTasksViewHolder holder, int position) {
        TaskFull taskFull = myTasks.get(position);
        holder.textViewMyTaskTitle.setText(String.format("%s", taskFull.getTitle()));
        holder.textViewMyTaskDate.setText(String.format("%s %s", taskFull.getCreatedAt().substring(0, 10), taskFull.getCreatedAt().substring(11, 16)));
        holder.textViewMyTaskCat.setText(String.format("%s", taskFull.getOccupationName()));
        if (taskFull.getStatus() != null) {
            holder.textViewMyTaskStatus.setText(String.format("%s", TaskStatus.getTextById(taskFull.getStatus())));
            if (taskFull.getStatus().equals(TaskStatus.CLOSED.getId())) {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.GONE);
                holder.buttonRating.setVisibility(Button.GONE);
            } else if (taskFull.getStatus().equals(TaskStatus.DONE.getId())) {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.VISIBLE);
                if (taskFull.getAcceptedBid() != null) {
                    holder.buttonRating.setVisibility(Button.VISIBLE);
                } else {
                    holder.buttonRating.setVisibility(Button.GONE);
                }

            } else {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.black, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.VISIBLE);
                holder.buttonRating.setVisibility(Button.GONE);
            }
        }
        holder.ratingBarMyRate.setVisibility(RatingBar.GONE);
        if (taskFull.getAcceptedBid() != null) {
            if (taskFull.getAcceptedBid().getRating() != null) {
                holder.buttonRating.setVisibility(Button.GONE);
                holder.ratingBarMyRate.setRating(taskFull.getAcceptedBid().getRating());
                holder.ratingBarMyRate.setVisibility(RatingBar.VISIBLE);
            }
        }
        holder.buttonCloseTask.setOnClickListener(v -> {
            if (onCloseBtnClickListener != null) {
                onCloseBtnClickListener.onCloseBtnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTasks.size();
    }

    class MyTasksViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMyTaskTitle;
        private TextView textViewMyTaskDate;
        private TextView textViewMyTaskCat;
        private TextView textViewMyTaskStatus;
        private Button buttonCloseTask;
        private Button buttonRating;
        private RatingBar ratingBarMyRate;

        public MyTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMyTaskTitle = itemView.findViewById(R.id.textViewMyTaskTitle);
            textViewMyTaskDate = itemView.findViewById(R.id.textViewMyTaskDate);
            textViewMyTaskCat = itemView.findViewById(R.id.textViewMyTaskCat);
            textViewMyTaskStatus = itemView.findViewById(R.id.textViewMyTaskStatus);
            buttonCloseTask = itemView.findViewById(R.id.buttonCloseTask);
            buttonRating = itemView.findViewById(R.id.buttonRating);
            ratingBarMyRate = itemView.findViewById(R.id.ratingBarMyRate);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            buttonRating.setOnClickListener(v -> {
                if (onRateBtnClickListener != null) {
                    onRateBtnClickListener.onRateBtnClick(getAdapterPosition());
                }
            });
        }
    }
}
