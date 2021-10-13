package com.example.appcraftmaster.ui.findTask;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.OtherTaskFull;
import com.google.gson.Gson;

public class OtherTaskFullInfoFragment extends Fragment {

    private OtherTaskFull task;

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_task_full_info, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        TextView textViewTaskFullInfoName = view.findViewById(R.id.textViewOtherTaskFullInfoName);
        TextView textViewTaskFullInfoCat = view.findViewById(R.id.textViewOtherTaskFullInfoCat);
        TextView textViewTaskFullInfoCreated = view.findViewById(R.id.textViewOtherTaskFullInfoCreated);
        TextView textViewTaskFullInfoDescr = view.findViewById(R.id.textViewOtherTaskFullInfoDescr);
        TextView otherTaskInfoExecutorName = view.findViewById(R.id.otherTaskInfoExecutorName);
        TextView otherTaskInfoExecutorRating = view.findViewById(R.id.otherTaskInfoExecutorRating);
        Button buttonAddResponse = view.findViewById(R.id.buttonAddResponse);

        String taskStr = getArguments().getString("otherTask");
        Gson gson = new Gson();
        task = gson.fromJson(taskStr, OtherTaskFull.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(task.getTitle());

        textViewTaskFullInfoName.setText(task.getTitle());
        textViewTaskFullInfoCat.setText(task.getCategory());
        textViewTaskFullInfoDescr.setText(task.getDescription());
        textViewTaskFullInfoCreated.setText(String.format("%s %s", task.getCreatedAt().substring(0, 10), task.getCreatedAt().substring(11, 16)));
        otherTaskInfoExecutorName.setText(task.getCustomer().getName());
        otherTaskInfoExecutorRating.setText(Html.fromHtml("&#9733;", Html.FROM_HTML_MODE_LEGACY) + String.format(" %.2f",task.getCustomer().getRating()));

        buttonAddResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("taskId", task.getId());
                navController.navigate(R.id.nav_add_response, bundle);
            }
        });
        return view;
    }
}
