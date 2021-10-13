package com.example.appcraftmaster.ui.myTasks;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.AddProfilesDto;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.model.Status;
import com.example.appcraftmaster.model.TaskFull;
import com.example.appcraftmaster.model.UpdateTaskStatusDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerTasksViewModel extends AndroidViewModel {

    public CustomerTasksViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<TaskFull>> updateMyTasks() {
        MutableLiveData<ArrayList<TaskFull>> myTasks = new MutableLiveData<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = "http://10.0.2.2:8189/craftmaster/api/v1/offers/my";
        System.out.println("Send update req");
        JsonArrayRequest request = new Common.JsonArrayRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    myTasks.setValue(gson.fromJson(response.toString(), new TypeToken<List<TaskFull>>() {
                    }.getType()));
                },
                error -> {
                    error.printStackTrace();
                    myTasks.setValue(null);
                });
        mRequestQueue.add(request);
        return myTasks;
    }

    public LiveData<Integer> closeTask(Long id) {
        MutableLiveData<Integer> closeStatus = new MutableLiveData<>();
        try {
            UpdateTaskStatusDto updateTaskStatusDto = new UpdateTaskStatusDto(id, TaskStatus.CLOSED.getId());
            Gson gson = new Gson();
            JSONObject updateTaskStatusJson = new JSONObject(gson.toJson(updateTaskStatusDto));
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/offers/update_status";
            JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                    Request.Method.POST,
                    url,
                    updateTaskStatusJson,
                    response -> {
                        System.out.println(response);
                        closeStatus.setValue(gson.fromJson(response.toString(), Status.class).getStatus());
                    },
                    error -> {
                        error.printStackTrace();
                        closeStatus.setValue(StatusCode.STATUS_ERROR);
                    });
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return closeStatus;
    }


}
