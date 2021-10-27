package com.example.appcraftmaster.ui.addOffer;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.Occupation;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.ui.WorkExp;
import com.example.appcraftmaster.ui.categories.ChildCategoriesFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddProfileFragment extends Fragment {
    private NavController navController;
    private TextInputEditText editTextAddProfileCat;
    private TextInputEditText editTextAddProfileDescr;
    private AutoCompleteTextView spinnerWorkExp;
    private Button addProfileButtonAddCat;
    private Category selectedCategory;

    private WorkExp workExp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_profile, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        editTextAddProfileCat = view.findViewById(R.id.editTextAddProfileCat);
        editTextAddProfileDescr = view.findViewById(R.id.editTextAddProfileDescr);
        spinnerWorkExp = view.findViewById(R.id.spinnerWorkExp);

        spinnerWorkExp.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item, WorkExp.values()));
        spinnerWorkExp.setOnItemClickListener((parent, v, position, id) -> workExp = (WorkExp) parent.getItemAtPosition(position));
        addProfileButtonAddCat = view.findViewById(R.id.addProfileButtonAddCat);
        addProfileButtonAddCat.setText(Html.fromHtml("&#65291;", Html.FROM_HTML_MODE_LEGACY));
        addProfileButtonAddCat.setOnClickListener(v -> addCategory());

        Button saveProfile = view.findViewById(R.id.buttonSaveProfile);
        saveProfile.setOnClickListener(v -> saveProfile());
        return view;
    }

    private void saveProfile() {
        if (editTextAddProfileCat.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.add_profile_toast_input_cat, Toast.LENGTH_LONG).show();
        } else  if (workExp == null) {
            Toast.makeText(getContext(), R.string.add_profile_toast_input_work_exp, Toast.LENGTH_LONG).show();
        } else {
            Profile newProfile = new Profile(null, new Occupation(selectedCategory), workExp.getId(), editTextAddProfileDescr.getText().toString());
            ((MyApp) getActivity().getApplicationContext()).addInProfileList(newProfile);
            navController.navigateUp();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        List<Integer> path = navController.getCurrentBackStackEntry().getSavedStateHandle().get("path");
        if (path != null) {
            selectedCategory = ChildCategoriesFragment.getCategoryFromPath(path, getActivity());
            editTextAddProfileCat.setText(selectedCategory.getName());
        }
    }

    private void addCategory() {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("path", new ArrayList<Integer>());
        NavOptions.Builder builder = new NavOptions.Builder();
        NavOptions navOptions = builder.setEnterAnim(R.anim.slide_in_right).build();
        navController.navigate(R.id.nav_add_profile_select_category, bundle, navOptions);
    }
}
