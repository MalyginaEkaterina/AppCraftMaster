package com.example.appcraftmaster.ui.myTasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.MyExecTask;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MyExecTasksAdapter extends RecyclerView.Adapter<MyExecTasksAdapter.MyExecTasksViewHolder>{
    private List<MyExecTask> myExecTasks;
    private OnDoneBtnClickListener onDoneBtnClickListener;
    private OnItemClickListener onItemClickListener;

    interface OnDoneBtnClickListener {
        void onDoneBtnClick(int position);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnDoneBtnClickListener(OnDoneBtnClickListener onDoneBtnClickListener) {
        this.onDoneBtnClickListener = onDoneBtnClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyExecTasksAdapter(List<MyExecTask> myTasks) {
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
        MyExecTask execTask = myExecTasks.get(position);
        holder.textViewMyExecTaskTitle.setText(String.format("%s", execTask.getTitle()));
        holder.textViewMyExecTaskDate.setText(String.format("%s %s", execTask.getCreatedAt().substring(0,10), execTask.getCreatedAt().substring(11,16)));
        holder.textViewMyExecTaskCat.setText(String.format("%s", execTask.getOccupationName()));
        if (execTask.getStatus() != null) {
            holder.textViewMyExecTaskStatus.setText(String.format("%s", TaskStatus.getTextById(execTask.getStatus())));
            if (execTask.getStatus().equals(TaskStatus.CLOSED.getId())) {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.red, holder.itemView.getContext().getTheme()));
                holder.buttonDoneTask.setVisibility(Button.GONE);
            } else if (execTask.getStatus().equals(TaskStatus.DONE.getId())) {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.green, holder.itemView.getContext().getTheme()));
                holder.buttonDoneTask.setVisibility(Button.GONE);
            } else if (execTask.getStatus().equals(TaskStatus.ASSIGNED.getId())) {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.black, holder.itemView.getContext().getTheme()));
                holder.buttonDoneTask.setVisibility(Button.VISIBLE);
            } else {
                holder.textViewMyExecTaskStatus.setTextColor(holder.itemView.getResources().getColor(R.color.black, holder.itemView.getContext().getTheme()));
                holder.buttonDoneTask.setVisibility(Button.GONE);
            }
        }
        holder.textViewExecTaskResponsePrice.setText(String.format("%s", execTask.getAcceptedBid().getPrice()));

        ZonedDateTime dateTimeBeg = ZonedDateTime.ofInstant(Instant.ofEpochMilli(execTask.getAcceptedBid().getDateBeg()), ZoneId.of("UTC"));
        ZonedDateTime dateTimeEnd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(execTask.getAcceptedBid().getDateEnd()), ZoneId.of("UTC"));
        DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());

        holder.textViewExecTaskResponseBegin.setText(String.format("%s", simpleFormat.format(dateTimeBeg)));
        holder.textViewExecTaskResponseEnd.setText(String.format("%s", simpleFormat.format(dateTimeEnd)));
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
        private TextView textViewExecTaskResponsePrice;
        private TextView textViewExecTaskResponseBegin;
        private TextView textViewExecTaskResponseEnd;
        private Button buttonDoneTask;

        public MyExecTasksViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewMyExecTaskTitle = itemView.findViewById(R.id.textViewMyExecTaskTitle);
            textViewMyExecTaskDate = itemView.findViewById(R.id.textViewMyExecTaskDate);
            textViewMyExecTaskCat = itemView.findViewById(R.id.textViewMyExecTaskCat);
            textViewMyExecTaskStatus = itemView.findViewById(R.id.textViewMyExecTaskStatus);
            textViewExecTaskResponsePrice = itemView.findViewById(R.id.textViewExecTaskResponsePrice);
            textViewExecTaskResponseBegin = itemView.findViewById(R.id.textViewExecTaskResponseBegin);
            textViewExecTaskResponseEnd = itemView.findViewById(R.id.textViewExecTaskResponseEnd);
            buttonDoneTask = itemView.findViewById(R.id.buttonDoneTask);
            buttonDoneTask.setOnClickListener(v -> {
                if (onDoneBtnClickListener != null) {
                    onDoneBtnClickListener.onDoneBtnClick(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
