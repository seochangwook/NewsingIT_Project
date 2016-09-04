package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentData {
    public List<UserScrapContentData> userScrapContentDataList = new ArrayList<>();
    private int id;
    private String title;
    private String ncTitle;
    private String ncAuthor;
    private String ncTime;
    private int like;
    private boolean likeFlag;
    private boolean locked;
    private String ncImgUrl;

    public String getNcImgUrl() {
        return ncImgUrl;
    }

    public void setNcImgUrl(String ncImgUrl) {
        this.ncImgUrl = ncImgUrl;
    }


    public List<UserScrapContentData> getUserScrapContentDataList() {
        return userScrapContentDataList;
    }

    public void setUserScrapContentDataList(List<UserScrapContentData> userScrapContentDataList) {
        this.userScrapContentDataList = userScrapContentDataList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNcAuthor() {
        return ncAuthor;
    }

    public void setNcAuthor(String ncAuthor) {
        this.ncAuthor = ncAuthor;
    }

    public boolean isLikeFlag() {
        return likeFlag;
    }

    public boolean isLock() {
        return locked;
    }

    public String getNcTitle() {
        return this.ncTitle;
    }

    public void setNcTitle(String ncTitle) {
        this.ncTitle = ncTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNcTime() {
        return this.ncTime;
    }

    public void setNcTime(String ncTime) {
        this.ncTime = ncTime;
    }

    public int getLike() {
        return this.like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public boolean getLikeFlag() {
        return this.likeFlag;
    }

    public void setLikeFlag(boolean likeFlag) {
        this.likeFlag = likeFlag;
    }

    public boolean getLock() {
        return this.locked;
    }

    public void setLock(boolean lock) {
        this.locked = lock;
    }
}
