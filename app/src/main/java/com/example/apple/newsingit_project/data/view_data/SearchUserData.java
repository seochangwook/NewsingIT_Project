package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserData {
    public String name;
    public String intro;

    public List<SearchUserData> searchUserDataArrayList = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public String getIntro(){
        return this.intro;
    }

}
