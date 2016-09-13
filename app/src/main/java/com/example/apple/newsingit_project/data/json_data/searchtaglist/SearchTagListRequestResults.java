package com.example.apple.newsingit_project.data.json_data.searchtaglist;

public class SearchTagListRequestResults {
    private int id;
    private String tag;
    private int scrap_count;

    public int getId() {
        return this.id;
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

    public int getScrap_count() {
        return this.scrap_count;
    }

    public void setScrap_count(int scrap_count) {
        this.scrap_count = scrap_count;
    }
}
