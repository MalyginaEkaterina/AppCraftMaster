package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.databinding.FragmentMyTasksBinding;
import com.google.android.material.tabs.TabLayout;

public class MyTasksFragment extends Fragment {

    private MyTasksViewModel myTaskViewModel;
    private FragmentMyTasksBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myTaskViewModel = new ViewModelProvider(this).get(MyTasksViewModel.class);

        binding = FragmentMyTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //viewPager = (ViewPager) root.findViewById(R.id.viewpagerTasks);

        //tabLayout = (TabLayout) root.findViewById(R.id.tabLayoutTasks);
        //tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}