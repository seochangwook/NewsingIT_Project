package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserData {
    public int id;
    public String name;
    public String aboutMe;
    public String profileUrl;
    public List<SearchUserData> searchUserDataArrayList = new ArrayList<>();

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SearchUserData> getSearchUserDataArrayList() {
        return searchUserDataArrayList;
    }

    public void setSearchUserDataArrayList(List<SearchUserData> searchUserDataArrayList) {
        this.searchUserDataArrayList = searchUserDataArrayList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

}
