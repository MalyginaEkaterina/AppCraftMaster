package com.example.appcraftmaster.ui.myTasks;

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
import com.example.appcraftmaster.model.MyExecTask;
import com.example.appcraftmaster.model.UserInfo;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerInfoViewModel extends AndroidViewModel {
    private MutableLiveData<UserInfo> customerInfo = new MutableLiveData<>();

    public CustomerInfoViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public MutableLiveData<UserInfo> getCustomerInfo(Integer id) {
        if (customerInfo.getValue() == null) {
            updateCustomerInfo(id);
        }
        return customerInfo;
    }

    public void updateCustomerInfo(Integer id) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = String.format("http://10.0.2.2:8189/craftmaster/api/v1/users/customer_info/%d", id);
        System.out.println("Send customer info req::" + url);
        JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    customerInfo.setValue(gson.fromJson(response.toString(), UserInfo.class));
                },
                error -> {
                    error.printStackTrace();
                    customerInfo.setValue(null);
                });
        mRequestQueue.add(request);
    }
}
