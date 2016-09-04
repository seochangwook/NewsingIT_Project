package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchTagData {
    public int id;
    public String tag;
    // public String count;

    public List<SearchTagData> searchTagDataList = new ArrayList<>();

    //   public void setCount(String count) {
    //      this.count = count;
    //  }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SearchTagData> getSearchTagDataList() {
        return searchTagDataList;
    }

    public void setSearchTagDataList(List<SearchTagData> searchTagDataList) {
        this.searchTagDataList = searchTagDataList;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    //  public String getCount(){
    //      return this.count;
    // }
}
