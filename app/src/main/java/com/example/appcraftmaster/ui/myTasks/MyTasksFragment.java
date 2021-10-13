package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.databinding.FragmentMyTasksBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyTasksFragment extends Fragment {

    private FragmentMyTasksBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager = view.findViewById(R.id.pagerTasks);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = view.findViewById(R.id.tabLayoutTasks);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Я заказчик");
                    } else if (position == 1) {
                        tab.setText("Я исполнитель");
                    }
                }).attach();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}