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
import com.example.appcraftmaster.model.MyExecTask;
import com.example.appcraftmaster.model.OtherTaskFull;
import com.example.appcraftmaster.model.Status;
import com.example.appcraftmaster.model.TaskFull;
import com.example.appcraftmaster.model.UpdateTaskStatusDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyExecTasksViewModel extends AndroidViewModel {

    private MutableLiveData<List<MyExecTask>> myExecTasks = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshing = new MutableLiveData<>();

    public MyExecTasksViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<List<MyExecTask>> getMyExecTasks() {
        if (myExecTasks.getValue() == null) {
            updateMyExecTasks();
        }
        return myExecTasks;
    }

    public MutableLiveData<Boolean> getRefreshing() {
        return refreshing;
    }

    public void setRefreshing(Boolean refreshing) {
        this.refreshing.setValue(refreshing);
    }

    public void updateMyExecTasks() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/offers/accepteduserbids");
        System.out.println("Send req::" + url);
        JsonArrayRequest request = new Common.JsonArrayRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    myExecTasks.setValue(gson.fromJson(response.toString(), new TypeToken<List<MyExecTask>>() {
                    }.getType()));
                    refreshing.setValue(false);
                },
                error -> {
                    error.printStackTrace();
                    myExecTasks.setValue(null);
                    refreshing.setValue(false);
                });
        mRequestQueue.add(request);
    }

    public LiveData<Integer> doTask(int pos) {
        MutableLiveData<Integer> doStatus = new MutableLiveData<>();
        try {
            UpdateTaskStatusDto updateTaskStatusDto = new UpdateTaskStatusDto(myExecTasks.getValue().get(pos).getId(), TaskStatus.DONE.getId());
            Gson gson = new Gson();
            JSONObject updateTaskStatusJson = new JSONObject(gson.toJson(updateTaskStatusDto));
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/offers/set_status_done";
            JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                    Request.Method.PUT,
                    url,
                    updateTaskStatusJson,
                    response -> {
                        System.out.println(response);
                        doStatus.setValue(gson.fromJson(response.toString(), Status.class).getStatus());
                    },
                    error -> {
                        error.printStackTrace();
                        doStatus.setValue(StatusCode.STATUS_ERROR);
                    });
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doStatus;
    }

    public void updateStatus(int pos) {
        myExecTasks.getValue().get(pos).setStatus(TaskStatus.DONE.getId());
    }

}