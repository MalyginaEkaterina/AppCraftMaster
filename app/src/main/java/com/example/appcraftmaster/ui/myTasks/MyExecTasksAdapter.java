package com.example.appcraftmaster.ui.myTasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.TaskFull;

import java.util.List;

public class MyExecTasksAdapter extends RecyclerView.Adapter<MyExecTasksAdapter.MyExecTasksViewHolder>{
    private List<TaskFull> myExecTasks;
    private OnDoneBtnClickListener onDoneBtnClickListener;

    interface OnDoneBtnClickListener {
        void onDoneBtnClick(int position);
    }

    public void setOnDoneBtnClickListener(OnDoneBtnClickListener onDoneBtnClickListener) {
        this.onDoneBtnClickListener = onDoneBtnClickListener;
    }

    public MyExecTasksAdapter(List<TaskFull> myTasks) {
        this.myExecTasks = myTasks;
    }

    @NonNull
    @Override
    public MyExecTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_exec_task_item, parent, false);
        return new MyExecTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyExecTasksViewHolder holder, int position) {
        TaskFull taskFull = myExecTasks.get(position);
        holder.textViewMyExecTaskTitle.setText(String.format("%s", taskFull.getTitle()));
        holder.textViewMyExecTaskDate.setText(String.format("%s %s", taskFull.getCreatedAt().substring(0,10), taskFull.getCreatedAt().substring(11,16)));
        holder.textViewMyExecTaskCat.setText(String.format("%s", taskFull.getCategory()));
        if (taskFull.getStatus() != null) {
            holder.textViewMyExecTaskStatus.setText(String.format("%s", TaskStatus.getTextById(taskFull.getStatus())));
            if (taskFull.getStatus().equals(TaskStatus.CLOSED.getId())) {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, holder.itemView.getContext().getTheme()));
                holder.buttonDoneTask.setVisibility(Button.GONE);
            } else if (taskFull.getStatus().equals(TaskStatus.DONE.getId())) {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green, holder.itemView.getContext().getTheme()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return myExecTasks.size();
    }

    class MyExecTasksViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMyExecTaskTitle;
        private TextView textViewMyExecTaskDate;
        private TextView textViewMyExecTaskCat;
        private TextView textViewMyExecTaskStatus;
        private Button buttonDoneTask;

        public MyExecTasksViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewMyExecTaskTitle = itemView.findViewById(R.id.textViewMyExecTaskTitle);
            textViewMyExecTaskDate = itemView.findViewById(R.id.textViewMyExecTaskDate);
            textViewMyExecTaskCat = itemView.findViewById(R.id.textViewMyExecTaskCat);
            textViewMyExecTaskStatus = itemView.findViewById(R.id.textViewMyExecTaskStatus);
            buttonDoneTask = itemView.findViewById(R.id.buttonDoneTask);
            buttonDoneTask.setOnClickListener(v -> {
                if (onDoneBtnClickListener != null) {
                    onDoneBtnClickListener.onDoneBtnClick(getAdapterPosition());
                }
            });
        }
    }
}
