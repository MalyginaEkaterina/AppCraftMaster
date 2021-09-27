package com.example.appcraftmaster.ui.addOffer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddOfferViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddOfferViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Стать исполнителем");
    }

    public LiveData<String> getText() {
        return mText;
    }
}