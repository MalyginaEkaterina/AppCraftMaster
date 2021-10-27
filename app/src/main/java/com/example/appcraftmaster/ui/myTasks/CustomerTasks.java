package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.TaskFull;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CustomerTasks extends Fragment {

    private CustomerTasksViewModel customerTasksViewModel;
    private RecyclerView recyclerViewMyTasks;
    private ProgressBar progressBarMyTasks;
    private MyTasksAdapter adapter;
    private TextView textViewMyTasksEmpty;
    private NavController navController;
    private Boolean isRefreshing;
    private SwipeRefreshLayout swipeRefreshMyTasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_tasks, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        swipeRefreshMyTasks = view.findViewById(R.id.swipeRefreshMyTasks);
        customerTasksViewModel = new ViewModelProvider(getActivity()).get(CustomerTasksViewModel.class);
        recyclerViewMyTasks = view.findViewById(R.id.recyclerViewMyTasks);
        recyclerViewMyTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBarMyTasks = view.findViewById(R.id.progressBarMyTasks);
        progressBarMyTasks.setVisibility(ProgressBar.INVISIBLE);
        textViewMyTasksEmpty = view.findViewById(R.id.textViewMyTasksEmpty);
        textViewMyTasksEmpty.setVisibility(TextView.INVISIBLE);
        isRefreshing = false;


        List<TaskFull> myTasks = ((MyApp) getActivity().getApplicationContext()).getMyTasks();
        adapter = new MyTasksAdapter(myTasks, this);
        recyclerViewMyTasks.setAdapter(adapter);
        if (myTasks.isEmpty()) {
            textViewMyTasksEmpty.setVisibility(TextView.VISIBLE);
        }
        if (((MyApp) getActivity().getApplicationContext()).getNeedUpdateMyTasks()) {
            progressBarMyTasks.setVisibility(ProgressBar.VISIBLE);
            updateTasks();
        }
        adapter.setOnCloseBtnClickListener(position -> {
            TaskFull closableTask = myTasks.get(position);
            closeTask(closableTask.getId(), position);
        });

        adapter.setOnRateBtnClickListener(position -> {
            TaskFull taskFull = myTasks.get(position);
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
            View myView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rating, null, false);
            RatingBar ratingBarSaveRate = myView.findViewById(R.id.ratingBarSaveRate);
            materialAlertDialogBuilder.setView(myView)
                    .setTitle("Оцените работу")
                    .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                        customerTasksViewModel.rateTask(taskFull.getAcceptedBid().getId(), ratingBarSaveRate.getRating())
                                .observe(getViewLifecycleOwner(), status -> {
                                    System.out.println("status::" + status);
                                    if (status.equals(StatusCode.STATUS_OK)) {
                                        ((MyApp) getActivity().getApplicationContext()).setTaskRating(position, ratingBarSaveRate.getRating());
                                        adapter.notifyDataSetChanged();
                                    }
                                    dialog.dismiss();
                                });
                    })
                    .show();});

        adapter.setOnItemClickListener(position -> {
            try {
                Gson gson = new Gson();
                JSONObject taskJson = new JSONObject(gson.toJson(myTasks.get(position)));
                Bundle bundle = new Bundle();
                bundle.putString("task", taskJson.toString());
                bundle.putInt("taskPos", position);
                navController.navigate(R.id.nav_task_full_info, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        swipeRefreshMyTasks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing) {
                    isRefreshing = true;
                    updateTasks();
                }
            }
        });
        return view;
    }

    public void closeTask(Long id, int pos) {
        progressBarMyTasks.setVisibility(ProgressBar.VISIBLE);
        customerTasksViewModel.closeTask(id).observe(getViewLifecycleOwner(), status -> {
            progressBarMyTasks.setVisibility(ProgressBar.INVISIBLE);
            System.out.println("status::" + status);
            if (status.equals(StatusCode.STATUS_OK)) {
                ((MyApp) getActivity().getApplicationContext()).setTaskStatusClose(pos);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void updateTasks() {
        textViewMyTasksEmpty.setVisibility(TextView.INVISIBLE);
        customerTasksViewModel.updateMyTasks().observe(getViewLifecycleOwner(), myTasks -> {
            progressBarMyTasks.setVisibility(ProgressBar.INVISIBLE);
            if (myTasks != null) {
                ((MyApp) getActivity().getApplicationContext()).setMyTasks(myTasks);
                ((MyApp) getActivity().getApplicationContext()).setNeedUpdateMyTasks(false);
                adapter.notifyDataSetChanged();
                if (myTasks.isEmpty()) {
                    textViewMyTasksEmpty.setVisibility(TextView.VISIBLE);
                }
            } else {
                Toast.makeText(getContext(), R.string.toast_server_error, Toast.LENGTH_LONG).show();
            }
            isRefreshing = false;
            swipeRefreshMyTasks.setRefreshing(false);
        });
    }
}
