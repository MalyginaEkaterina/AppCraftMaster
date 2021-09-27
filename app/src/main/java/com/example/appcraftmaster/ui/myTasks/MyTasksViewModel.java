package com.example.appcraftmaster.ui.myTasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyTasksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyTasksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Мои задания");
    }

    public LiveData<String> getText() {
        return mText;
    }
}