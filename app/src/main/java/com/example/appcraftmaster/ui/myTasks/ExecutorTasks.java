package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.TaskFull;

import java.util.ArrayList;
import java.util.List;

public class ExecutorTasks extends Fragment {

    private RecyclerView recyclerViewMyExecTasks;
    private ProgressBar progressBarMyExecTasks;
    private MyExecTasksAdapter adapter;
    private TextView textViewMyExecTasksEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_executor_tasks, container, false);
        recyclerViewMyExecTasks = view.findViewById(R.id.recyclerViewMyExecTasks);
        recyclerViewMyExecTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBarMyExecTasks = view.findViewById(R.id.progressBarMyExecTasks);
        progressBarMyExecTasks.setVisibility(ProgressBar.INVISIBLE);
        textViewMyExecTasksEmpty = view.findViewById(R.id.textViewMyExecTasksEmpty);
        textViewMyExecTasksEmpty.setVisibility(TextView.INVISIBLE);

        //List<TaskFull> myTasks = ((MyApp) getActivity().getApplicationContext()).getMyTasks();
        adapter = new MyExecTasksAdapter(new ArrayList<TaskFull>());
        recyclerViewMyExecTasks.setAdapter(adapter);
        textViewMyExecTasksEmpty.setVisibility(TextView.VISIBLE);
        return view;
    }
}
