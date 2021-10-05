package com.example.appcraftmaster.ui.addOffer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.StatusCode;
import com.example.appcraftmaster.databinding.FragmentAddOfferBinding;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.model.ProfileList;

import java.util.List;

public class AddOfferFragment extends Fragment {

    private AddOfferViewModel addOfferViewModel;
    private FragmentAddOfferBinding binding;
    private RecyclerView recyclerViewSelectedProfiles;
    private SelectedProfilesAdapter adapter;
    private NavController navController;
    private ProgressBar progressBarAddOffer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addOfferViewModel = new ViewModelProvider(this).get(AddOfferViewModel.class);
        binding = FragmentAddOfferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerViewSelectedProfiles = root.findViewById(R.id.recyclerViewSelectedProfiles);
        recyclerViewSelectedProfiles.setLayoutManager(new LinearLayoutManager(getContext()));
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        List<Profile> profiles = ((MyApp) getActivity().getApplicationContext()).getProfileList();

        adapter = new SelectedProfilesAdapter(profiles);
        adapter.setOnProfileContextMenuListener(position -> {
            Profile p = profiles.get(position);
            ((MyApp) getActivity().getApplicationContext()).deleteFromProfileList(p);
            if (p.getId() != null) {
                ((MyApp) getActivity().getApplicationContext()).addToDeletedProfilesId(p.getId());
            }
            adapter.notifyDataSetChanged();
        });
        recyclerViewSelectedProfiles.setAdapter(adapter);

        progressBarAddOffer = root.findViewById(R.id.progressBarAddOffer);

        ProfileList serverProfiles = ((MyApp) getActivity().getApplicationContext()).getProfileListFromServer();
        if (serverProfiles == null) {
            progressBarAddOffer.setVisibility(ProgressBar.VISIBLE);
            updateProfiles();
        }

        Button buttonAddOffer = (Button) root.findViewById(R.id.buttonAddOffer);
        buttonAddOffer.setOnClickListener(v -> addOffer());

        Button buttonAddProfile = (Button) root.findViewById(R.id.buttonAddProfile);
        buttonAddProfile.setOnClickListener(v -> addProfile());

        Button buttonAddOfferCancel = (Button) root.findViewById(R.id.buttonAddOfferCancel);
        buttonAddOfferCancel.setOnClickListener(v -> cancelChanges());
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    private void cancelChanges() {
        ((MyApp) getActivity().getApplicationContext()).flushProfiles();
        adapter.notifyDataSetChanged();
    }

    private void addProfile() {
        navController.navigate(R.id.nav_add_profile);
    }

    private void addOffer() {
        List<Long> deletedIds = ((MyApp) getActivity().getApplicationContext()).getDeletedProfilesId();
        List<Profile> profilesChanges = ((MyApp) getActivity().getApplicationContext()).getProfilesChanges();
        System.out.println("deletedIds::" + deletedIds);
        System.out.println("profilesChanges::" + profilesChanges);
        if (deletedIds.isEmpty() && profilesChanges.isEmpty()) {
            return;
        }
        if (!deletedIds.isEmpty() && !profilesChanges.isEmpty()) {
            addOfferViewModel.deleteProfilesByIds(deletedIds).observe(getViewLifecycleOwner(), deleteStatus -> {
                if (deleteStatus.equals(StatusCode.STATUS_ERROR)) {
                    showDialog(R.string.dialog_profiles_changes_error);
                } else if (deleteStatus.equals(StatusCode.STATUS_OK)) {
                    addOfferViewModel.addProfilesReq(profilesChanges).observe(getViewLifecycleOwner(), addStatus -> {
                        if (addStatus.equals(StatusCode.STATUS_ERROR)) {
                            updateProfiles();
                            showDialog(R.string.dialog_profiles_changes_error);
                        } else if (addStatus.equals(StatusCode.STATUS_OK)) {
                            updateProfiles();
                            showDialog(R.string.dialog_profiles_changes);
                        }
                    });
                }
            });
        } else if (!deletedIds.isEmpty()) {
            addOfferViewModel.deleteProfilesByIds(deletedIds).observe(getViewLifecycleOwner(), deleteStatus -> {
                if (deleteStatus.equals(StatusCode.STATUS_ERROR)) {
                    showDialog(R.string.dialog_profiles_changes_error);
                } else if (deleteStatus.equals(StatusCode.STATUS_OK)) {
                    updateProfiles();
                    showDialog(R.string.dialog_profiles_changes);
                }
            });
        } else {
            addOfferViewModel.addProfilesReq(profilesChanges).observe(getViewLifecycleOwner(), addStatus -> {
                if (addStatus.equals(StatusCode.STATUS_ERROR)) {
                    showDialog(R.string.dialog_profiles_changes_error);
                } else if (addStatus.equals(StatusCode.STATUS_OK)) {
                    updateProfiles();
                    showDialog(R.string.dialog_profiles_changes);
                }
            });
        }
    }

    private void updateProfiles() {
        addOfferViewModel.updateServerProfiles().observe(getViewLifecycleOwner(), serverProfiles -> {
            progressBarAddOffer.setVisibility(ProgressBar.INVISIBLE);
            ((MyApp) getActivity().getApplicationContext()).setProfileListFromServer(new ProfileList(serverProfiles));
            if (!serverProfiles.isEmpty()) {
                System.out.println("flush profiles");
                ((MyApp) getActivity().getApplicationContext()).flushProfiles();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showDialog(int msgId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msgId);
        builder.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
            dialog.dismiss(); // Отпускает диалоговое окно
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}