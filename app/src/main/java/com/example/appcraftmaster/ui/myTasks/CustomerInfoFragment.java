package com.example.appcraftmaster.ui.myTasks;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appcraftmaster.Common;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.UserInfo;
import com.google.gson.Gson;

public class CustomerInfoFragment extends Fragment {
    private CustomerInfoViewModel customerInfoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_full_info, container, false);
        Integer customerId = getArguments().getInt("customerId");
        customerInfoViewModel = new ViewModelProvider(getActivity()).get(CustomerInfoViewModel.class);

        TextView customerFullInfoName = view.findViewById(R.id.customerFullInfoName);
        RatingBar ratingBarCustomerInfo = view.findViewById(R.id.ratingBarCustomerInfo);
        TextView customerFullInfoNumRating = view.findViewById(R.id.customerFullInfoNumRating);
        TextView customerFullInfoPhone = view.findViewById(R.id.customerFullInfoPhone);
        TextView customerFullInfoEmail = view.findViewById(R.id.customerFullInfoEmail);

        customerInfoViewModel.getCustomerInfo(customerId).observe(getViewLifecycleOwner(), customerInfo -> {

            customerFullInfoName.setText(customerInfo.getName());
            ratingBarCustomerInfo.setRating(customerInfo.getRating());
            customerFullInfoNumRating.setText(String.format("(%d оценок)" ,customerInfo.getNumRatings()));
            String phone = Common.getCredentialWithCode(Common.PHONE_CREDENTIAL, customerInfo.getCredentials());
            String email = Common.getCredentialWithCode(Common.EMAIL_CREDENTIAL, customerInfo.getCredentials());
            if (phone != null) {
                customerFullInfoPhone.setText(phone);
            }
            if (email != null) {
                customerFullInfoEmail.setText(email);
            }
        });

        return view;
    }

}
