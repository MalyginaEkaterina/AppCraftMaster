package com.example.appcraftmaster.ui.addOffer;

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
import com.example.appcraftmaster.model.AddProfile;
import com.example.appcraftmaster.model.AddProfilesDto;
import com.example.appcraftmaster.model.DeleteProfilesDto;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.model.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddOfferViewModel extends AndroidViewModel {
    public AddOfferViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Profile>> updateServerProfiles() {
        MutableLiveData<ArrayList<Profile>> serverProfiles = new MutableLiveData<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
        String url = "http://10.0.2.2:8189/craftmaster/api/v1/userprofiles/my_profiles";
        JsonArrayRequest request = new Common.JsonArrayRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    serverProfiles.setValue(gson.fromJson(response.toString(), new TypeToken<List<Profile>>() {
                    }.getType()));
                }, Throwable::printStackTrace);
        mRequestQueue.add(request);
        return serverProfiles;
    }

    public LiveData<Integer> addProfilesReq(List<Profile> addedProfiles) {
        MutableLiveData<Integer> addStatus = new MutableLiveData<>();
        try {
            AddProfilesDto addProfilesDto = new AddProfilesDto(addedProfiles.stream().map(AddProfile::new).collect(Collectors.toList()));
            System.out.println("addProfilesDto::" + addProfilesDto);
            Gson gson = new Gson();
            JSONObject addProfilesJson = new JSONObject(gson.toJson(addProfilesDto));
            System.out.println(addProfilesJson);
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/userprofiles/add_profiles";
            JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                    Request.Method.POST,
                    url,
                    addProfilesJson,
                    response -> {
                        System.out.println(response);
                        addStatus.setValue(gson.fromJson(response.toString(), Status.class).getStatus());
                    },
                    error -> {
                        error.printStackTrace();
                        addStatus.setValue(StatusCode.STATUS_ERROR);
                    });
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addStatus;
    }

    public LiveData<Integer> deleteProfilesByIds(List<Long> deletedIds) {
        MutableLiveData<Integer> deleteStatus = new MutableLiveData<>();
        try {
            DeleteProfilesDto deleteProfilesDto = new DeleteProfilesDto(deletedIds);
            Gson gson = new Gson();
            JSONObject deleteJson = new JSONObject(gson.toJson(deleteProfilesDto));
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplication());
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/userprofiles/delete_profiles";
            System.out.println("deleteJson::" + deleteJson);
            JsonObjectRequest request = new Common.JsonObjectRequestWithToken(PreferenceManager.getDefaultSharedPreferences(getApplication()),
                    Request.Method.POST,
                    url,
                    deleteJson,
                    response -> {
                        System.out.println(response);
                        deleteStatus.setValue(gson.fromJson(response.toString(), Status.class).getStatus());
                    },
                    error -> {
                        error.printStackTrace();
                        deleteStatus.setValue(StatusCode.STATUS_ERROR);
                    });
            mRequestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deleteStatus;
    }
}
