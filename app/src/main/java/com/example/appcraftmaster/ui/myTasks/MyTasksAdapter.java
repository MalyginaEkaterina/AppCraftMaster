package com.example.appcraftmaster.ui.myTasks;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.TaskFull;

import java.util.List;

public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.MyTasksViewHolder> {
    private List<TaskFull> myTasks;
    private OnCloseBtnClickListener onCloseBtnClickListener;
    private OnItemClickListener onItemClickListener;

    interface OnCloseBtnClickListener {
        void onCloseBtnClick(int position);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnCloseBtnClickListener(OnCloseBtnClickListener onCloseBtnClickListener) {
        this.onCloseBtnClickListener = onCloseBtnClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyTasksAdapter(List<TaskFull> myTasks) {
        this.myTasks = myTasks;
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
        holder.textViewMyTaskCat.setText(String.format("%s", taskFull.getCategory()));
        if (taskFull.getStatus() != null) {
            holder.textViewMyTaskStatus.setText(String.format("%s", TaskStatus.getTextById(taskFull.getStatus())));
            if (taskFull.getStatus().equals(TaskStatus.CLOSED.getId())) {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.GONE);
            } else if (taskFull.getStatus().equals(TaskStatus.DONE.getId())) {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.VISIBLE);
            } else {
                holder.textViewMyTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.black, holder.itemView.getContext().getTheme()));
                holder.buttonCloseTask.setVisibility(Button.VISIBLE);
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

        public MyTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMyTaskTitle = itemView.findViewById(R.id.textViewMyTaskTitle);
            textViewMyTaskDate = itemView.findViewById(R.id.textViewMyTaskDate);
            textViewMyTaskCat = itemView.findViewById(R.id.textViewMyTaskCat);
            textViewMyTaskStatus = itemView.findViewById(R.id.textViewMyTaskStatus);
            buttonCloseTask = itemView.findViewById(R.id.buttonCloseTask);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
