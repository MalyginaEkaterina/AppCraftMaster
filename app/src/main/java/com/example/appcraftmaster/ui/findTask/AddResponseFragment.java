package com.example.appcraftmaster.ui.findTask;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.AddResponseDto;
import com.example.appcraftmaster.model.Status;
import com.example.appcraftmaster.model.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AddResponseFragment extends Fragment {

    private Long taskId;
    private Long respBegin = null;
    private Long respEnd = null;
    private TextInputEditText editTextAddRespPrice;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_response, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        Button buttonSaveResponse = view.findViewById(R.id.buttonSaveResponse);
        TextInputEditText editTextAddRespBegin = view.findViewById(R.id.editTextAddRespBegin);
        TextInputLayout textInputLayoutAddRespBegin = view.findViewById(R.id.textInputLayoutAddRespBegin);
        TextInputEditText editTextAddRespEnd = view.findViewById(R.id.editTextAddRespEnd);
        TextInputLayout textInputLayoutAddRespEnd = view.findViewById(R.id.textInputLayoutAddRespEnd);
        editTextAddRespPrice = view.findViewById(R.id.editTextAddRespPrice);

        textInputLayoutAddRespBegin.setEndIconOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_date_title)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                respBegin = selection;
                ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.of("UTC"));
                DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                editTextAddRespBegin.setText(simpleFormat.format(dateTime));
            });
            datePicker.show(getChildFragmentManager(), null);
        });

        textInputLayoutAddRespEnd.setEndIconOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_date_title)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                respEnd = selection;
                ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.of("UTC"));
                DateTimeFormatter simpleFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                editTextAddRespEnd.setText(simpleFormat.format(dateTime));
            });
            datePicker.show(getChildFragmentManager(), null);
        });

        taskId = getArguments().getLong("taskId");

        buttonSaveResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAddResponseReq();
            }
        });
        return view;
    }

    private void sendAddResponseReq() {
        if (editTextAddRespPrice.getText().toString().isEmpty() || respBegin == null || respEnd == null) {
            Toast.makeText(getContext(), R.string.toast_add_response_data, Toast.LENGTH_LONG).show();
        } else {
            try {
                AddResponseDto addResponseDto = new AddResponseDto(taskId, new BigDecimal(editTextAddRespPrice.getText().toString()), respBegin, respEnd);Gson gson = new Gson();
                JSONObject addResponseDtoJson = new JSONObject(gson.toJson(addResponseDto));
                RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
                String url = "http://10.0.2.2:8189/craftmaster/api/v1/offers/add_response";
                JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()),
                        Request.Method.POST,
                        url,
                        addResponseDtoJson,
                        response -> {
                            System.out.println(response);
                            Status status = gson.fromJson(response.toString(), Status.class);
                            if (status.getStatus().equals(StatusCode.STATUS_OK)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(R.string.dialog_add_response_msg);
                                builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                                    dialog.dismiss(); // Отпускает диалоговое окно
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                navController.popBackStack(R.id.nav_find_task, false);
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
