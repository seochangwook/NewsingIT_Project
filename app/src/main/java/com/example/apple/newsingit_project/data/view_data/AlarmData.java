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
    public int data_pk;
    public String acase;
    //1 - 스크랩 좋아요
    //2 - 나를 팔로우
    //3 - 새 스크랩


    public List<AlarmData> alarmDataList = new ArrayList<>();

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCase() {
        return this.acase;
    }

    public void setCase(String acase) {
        this.acase = acase;
    }

    public int getData_pk() {
        return this.data_pk;
    }

    public void setData_pk(int data_pk) {
        this.data_pk = data_pk;
    }
}
