package com.example.appcraftmaster.ui.categories;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;

import java.util.List;

public class ChildCategoriesFragment extends Fragment {

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        //private CategoriesViewModel categoriesViewModel;
        RecyclerView recyclerViewCategories = view.findViewById(R.id.recyclerViewSelectCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        List<Integer> path = getArguments().getIntegerArrayList("path");
        Category category = getCategoryFromPath(path, getActivity());
        if (category != null && !category.getChild().isEmpty()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(category.getName());
            CategoriesAdapter adapter = new CategoriesAdapter(category.getChild());
            recyclerViewCategories.setAdapter(adapter);
            adapter.setOnCategoryClickListener(position -> {
                Category newCategory = category.getChild().get(position);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("path", CategoriesFragment.appendPath(path, position));
                NavOptions.Builder builder = new NavOptions.Builder();
                NavOptions navOptions = builder.setEnterAnim(R.anim.slide_in_right).build();
                if (newCategory.getChild().isEmpty()) {
                    navController.navigate(R.id.nav_add_task_info, bundle, navOptions);
                } else {
                    navController.navigate(R.id.nav_select_child_category, bundle, navOptions);
                }
            });
        } else {

        }
        return view;
    }

    public static Category getCategoryFromPath(List<Integer> path, Activity activity) {
        Category category = null;
        List<Category> childList = ((MyApp) activity.getApplicationContext()).getCategoryList().getCategories();
        for (int i = 0; i < path.size(); i++) {
            category = childList.get(path.get(i));
            childList = category.getChild();
        }
        return category;
    }
}