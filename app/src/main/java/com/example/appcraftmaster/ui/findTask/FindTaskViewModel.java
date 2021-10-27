package com.example.appcraftmaster.ui.findTask;

import android.app.Application;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.model.OtherTaskFull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FindTaskViewModel extends AndroidViewModel {

    private List<OtherTaskFull> otherTasks = new ArrayList<>();
    private MutableLiveData<Integer> page = new MutableLiveData<>();
    private static final Integer PAGE_SIZE = 5;
    private MutableLiveData<Boolean> wasLastPage = new MutableLiveData<>();

    public FindTaskViewModel(@NonNull @NotNull Application application) {
        super(application);
        refreshAll();
    }

    public List<OtherTaskFull> getOtherTasks() {
        return otherTasks;
    }

    public Integer getPage() {
        return page.getValue();
    }

    public void addOtherTasks(List<OtherTaskFull> list) {
        otherTasks.addAll(list);
    }

    public void refreshAll() {
        otherTasks.clear();
        page.setValue(1);
        wasLastPage.setValue(false);
    }

    public Boolean getWasLastPage() {
        return wasLastPage.getValue();
    }

    public MutableLiveData<List<OtherTaskFull>> getNewPage() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        MutableLiveData<List<OtherTaskFull>> tasksPage = new MutableLiveData<>();
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/offers/suitable/?page=%d&size=%d", page.getValue(), PAGE_SIZE);
        System.out.println("Send req::" + url);
        JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        tasksPage.setValue(gson.fromJson(response.getString("content"), new TypeToken<List<OtherTaskFull>>() {
                        }.getType()));
                        page.setValue(page.getValue() + 1);
                        wasLastPage.setValue(response.getBoolean("last"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    tasksPage.setValue(null);
                });
        mRequestQueue.add(request);
        return tasksPage;
    }

}