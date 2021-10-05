package com.example.appcraftmaster;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.appcraftmaster.model.Credential;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common {
    public static final Integer INCORRECT_LOGIN_PASSWORD = 401;
    public static final String PHONE_CREDENTIAL = "phone";

    public static String getCredentialWithCode(String code, List<Credential> credentials) {
        for (Credential c : credentials) {
            if (c.getCode().equals(code)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static Integer getStatusCodeFromVolleyError(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null) {
            try {
                JSONObject data = new JSONObject(new String(networkResponse.data));
                return data.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class JsonArrayRequestWithToken extends JsonArrayRequest {

        SharedPreferences preferences;

        public JsonArrayRequestWithToken(SharedPreferences preferences,
                                         int method,
                                         String url,
                                         JSONArray jsonRequest,
                                         Response.Listener<JSONArray> listener,
                                         @Nullable Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.preferences = preferences;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", preferences.getString("token", ""));
            return headers;
        }
    }

    public static class JsonObjectRequestWithToken extends JsonObjectRequest {

        SharedPreferences preferences;
        public JsonObjectRequestWithToken(SharedPreferences preferences,
                                          int method,
                                          String url,
                                          @Nullable JSONObject jsonRequest,
                                          Response.Listener<JSONObject> listener,
                                          @Nullable Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.preferences = preferences;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", preferences.getString("token", ""));
            return headers;
        }
    }
}
