package com.example.appcraftmaster;

import android.app.Application;

import com.example.appcraftmaster.model.CategoryList;
import com.example.appcraftmaster.model.Profile;
import com.example.appcraftmaster.model.ProfileList;
import com.example.appcraftmaster.model.Response;
import com.example.appcraftmaster.model.TaskFull;
import com.example.appcraftmaster.model.UserInfo;
import com.example.appcraftmaster.ui.myTasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MyApp extends Application {
    private UserInfo userInfo;
    private CategoryList categoryList;
    private ProfileList profileListFromServer;
    private List<Profile> profileList = new ArrayList<>();
    private List<Long> deletedProfilesId = new ArrayList<>();
    private List<TaskFull> myTasks = new ArrayList<>();
    private Boolean needUpdateMyTasks = true;

    public List<TaskFull> getMyTasks() {
        return myTasks;
    }

    public void setMyTasks(List<TaskFull> tasks) {
        myTasks.clear();
        myTasks.addAll(tasks);
    }

    public Boolean getNeedUpdateMyTasks() {
        return needUpdateMyTasks;
    }

    public void setNeedUpdateMyTasks(Boolean needUpdateMyTasks) {
        this.needUpdateMyTasks = needUpdateMyTasks;
    }

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

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void addInProfileList(Profile p) {
        profileList.add(p);
    }

    public ProfileList getProfileListFromServer() {
        return profileListFromServer;
    }

    public void setProfileListFromServer(ProfileList profileListFromServer) {
        this.profileListFromServer = profileListFromServer;
    }

    public void flushProfiles() {
        profileList.clear();
        profileList.addAll(profileListFromServer.getProfiles());
        deletedProfilesId.clear();
    }

    public void deleteFromProfileList(Profile p) {
        profileList.remove(p);
    }

    public List<Long> getDeletedProfilesId() {
        return deletedProfilesId;
    }

    public void addToDeletedProfilesId(Long id) {
        deletedProfilesId.add(id);
    }

    public void logOut() {
        setUserInfo(null);
        setCategoryList(null);
        setProfileListFromServer(null);
        profileList.clear();
        deletedProfilesId.clear();
        myTasks.clear();
        setNeedUpdateMyTasks(true);
    }

    public List<Profile> getProfilesChanges() {
        return profileList.stream().filter(profile -> profile.getId() == null).collect(Collectors.toList());
    }

    public void setTaskStatusClose(int pos) {
        myTasks.get(pos).setStatus(TaskStatus.CLOSED.getId());
    }

    public void setTaskStatusAssigned(int pos, Response acceptedResp) {
        TaskFull task = myTasks.get(pos);
        task.setStatus(TaskStatus.ASSIGNED.getId());
        task.setAcceptedBid(acceptedResp);

    }

    public void setTaskRating(int position, float rating) {
        myTasks.get(position).getAcceptedBid().setRating(rating);
    }
}
