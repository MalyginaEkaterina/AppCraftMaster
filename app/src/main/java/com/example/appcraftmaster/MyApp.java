package com.example.appcraftmaster;

import android.app.Application;

import com.example.appcraftmaster.model.CategoryList;
import com.example.appcraftmaster.model.UserInfo;


public class MyApp extends Application {
    private UserInfo userInfo;
    private CategoryList categoryList;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CategoryList getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(CategoryList categoryList) {
        this.categoryList = categoryList;
    }
}
