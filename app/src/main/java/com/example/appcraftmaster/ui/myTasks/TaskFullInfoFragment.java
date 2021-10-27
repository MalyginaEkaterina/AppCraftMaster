package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.Response;
import com.example.appcraftmaster.model.TaskFull;

import java.util.ArrayList;
import java.util.List;

public class TaskFullInfoFragment extends Fragment {

    private TaskFull task;
    private TaskFullInfoViewModel taskFullInfoViewModel;
    private RecyclerView recyclerViewTaskResponse;
    private MyTaskResponsesAdapter adapter;
    private TextView textViewTaskResponseEmpty;
    private ProgressBar progressBarTaskResponse;
    private List<Response> taskResponses;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_task_full_info, container, false);

//        String taskStr = getArguments().getString("task");
//        Gson gson = new Gson();
//        task = gson.fromJson(taskStr, TaskFull.class);
        int taskPos = getArguments().getInt("taskPos");
        task = ((MyApp) getActivity().getApplicationContext()).getMyTasks().get(taskPos);

        taskFullInfoViewModel = new ViewModelProvider(this).get(TaskFullInfoViewModel.class);
        taskFullInfoViewModel.setTaskId(task.getId());

        recyclerViewTaskResponse = view.findViewById(R.id.recyclerViewTaskResponse);
        recyclerViewTaskResponse.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView textViewTaskFullInfoName = view.findViewById(R.id.textViewTaskFullInfoName);
        TextView textViewTaskFullInfoCat = view.findViewById(R.id.textViewTaskFullInfoCat);
        TextView textViewTaskFullInfoCreated = view.findViewById(R.id.textViewTaskFullInfoCreated);
        TextView textViewTaskFullInfoStatus = view.findViewById(R.id.textViewTaskFullInfoStatus);
        TextView textViewTaskFullInfoDescr = view.findViewById(R.id.textViewTaskFullInfoDescr);
        textViewTaskResponseEmpty = view.findViewById(R.id.textViewTaskResponseEmpty);
        progressBarTaskResponse = view.findViewById(R.id.progressBarTaskResponse);

        taskResponses = new ArrayList<>();
        adapter = new MyTaskResponsesAdapter(taskResponses, task);
        recyclerViewTaskResponse.setAdapter(adapter);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(task.getTitle());

        textViewTaskFullInfoName.setText(task.getTitle());
        textViewTaskFullInfoCat.setText(task.getOccupationName());
        textViewTaskFullInfoDescr.setText(task.getDescription());
        textViewTaskFullInfoCreated.setText(String.format("%s %s", task.getCreatedAt().substring(0, 10), task.getCreatedAt().substring(11, 16)));
        textViewTaskFullInfoStatus.setText(String.format("%s", TaskStatus.getTextById(task.getStatus())));

        getTaskResponses();

        adapter.setOnAcceptBtnClickListener(position -> {
            progressBarTaskResponse.setVisibility(ProgressBar.VISIBLE);
            taskFullInfoViewModel.acceptResponse(taskResponses.get(position).getId()).observe(getViewLifecycleOwner(), status -> {
                progressBarTaskResponse.setVisibility(ProgressBar.INVISIBLE);
                System.out.println("status::" + status);
                if (status.equals(StatusCode.STATUS_OK)) {
                    ((MyApp) getActivity().getApplicationContext()).setTaskStatusAssigned(taskPos, taskResponses.get(position));
                    textViewTaskFullInfoStatus.setText(String.format("%s", TaskStatus.getTextById(task.getStatus())));
                    adapter.notifyDataSetChanged();
                }
            });
        });

        return view;
    }

    public void getTaskResponses() {
        progressBarTaskResponse.setVisibility(ProgressBar.VISIBLE);
        textViewTaskResponseEmpty.setVisibility(TextView.INVISIBLE);
        taskFullInfoViewModel.getResponses().observe(getViewLifecycleOwner(), responses -> {
            progressBarTaskResponse.setVisibility(ProgressBar.INVISIBLE);
            if (responses != null) {
                taskResponses.clear();
                taskResponses.addAll(responses);
                adapter.notifyDataSetChanged();
                if (responses.isEmpty()) {
                    textViewTaskResponseEmpty.setVisibility(TextView.VISIBLE);
                }
            } else {
                Toast.makeText(getContext(), R.string.toast_server_error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
