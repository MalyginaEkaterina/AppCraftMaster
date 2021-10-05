package com.example.appcraftmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.databinding.ActivityMainBinding;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.CategoryList;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.model.ProfileList;
import com.example.appcraftmaster.model.UserInfo;
import com.example.appcraftmaster.ui.addTask.TaskInfoFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    RequestQueue mRequestQueue;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView navUsername;
    private TextView navUserPhone;
    private MutableLiveData<UserInfo> liveUserInfo;
    private MutableLiveData<List<Category>> categories;
    //private MutableLiveData<List<Profile>> profiles;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("token", "");
        if (token.isEmpty()) {
            Intent intentAuth = new Intent(this, AuthActivity.class);
            startActivity(intentAuth);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_task, R.id.nav_find_task, R.id.nav_my_tasks, R.id.nav_add_offer, R.id.nav_find_offer, R.id.nav_notification)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.textViewNavUsername);
        navUserPhone = (TextView) headerView.findViewById(R.id.textViewNavUserPhone);
        setNavNameAndPhone();
    }

    private void setNavNameAndPhone() {
        UserInfo globalUserInfo = ((MyApp) getApplicationContext()).getUserInfo();
        if (globalUserInfo != null) {
            navUsername.setText(globalUserInfo.getName());
            navUserPhone.setText(Common.getCredentialWithCode(Common.PHONE_CREDENTIAL, globalUserInfo.getCredentials()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String token = preferences.getString("token", "");
        UserInfo globalUserInfo = ((MyApp) getApplicationContext()).getUserInfo();

        if (globalUserInfo == null && !token.isEmpty()) {
            getUserInfo().observe(this, userInfo -> {
                if (userInfo != null) {
                    ((MyApp) getApplicationContext()).setUserInfo(userInfo);
                    setNavNameAndPhone();
                }
            });
        }
        CategoryList categoryList = ((MyApp) getApplicationContext()).getCategoryList();
        if (categoryList == null && !token.isEmpty()) {
            getCategories().observe(this, categories -> {
                if (!categories.isEmpty()) {
                    ((MyApp) getApplicationContext()).setCategoryList(new CategoryList(categories));
                }
            });
        }
//        ProfileList profileList = ((MyApp) getApplicationContext()).getProfileListFromServer();
//        if (profileList == null && !token.isEmpty()) {
//            getProfiles().observe(this, profiles -> {
//                ((MyApp) getApplicationContext()).setProfileListFromServer(new ProfileList(profiles));
//                if (!profiles.isEmpty()) {
//                    ((MyApp) getApplicationContext()).flushProfiles();
//                }
//            });
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void menuAction(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            logOut();
        }
    }

    public void logOut() {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8189/craftmaster/auth/user_logout/";
        StringRequest request = new StringRequestWithToken(Request.Method.POST,
                url,
                response -> {
                    Toast.makeText(MainActivity.this, R.string.toast_log_out, Toast.LENGTH_LONG).show();
                    doLogOut();
                },
                error -> {
                    error.printStackTrace();
                    doLogOut();
                });
        mRequestQueue.add(request);
    }

    private void doLogOut() {
        preferences.edit().putString("token", "").apply();
        ((MyApp) getApplicationContext()).logOut();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    public LiveData<UserInfo> getUserInfo() {
        if (liveUserInfo == null) {
            mRequestQueue = Volley.newRequestQueue(getApplication());
            liveUserInfo = new MutableLiveData<>();
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/users/user_info";
            JsonObjectRequest request = new JsonObjectRequestWithToken(Request.Method.GET,
                    url,
                    null,
                    response -> {
                        Gson gson = new Gson();
                        System.out.println(response);
                        liveUserInfo.setValue(gson.fromJson(response.toString(), UserInfo.class));
                        System.out.println("userInfo::" + liveUserInfo);
                    },
                    error -> error.printStackTrace());
            mRequestQueue.add(request);
        }
        return liveUserInfo;
    }

    public void selectCategory(View view) {
        NavOptions.Builder builder = new NavOptions.Builder();
        NavOptions navOptions = builder.setEnterAnim(R.anim.slide_in_right).build();
        navController.navigate(R.id.nav_select_category, null, navOptions);
    }

    public LiveData<List<Category>> getCategories() {
        if (categories == null) {
            mRequestQueue = Volley.newRequestQueue(getApplication());
            categories = new MutableLiveData<>();
            String url = "http://10.0.2.2:8189/craftmaster/api/v1/services/";
            JsonArrayRequest request = new Common.JsonArrayRequestWithToken(preferences, Request.Method.GET,
                    url,
                    null,
                    response -> {
                        Gson gson = new Gson();
                        categories.setValue(gson.fromJson(response.toString(), new TypeToken<List<Category>>() {
                        }.getType()));
                    }, error -> error.printStackTrace());
            mRequestQueue.add(request);
        }
        return categories;
    }

//    public LiveData<List<Profile>> getProfiles() {
//        if (profiles == null) {
//            mRequestQueue = Volley.newRequestQueue(getApplication());
//            profiles = new MutableLiveData<>();
//            String url = "http://10.0.2.2:8189/craftmaster/api/v1/profiles/";
//            JsonArrayRequest request = new Common.JsonArrayRequestWithToken(preferences, Request.Method.GET,
//                    url,
//                    null,
//                    response -> {
//                        Gson gson = new Gson();
//                        profiles.setValue(gson.fromJson(response.toString(), new TypeToken<List<Profile>>() {
//                        }.getType()));
//                    }, error -> error.printStackTrace());
//            mRequestQueue.add(request);
//        }
//        return profiles;
//    }

    private class StringRequestWithToken extends StringRequest {

        public StringRequestWithToken(int method,
                                      String url,
                                      Response.Listener<String> listener,
                                      @Nullable Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", preferences.getString("token", ""));
            return headers;
        }
    }

    private class JsonObjectRequestWithToken extends JsonObjectRequest {

        public JsonObjectRequestWithToken(int method,
                                          String url,
                                          @Nullable JSONObject jsonRequest,
                                          Response.Listener<JSONObject> listener,
                                          @Nullable Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", preferences.getString("token", ""));
            return headers;
        }
    }

//    private class JsonArrayRequestWithToken extends JsonArrayRequest {
//
//        public JsonArrayRequestWithToken(int method,
//                                         String url,
//                                         JSONArray jsonRequest,
//                                         Response.Listener<JSONArray> listener,
//                                         @Nullable Response.ErrorListener errorListener) {
//            super(method, url, jsonRequest, listener, errorListener);
//        }
//
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            HashMap<String, String> headers = new HashMap<>();
//            headers.put("Authorization", preferences.getString("token", ""));
//            return headers;
//        }
//    }
}