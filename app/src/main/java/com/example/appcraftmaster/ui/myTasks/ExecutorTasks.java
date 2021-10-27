package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.MyExecTask;
import com.example.appcraftmaster.model.TaskFull;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExecutorTasks extends Fragment {

    private RecyclerView recyclerViewMyExecTasks;
    private ProgressBar progressBarMyExecTasks;
    private MyExecTasksAdapter adapter;
    private TextView textViewMyExecTasksEmpty;
    private MyExecTasksViewModel myExecTasksViewModel;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_executor_tasks, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        myExecTasksViewModel = new ViewModelProvider(getActivity()).get(MyExecTasksViewModel.class);
        SwipeRefreshLayout swipeRefreshMyExecTasks = view.findViewById(R.id.swipeRefreshMyExecTasks);
        recyclerViewMyExecTasks = view.findViewById(R.id.recyclerViewMyExecTasks);
        recyclerViewMyExecTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBarMyExecTasks = view.findViewById(R.id.progressBarMyExecTasks);
        progressBarMyExecTasks.setVisibility(ProgressBar.VISIBLE);
        textViewMyExecTasksEmpty = view.findViewById(R.id.textViewMyExecTasksEmpty);
        textViewMyExecTasksEmpty.setVisibility(TextView.INVISIBLE);

        List<MyExecTask> myTasks = new ArrayList<>();
        adapter = new MyExecTasksAdapter(myTasks);
        recyclerViewMyExecTasks.setAdapter(adapter);

        myExecTasksViewModel.getMyExecTasks().observe(getViewLifecycleOwner(), myExecTasks -> {
            if (myExecTasks.isEmpty()) {
                textViewMyExecTasksEmpty.setVisibility(TextView.VISIBLE);
            } else {
                textViewMyExecTasksEmpty.setVisibility(TextView.INVISIBLE);
            }
            progressBarMyExecTasks.setVisibility(ProgressBar.INVISIBLE);
            myTasks.clear();
            myTasks.addAll(myExecTasks);
            adapter.notifyDataSetChanged();
        });

        swipeRefreshMyExecTasks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myExecTasksViewModel.setRefreshing(true);
                myExecTasksViewModel.getRefreshing().observe(getViewLifecycleOwner(), refreshing -> {
                    if (!refreshing) {
                        swipeRefreshMyExecTasks.setRefreshing(false);
                    }
                });
                myExecTasksViewModel.updateMyExecTasks();
            }
        });

        adapter.setOnDoneBtnClickListener(position -> doTask(position));

        adapter.setOnItemClickListener(position -> {
            try {
                Gson gson = new Gson();
                JSONObject taskJson = new JSONObject(gson.toJson(myTasks.get(position)));
                Bundle bundle = new Bundle();
                bundle.putString("execTask", taskJson.toString());
                navController.navigate(R.id.nav_exec_task_full_info, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    public void doTask(int pos) {
        progressBarMyExecTasks.setVisibility(ProgressBar.VISIBLE);
        myExecTasksViewModel.doTask(pos).observe(getViewLifecycleOwner(), status -> {
            progressBarMyExecTasks.setVisibility(ProgressBar.INVISIBLE);
            System.out.println("status::" + status);
            if (status.equals(StatusCode.STATUS_OK)) {
                myExecTasksViewModel.updateStatus(pos);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
