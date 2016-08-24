package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowingData {
    public String name;

    public List<FollowingData> followingDataList = new ArrayList<>();

    public String getName() {
        return this.name;
    }
}
