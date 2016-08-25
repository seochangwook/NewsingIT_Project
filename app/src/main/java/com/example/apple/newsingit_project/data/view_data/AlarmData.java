package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class AlarmData {
    public String name;
    public String content;
    public String date;

    public String acase;
    //1 - 스크랩 좋아요
    //2 - 나를 팔로우
    //3 - 새 스크랩


    public List<AlarmData> alarmDataList = new ArrayList<>();

    public String getContent() {
        return this.content;
    }

    public String getName(){
        return this.name;
    }

    public String getDate(){
        return this.date;
    }

    public String getCase() {
        return this.acase;
    }
}
