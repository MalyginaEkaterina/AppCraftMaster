package com.example.appcraftmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.model.CredentialDto;
import com.example.appcraftmaster.model.SignUpRequest;
import com.example.appcraftmaster.model.Status;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextRegLogin;
    private EditText editTextRegPassword;
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextEmail;
    RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextRegLogin = findViewById(R.id.editTextRegLogin);
        editTextRegPassword = findViewById(R.id.editTextRegPassword);
        editTextName = findViewById(R.id.editTextFio);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public void sendRegRequest(View view) {
        if (editTextRegLogin.getText().toString().isEmpty() || editTextRegPassword.getText().toString().isEmpty()) {
            Toast toast = new Toast(this);
            toast.setText(getResources().getString(R.string.empty_login_pass));
            toast.show();
        } else {
            try {
                String url = "http://10.0.2.2:8189/craftmaster/api/v1/users/registr";
                List<CredentialDto> credentials = new ArrayList<>();
                if (!editTextPhone.getText().toString().isEmpty()) {
                    credentials.add(new CredentialDto(Common.PHONE_CREDENTIAL, editTextPhone.getText().toString(), null));
                }
                if (!editTextEmail.getText().toString().isEmpty()) {
                    credentials.add(new CredentialDto(Common.EMAIL_CREDENTIAL, editTextEmail.getText().toString(), null));
                }
                SignUpRequest req = new SignUpRequest(editTextRegLogin.getText().toString(), editTextName.getText().toString(),
                        editTextRegPassword.getText().toString(), credentials);
                Gson gson = new Gson();
                JSONObject reqJson = new JSONObject(gson.toJson(req));
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, reqJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Integer status = gson.fromJson(response.toString(), Status.class).getStatus();
                        if (status.equals(StatusCode.STATUS_OK)) {
                            Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.reg_success), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistrationActivity.this, AuthActivity.class);
                            startActivity(intent);
                        } else if (status.equals(StatusCode.LOGIN_EXISTS)) {
                            Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.reg_login_exists), Toast.LENGTH_LONG).show();
                            editTextRegLogin.setText("");
                        } else {
                            Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.reg_not_success), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.reg_not_success), Toast.LENGTH_LONG).show();
                    }
                });
                mRequestQueue.add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}