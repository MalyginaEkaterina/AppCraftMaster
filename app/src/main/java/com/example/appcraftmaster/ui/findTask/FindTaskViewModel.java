package com.example.appcraftmaster.ui.findTask;

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
import com.example.appcraftmaster.model.OtherTaskFull;
import com.example.appcraftmaster.model.TaskFull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

public class FindTaskViewModel extends AndroidViewModel {

    private MutableLiveData<List<OtherTaskFull>> otherTasks = new MutableLiveData<>();
    private MutableLiveData<Integer> page = new MutableLiveData<>();
    private static final Integer PAGE_SIZE = 5;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();

    public FindTaskViewModel(@NonNull @NotNull Application application) {
        super(application);
        page.setValue(1);
        isLoading.setValue(false);
        isLastPage.setValue(false);
    }

    public MutableLiveData<List<OtherTaskFull>> getOtherTasks() {
        if (otherTasks.getValue() == null) {
            getNewPage();
        }
        return otherTasks;
    }

    public void updateOtherTasks() {
        if (!isLoading.getValue() && !isLastPage.getValue()) {
            getNewPage();
        }
    }

    public void getNewPage() {
        isLoading.setValue(true);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/offers/?page=%d&size=%d", page.getValue(), PAGE_SIZE);
        System.out.println("Send req::" + url);
        JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        List<OtherTaskFull> listTask = gson.fromJson(response.getString("content"), new TypeToken<List<OtherTaskFull>>() {
                        }.getType());
                        List<OtherTaskFull> curList = otherTasks.getValue();
                        if (curList == null) {
                            otherTasks.setValue(listTask);
                        } else {
                            curList.addAll(listTask);
                        }
                        page.setValue(page.getValue() + 1);
                        isLoading.setValue(false);
                        isLastPage.setValue(response.getBoolean("last"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    isLoading.setValue(false);
                });
        mRequestQueue.add(request);
    }

}