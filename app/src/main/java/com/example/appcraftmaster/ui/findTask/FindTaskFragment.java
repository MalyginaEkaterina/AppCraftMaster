package com.example.appcraftmaster.ui.findTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private ProgressBar progressBarFindTasks;
    private NavController navController;
    private boolean isPageLoading;

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
        SwipeRefreshLayout swipeRefreshOtherTasks = root.findViewById(R.id.swipeRefreshOtherTasks);

        progressBarFindTasks.setVisibility(ProgressBar.INVISIBLE);
        isPageLoading = false;
        adapter = new OtherTasksAdapter(findTaskViewModel.getOtherTasks());
        recyclerViewOtherTasks.setAdapter(adapter);

        getNewTasksPage();

        if (findTaskViewModel.getPage().equals(1)) {
            progressBarFindTasks.setVisibility(ProgressBar.VISIBLE);
        }

        swipeRefreshOtherTasks.setOnRefreshListener(() -> {
            findTaskViewModel.refreshAll();
            isPageLoading = true;
            findTaskViewModel.getNewPage().observe(getViewLifecycleOwner(), tasksPage -> {
                findTaskViewModel.addOtherTasks(tasksPage);
                adapter.notifyDataSetChanged();
                isPageLoading = false;
                swipeRefreshOtherTasks.setRefreshing(false);
            });
        });

        recyclerViewOtherTasks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (!isPageLoading && lastVisibleItemPosition == adapter.getItemCount() - 1) {
                    isPageLoading = true;
                    getNewTasksPage();
                }
            }
        });

        adapter.setOnItemClickListener(position -> {
            try {
                Gson gson = new Gson();
                JSONObject taskJson = new JSONObject(gson.toJson(findTaskViewModel.getOtherTasks().get(position)));
                Bundle bundle = new Bundle();
                bundle.putString("otherTask", taskJson.toString());
                navController.navigate(R.id.nav_other_task_full_info, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    private void getNewTasksPage() {
        if (!findTaskViewModel.getWasLastPage()) {
            findTaskViewModel.getNewPage().observe(getViewLifecycleOwner(), tasksPage -> {
                progressBarFindTasks.setVisibility(ProgressBar.INVISIBLE);
                findTaskViewModel.addOtherTasks(tasksPage);
                adapter.notifyDataSetChanged();
                isPageLoading = false;
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}