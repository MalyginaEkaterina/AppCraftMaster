package com.example.appcraftmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.model.LoginRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public void loginUser(View view) {
        if (editTextLogin.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.empty_login_pass, Toast.LENGTH_LONG).show();
        } else {
            try {
                String url = "http://10.0.2.2:8189/craftmaster/auth/user_login/";
                LoginRequest req = new LoginRequest(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                Gson gson = new Gson();
                JSONObject reqJson = new JSONObject(gson.toJson(req));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            preferences.edit().putString("token", token).apply();
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Integer status = Common.getStatusCodeFromVolleyError(error);
                        if (status.equals(Common.INCORRECT_LOGIN_PASSWORD)) {
                            Toast.makeText(AuthActivity.this, R.string.toast_incorrect_login_password, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mRequestQueue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void registrUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}