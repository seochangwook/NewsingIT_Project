package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentData {
    public List<UserScrapContentData> userScrapContentDataList = new ArrayList<>();
    private String content;
    private String title;
    private String date;
    private int like;
    private boolean likeFlag;
    private boolean scrap; //스크랩 설정 버튼 flag//

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
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


    public boolean getScrap() {
        return this.scrap;
    }
}
