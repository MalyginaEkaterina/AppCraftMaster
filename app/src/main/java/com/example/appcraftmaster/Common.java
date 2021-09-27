package com.example.appcraftmaster;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.appcraftmaster.model.Credential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
}
