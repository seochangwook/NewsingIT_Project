package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentData {
    public String content;
    public String title;
    public String date;
    public String like;

    public List<UserScrapContentData> userScrapContentDataList = new ArrayList<>();
    private boolean scrap; //스크랩 설정 버튼 flag//

    public String getContent() {
        return this.content;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDate() {
        return this.date;
    }

    public String getLike(){
        return this.like;
    }

    public boolean getScrap() {
        return this.scrap;
    }
}
