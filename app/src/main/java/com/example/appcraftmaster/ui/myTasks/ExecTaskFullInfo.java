package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.MyExecTask;
import com.google.gson.Gson;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ExecTaskFullInfo extends Fragment {

    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exec_task_full_info, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        String taskStr = getArguments().getString("execTask");
        Gson gson = new Gson();
        MyExecTask task = gson.fromJson(taskStr, MyExecTask.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(task.getTitle());

        TextView textViewExecTaskFullInfoName = view.findViewById(R.id.textViewExecTaskFullInfoName);
        TextView textViewExecTaskFullInfoCat = view.findViewById(R.id.textViewExecTaskFullInfoCat);
        TextView textViewExecTaskFullInfoCreated = view.findViewById(R.id.textViewExecTaskFullInfoCreated);
        TextView textViewExecTaskFullInfoDescr = view.findViewById(R.id.textViewExecTaskFullInfoDescr);
        TextView ExecTaskFullResponsePrice = view.findViewById(R.id.ExecTaskFullResponsePrice);
        TextView ExecTaskFullResponseBegin = view.findViewById(R.id.ExecTaskFullResponseBegin);
        TextView ExecTaskFullResponseEnd = view.findViewById(R.id.ExecTaskFullResponseEnd);
        TextView execTaskInfoCustomerName = view.findViewById(R.id.execTaskInfoCustomerName);
        TextView execTaskInfoCustomerRating = view.findViewById(R.id.execTaskInfoCustomerRating);
        CardView cardViewCustomer = view.findViewById(R.id.cardViewCustomer);

        textViewExecTaskFullInfoName.setText(task.getTitle());
        textViewExecTaskFullInfoCat.setText(task.getOccupationName());
        textViewExecTaskFullInfoCreated.setText(String.format("%s %s", task.getCreatedAt().substring(0, 10), task.getCreatedAt().substring(11, 16)));
        textViewExecTaskFullInfoDescr.setText(task.getDescription());
        ExecTaskFullResponsePrice.setText(String.format("%s", task.getAcceptedBid().getPrice()));

        ZonedDateTime dateTimeBeg = ZonedDateTime.ofInstant(Instant.ofEpochMilli(task.getAcceptedBid().getDateBeg()), ZoneId.of("UTC"));
        ZonedDateTime dateTimeEnd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(task.getAcceptedBid().getDateEnd()), ZoneId.of("UTC"));
        DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());

        ExecTaskFullResponseBegin.setText(String.format("%s", simpleFormat.format(dateTimeBeg)));
        ExecTaskFullResponseEnd.setText(String.format("%s", simpleFormat.format(dateTimeEnd)));

        execTaskInfoCustomerName.setText(task.getCustomer().getName());
        execTaskInfoCustomerRating.setText(Html.fromHtml("&#9733;", Html.FROM_HTML_MODE_LEGACY) + String.format(" %.2f", task.getCustomer().getRating()));

        cardViewCustomer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("customerId", task.getCustomer().getId());
            navController.navigate(R.id.nav_customer_full_info, bundle);
        });
        return view;
    }
}
