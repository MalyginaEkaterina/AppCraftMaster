package com.example.appcraftmaster.ui.findTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.databinding.FragmentFindTaskBinding;
import com.example.appcraftmaster.model.OtherTaskFull;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindTaskFragment extends Fragment {

    private FindTaskViewModel findTaskViewModel;
    private FragmentFindTaskBinding binding;
    private RecyclerView recyclerViewOtherTasks;
    private LinearLayoutManager mLayoutManager;
    private OtherTasksAdapter adapter;
    private List<OtherTaskFull> otherTasks;
    private ProgressBar progressBarFindTasks;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findTaskViewModel = new ViewModelProvider(this).get(FindTaskViewModel.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        binding = FragmentFindTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerViewOtherTasks = root.findViewById(R.id.recyclerViewOtherTasks);
        progressBarFindTasks = root.findViewById(R.id.progressBarFindTasks);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewOtherTasks.setLayoutManager(mLayoutManager);

        otherTasks = new ArrayList<>();
        adapter = new OtherTasksAdapter(otherTasks);
        recyclerViewOtherTasks.setAdapter(adapter);

        if (otherTasks.isEmpty()) {
            progressBarFindTasks.setVisibility(ProgressBar.VISIBLE);
        }
        getOtherTasks();

        recyclerViewOtherTasks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition == adapter.getItemCount() - 1) {
                    findTaskViewModel.updateOtherTasks();
                    getOtherTasks();
                }
            }
        });

        adapter.setOnItemClickListener(position -> {
            try {
                Gson gson = new Gson();
                JSONObject taskJson = new JSONObject(gson.toJson(otherTasks.get(position)));
                Bundle bundle = new Bundle();
                bundle.putString("otherTask", taskJson.toString());
                navController.navigate(R.id.nav_other_task_full_info, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    private void getOtherTasks() {
        findTaskViewModel.getOtherTasks().observe(getViewLifecycleOwner(), tasks -> {
            progressBarFindTasks.setVisibility(ProgressBar.INVISIBLE);
            otherTasks.clear();
            otherTasks.addAll(tasks);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}