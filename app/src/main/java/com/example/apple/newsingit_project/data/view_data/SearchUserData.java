package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserData {
    public int id;
    public String name;
    public String intro;
    public String user_img_url;
    public int user_id;

    //현재 객체를 가지고 있는 것이 여러개이므로 배열로 선언//
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

    public void setName(String user_name) {
        this.name = user_name;
    }

    public String getIntro(){
        return this.intro;
    public void setName(String name) {
        this.name = name;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void set_Intro(String user_intro) {
        this.intro = user_intro;
    }

    public String get_User_imgUrl() {
        return this.user_img_url;
    }

    public void set_User_imgUrl(String user_imageUrl) {
        this.user_img_url = user_imageUrl;
    }

    public int get_user_id() {
        return this.user_id;
    }

    public void set_user_id(int user_id) {
        this.user_id = user_id;
    }
}
