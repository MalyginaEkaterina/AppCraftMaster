package com.example.appcraftmaster.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.CategoryList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriesFragment extends Fragment {

    //private CategoriesViewModel categoriesViewModel;
    private RecyclerView recyclerViewCategories;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        CategoryList categoryList = ((MyApp) getActivity().getApplicationContext()).getCategoryList();
        CategoriesAdapter adapter = new CategoriesAdapter(categoryList.getCategories());
        recyclerViewCategories.setAdapter(adapter);
        adapter.setOnCategoryClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("path", appendPath(Collections.emptyList(), position));
            NavOptions.Builder builder = new NavOptions.Builder();
            NavOptions navOptions = builder.setEnterAnim(R.anim.slide_in_right).build();
            navController.navigate(R.id.nav_select_child_category, bundle, navOptions);
        });
        return view;
    }

    public static ArrayList<Integer> appendPath(List<Integer> path, Integer newValue) {
        ArrayList<Integer> ret = new ArrayList<>(path);
        ret.add(newValue);
        return ret;
    }
}
