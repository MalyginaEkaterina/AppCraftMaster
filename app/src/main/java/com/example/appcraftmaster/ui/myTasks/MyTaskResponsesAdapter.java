package com.example.appcraftmaster.ui.myTasks;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Response;
import com.example.appcraftmaster.model.TaskFull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MyTaskResponsesAdapter extends RecyclerView.Adapter<MyTaskResponsesAdapter.MyTasksResponsesViewHolder>{
    private List<Response> responses;
    private TaskFull task;
    private OnAcceptBtnClickListener onAcceptBtnClickListener;

    public MyTaskResponsesAdapter(List<Response> responses, TaskFull task) {
        this.responses = responses;
        this.task = task;
    }

    interface OnAcceptBtnClickListener {
        void onAcceptBtnClick(int position);
    }

    public void setOnAcceptBtnClickListener(OnAcceptBtnClickListener onAcceptBtnClickListener) {
        this.onAcceptBtnClickListener = onAcceptBtnClickListener;
    }

    @NonNull
    @Override
    public MyTasksResponsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_response_item, parent, false);
        return new MyTasksResponsesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTasksResponsesViewHolder holder, int position) {
        Response response = responses.get(position);

        holder.buttonAccepted.setVisibility(Button.GONE);
        if (task.getStatus().equals(TaskStatus.NEW.getId())) {
            holder.buttonAcceptResponse.setVisibility(Button.VISIBLE);
        } else {
            holder.buttonAcceptResponse.setVisibility(Button.GONE);
            if (task.getAcceptedBid() != null) {
                if (task.getAcceptedBid().getId().equals(response.getId())) {
                    holder.buttonAccepted.setVisibility(Button.VISIBLE);
                    holder.buttonAccepted.setEnabled(false);
                }
            }
        }

        holder.textViewTaskRespName.setText(String.format("%s", response.getExecutor().getName()));
        holder.textViewTaskRespRatingIcon.setText(Html.fromHtml("&#9733;", Html.FROM_HTML_MODE_LEGACY));
        holder.textViewTaskRespRatingValue.setText(String.format("%s", response.getExecutor().getRating()));
        holder.textViewResponsePrice.setText(String.format("%s", response.getPrice()));

        ZonedDateTime dateTimeBeg = ZonedDateTime.ofInstant(Instant.ofEpochMilli(response.getDateBeg()), ZoneId.of("UTC"));
        ZonedDateTime dateTimeEnd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(response.getDateEnd()), ZoneId.of("UTC"));
        DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());

        holder.textViewResponseBegin.setText(String.format("%s", simpleFormat.format(dateTimeBeg)));
        holder.textViewResponseEnd.setText(String.format("%s", simpleFormat.format(dateTimeEnd)));
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    class MyTasksResponsesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTaskRespName;
        private TextView textViewTaskRespRatingIcon;
        private TextView textViewTaskRespRatingValue;
        private TextView textViewResponsePrice;
        private TextView textViewResponseBegin;
        private TextView textViewResponseEnd;
        private Button buttonAcceptResponse;
        private Button buttonAccepted;

        public MyTasksResponsesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskRespName = itemView.findViewById(R.id.textViewTaskRespName);
            textViewTaskRespRatingIcon = itemView.findViewById(R.id.textViewTaskRespRatingIcon);
            textViewTaskRespRatingValue = itemView.findViewById(R.id.textViewTaskRespRatingValue);
            textViewResponsePrice = itemView.findViewById(R.id.textViewResponsePrice);
            textViewResponseBegin = itemView.findViewById(R.id.textViewResponseBegin);
            textViewResponseEnd = itemView.findViewById(R.id.textViewResponseEnd);
            buttonAcceptResponse = itemView.findViewById(R.id.buttonAcceptResponse);
            buttonAccepted = itemView.findViewById(R.id.buttonAccepted);
            buttonAcceptResponse.setOnClickListener(v -> {
                if (onAcceptBtnClickListener != null) {
                    onAcceptBtnClickListener.onAcceptBtnClick(getAdapterPosition());
                }
            });
        }
    }
}
