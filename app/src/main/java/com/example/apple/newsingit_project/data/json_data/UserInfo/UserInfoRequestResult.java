package com.example.apple.newsingit_project.data.json_data.userinfo;

public class UserInfoRequestResult {
    private int followers;
    private int followings;
    private String name;
    private int scrapings;
    private String pf_url;
    private String aboutme;

    public int getFollowers() {
        return this.followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return this.followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScrapings() {
        return this.scrapings;
    }

    public void setScrapings(int scrapings) {
        this.scrapings = scrapings;
    }

    public String getPf_url() {
        return this.pf_url;
    }

    public void setPf_url(String pf_url) {
        this.pf_url = pf_url;
    }

    public String getAboutme() {
        return this.aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }
}
