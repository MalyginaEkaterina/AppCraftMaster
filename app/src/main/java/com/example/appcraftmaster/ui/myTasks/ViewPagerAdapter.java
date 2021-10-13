package com.example.appcraftmaster.ui.myTasks;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;


public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_ITEMS = 2;

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CustomerTasks();
            case 1:
                return new ExecutorTasks();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_ITEMS;
    }
}
