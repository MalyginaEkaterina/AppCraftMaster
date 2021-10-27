package com.example.appcraftmaster.ui.myTasks;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.model.Response;
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

public class TaskFullInfoViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Response>> responses;
    private Long taskId;

    public TaskFullInfoViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LiveData<ArrayList<Response>> getResponses() {
        if (responses == null) {
            updateResponses();
        }
        return this.responses;
    }

    public void updateResponses() {
        responses = new MutableLiveData<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/bids/offer_bids/%d", taskId);
        System.out.println("Send get responses request for task " + taskId);
        JsonArrayRequest request = new Common.JsonArrayRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    responses.setValue(gson.fromJson(response.toString(), new TypeToken<List<Response>>() {
                    }.getType()));
                },
                error -> {
                    error.printStackTrace();
                    responses.setValue(null);
                });
        mRequestQueue.add(request);
    }

    public LiveData<Integer> acceptResponse(Long respId) {
        MutableLiveData<Integer> acceptStatus = new MutableLiveData<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/bids/accept_bid/%d", respId);
        JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.POST,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    System.out.println(response);
                    acceptStatus.setValue(gson.fromJson(response.toString(), Status.class).getStatus());
                },
                error -> {
                    error.printStackTrace();
                    acceptStatus.setValue(StatusCode.STATUS_ERROR);
                });
        mRequestQueue.add(request);
        return acceptStatus;
    }
}