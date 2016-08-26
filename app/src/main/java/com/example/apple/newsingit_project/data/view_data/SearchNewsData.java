package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchNewsData {
    public String title;
    public String content;
    public String date;

    public List<SearchNewsData> searchNewsDataArrayList = new ArrayList<>();

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getDate(){
        return this.date;
    }


}
