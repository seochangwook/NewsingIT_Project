package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class NoticeGroup {
    public String title;
    public String date;
    public List<NoticeChild> childList = new ArrayList<>();

    public NoticeGroup(String title, String date, NoticeChild... childList ){
        this.title = title;
        this.date = date;
        this.childList.addAll(Arrays.asList(childList));
    }
}
