package com.example.appcraftmaster.ui.addTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.Offer;
import com.example.appcraftmaster.model.Status;
import com.example.appcraftmaster.ui.categories.ChildCategoriesFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskInfoFragment extends Fragment {

    private NavController navController;
    private EditText editTextAddTaskCategory;
    private EditText editTextAddTaskName;
    private EditText editTextAddTaskDescription;
    private Category category;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_info, container, false);
        editTextAddTaskCategory = (EditText) view.findViewById(R.id.editTextAddTaskCategory);
        editTextAddTaskName = (EditText) view.findViewById(R.id.editTextAddTaskName);
        editTextAddTaskDescription = (EditText) view.findViewById(R.id.editTextAddTaskDescription);

        Button button = (Button) view.findViewById(R.id.buttonAddTask);
        button.setOnClickListener(v -> addTask());

        List<Integer> path = getArguments().getIntegerArrayList("path");
        if (path != null) {
            category = ChildCategoriesFragment.getCategoryFromPath(path, getActivity());
            editTextAddTaskCategory.setText(category.getName());
            editTextAddTaskCategory.setEnabled(false);
        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        return view;
    }

    public void addTask() {
        if (editTextAddTaskName.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_add_task_name, Toast.LENGTH_LONG).show();
        } else {
            try {
                Offer offer = new Offer(editTextAddTaskName.getText().toString(), editTextAddTaskDescription.getText().toString(), category.getId());
                Gson gson = new Gson();
                JSONObject offerJson = new JSONObject(gson.toJson(offer));
                RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
                String url = "http://10.0.2.2:8189/craftmaster/api/v1/offers";
                JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()),
                        Request.Method.PUT,
                        url,
                        offerJson,
                        response -> {
                            System.out.println(response);
                            Status status = gson.fromJson(response.toString(), Status.class);
                            if (status.getStatus().equals(StatusCode.STATUS_OK)) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.dialog_add_task_msg);
                                //builder.setCancelable(true);
                                builder.setPositiveButton(R.string.dialog_add_task_ok, (dialog, which) -> {
                                    dialog.dismiss(); // Отпускает диалоговое окно
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                NavOptions.Builder builderNav = new NavOptions.Builder();
                                NavOptions navOptions = builderNav.setEnterAnim(R.anim.slide_in_left).setPopUpTo(R.id.nav_add_task, true).build();
                                navController.navigate(R.id.nav_add_task, null, navOptions);
                            }
                        },
                        error -> error.printStackTrace());
                mRequestQueue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}