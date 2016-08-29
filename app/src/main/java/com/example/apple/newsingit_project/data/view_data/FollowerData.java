package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowerData {
    public String name;
    public String intro;

    public List<FollowerData> followerDataList = new ArrayList<>();

    public String getName() {
        return this.name;
    }
    public String getIntro(){
        return this.intro;
    }


}
