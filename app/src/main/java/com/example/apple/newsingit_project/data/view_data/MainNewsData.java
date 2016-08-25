package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class MainNewsData {
    public String news;

    public List<MainNewsData> mainNewsDataList = new ArrayList<>();

    public String getNews() {
        return this.news;
    }
}
