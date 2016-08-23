package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class KeywordData {
    public String keyword;
    public List<KeywordData> keywordDataList = new ArrayList<>();

    public String getKeyword() {
        return this.keyword;
    }
}
