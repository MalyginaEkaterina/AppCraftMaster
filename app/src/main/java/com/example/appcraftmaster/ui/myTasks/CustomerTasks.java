package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MainActivity;
import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.TaskFull;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_tasks, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        customerTasksViewModel = new ViewModelProvider(getActivity()).get(CustomerTasksViewModel.class);
        recyclerViewMyTasks = view.findViewById(R.id.recyclerViewMyTasks);
        recyclerViewMyTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBarMyTasks = view.findViewById(R.id.progressBarMyTasks);
        progressBarMyTasks.setVisibility(ProgressBar.INVISIBLE);
        textViewMyTasksEmpty = view.findViewById(R.id.textViewMyTasksEmpty);
        textViewMyTasksEmpty.setVisibility(TextView.INVISIBLE);


        List<TaskFull> myTasks = ((MyApp) getActivity().getApplicationContext()).getMyTasks();
        adapter = new MyTasksAdapter(myTasks);
        recyclerViewMyTasks.setAdapter(adapter);
        if (myTasks.isEmpty()) {
            textViewMyTasksEmpty.setVisibility(TextView.VISIBLE);
        }
        if (((MyApp) getActivity().getApplicationContext()).getNeedUpdateMyTasks()) {
            updateTasks();
        }
        adapter.setOnCloseBtnClickListener(position -> {
            TaskFull closableTask = myTasks.get(position);
            closeTask(closableTask.getId(), position);
        });

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
        progressBarMyTasks.setVisibility(ProgressBar.VISIBLE);
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
        });
    }
}
