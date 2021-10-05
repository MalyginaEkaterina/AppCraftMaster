package com.example.appcraftmaster.ui.addOffer;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcraftmaster.MyApp;
import com.example.appcraftmaster.R;
import com.example.appcraftmaster.model.Category;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.ui.WorkExp;

import java.util.List;

public class SelectedProfilesAdapter extends RecyclerView.Adapter<SelectedProfilesAdapter.SelectedProfilesViewHolder> {
    private List<Profile> selectedProfiles;
    private OnProfileContextMenuListener onProfileContextMenuListener;


    interface OnProfileContextMenuListener {
        void onContextMenuClick(int position);
    }

    public SelectedProfilesAdapter(List<Profile> profiles) {
        this.selectedProfiles = profiles;
    }

    public void setOnProfileContextMenuListener(OnProfileContextMenuListener onProfileContextMenuListener) {
        this.onProfileContextMenuListener = onProfileContextMenuListener;
    }

    @NonNull
    @Override
    public SelectedProfilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_profile_item, parent, false);
        return new SelectedProfilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedProfilesViewHolder holder, int position) {
        Profile profile = selectedProfiles.get(position);
        holder.textViewSelProfileCatValue.setText(String.format("%s", profile.getCategory().getName()));
        holder.textViewSelProfileWorkExpValue.setText(String.format("%s", WorkExp.getTextById(profile.getWorkExp())));
        holder.textViewSelProfileDescrValue.setText(String.format("%s", profile.getDescription()));
        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> menu.add("Удалить").setOnMenuItemClickListener(item -> {
            if (onProfileContextMenuListener != null) {
                onProfileContextMenuListener.onContextMenuClick(position);
            }
            return true;
        }));
    }

    @Override
    public int getItemCount() {
        return selectedProfiles.size();
    }

    class SelectedProfilesViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewSelProfileCatValue;
        private TextView textViewSelProfileWorkExpValue;
        private TextView textViewSelProfileDescrValue;

        public SelectedProfilesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSelProfileCatValue = itemView.findViewById(R.id.textViewSelProfileCatValue);
            textViewSelProfileWorkExpValue = itemView.findViewById(R.id.textViewSelProfileWorkExpValue);
            textViewSelProfileDescrValue = itemView.findViewById(R.id.textViewSelProfileDescrValue);
        }
    }
}
