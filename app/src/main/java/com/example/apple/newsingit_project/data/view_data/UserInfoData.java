package com.example.apple.newsingit_project.data.view_data;

/**
 * Created by Tacademy on 2016-08-31.
 */
public class UserInfoData {
    String name;
    String pf_url;
    String aboutMe;
    int scrapings;
    int followerCount;
    int follwingCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return pf_url;
    }

    public void setProfileUrl(String profileUrl) {
        this.pf_url = profileUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public int getScrapCount() {
        return scrapings;
    }

    public void setScrapCount(int scrapCount) {
        this.scrapings = scrapCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollwingCount() {
        return follwingCount;
    }

    public void setFollwingCount(int follwingCount) {
        this.follwingCount = follwingCount;
    }

}
