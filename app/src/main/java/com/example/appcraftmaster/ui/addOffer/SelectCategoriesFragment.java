package com.example.appcraftmaster.ui.addOffer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.ui.categories.CategoriesAdapter;
import com.example.appcraftmaster.ui.categories.CategoriesFragment;
import com.example.appcraftmaster.ui.categories.ChildCategoriesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCategoriesFragment extends Fragment {

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_categories, container, false);
        //private CategoriesViewModel categoriesViewModel;
        RecyclerView recyclerViewCategories = view.findViewById(R.id.recyclerViewAddOrSelectCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        List<Integer> path = getArguments().getIntegerArrayList("path");
        List<Category> categoryList;
        if (path.isEmpty()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.select_categories_title_all_cat);
            categoryList = ((MyApp) getActivity().getApplicationContext()).getCategoryList().getCategories();
        } else {
            Category category = ChildCategoriesFragment.getCategoryFromPath(path, getActivity());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(category.getName());
            categoryList = category.getChild();
        }
        List<Category> addedCategories = ((MyApp) getActivity().getApplicationContext()).getProfileList().stream().map(m -> m.getCategory()).collect(Collectors.toList());
        SelectCategoriesAdapter adapter = new SelectCategoriesAdapter(categoryList, addedCategories);
        recyclerViewCategories.setAdapter(adapter);
        adapter.setOnCategoryClickListener(position -> {
            Category newCategory = categoryList.get(position);
            if (!newCategory.getChild().isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("path", CategoriesFragment.appendPath(path, position));
                NavOptions.Builder builder = new NavOptions.Builder();
                NavOptions navOptions = builder.setEnterAnim(R.anim.slide_in_right).build();
                navController.navigate(R.id.nav_add_profile_select_category, bundle, navOptions);
            }
        });
        adapter.setOnCategorySelectListener(position -> {
            NavBackStackEntry entry = navController.getBackStackEntry(R.id.nav_add_profile);
            entry.getSavedStateHandle().set("path", CategoriesFragment.appendPath(path, position));
            navController.popBackStack(R.id.nav_add_profile, false);
        });
        return view;
    }
}