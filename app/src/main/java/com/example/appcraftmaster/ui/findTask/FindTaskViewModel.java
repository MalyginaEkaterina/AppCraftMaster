package com.example.appcraftmaster.ui.findTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindTaskViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FindTaskViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Поиск заданий");
    }

    public LiveData<String> getText() {
        return mText;
    }
}