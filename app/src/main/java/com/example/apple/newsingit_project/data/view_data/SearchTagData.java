package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchTagData {
    public int id;
    public String tag;
    public String scrap_count;

    public List<SearchTagData> searchTagDataList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getScrap_count() {
        return this.scrap_count;
    }

    public void setScrap_count(String scrap_count) {
        this.scrap_count = scrap_count;
    }
}
