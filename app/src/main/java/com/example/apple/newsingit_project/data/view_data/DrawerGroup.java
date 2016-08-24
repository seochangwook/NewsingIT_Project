package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class DrawerGroup {
    public String name;
    public List<DrawerChild> childViewList = new ArrayList<>();

    public DrawerGroup(String groupname, DrawerChild... childViewList ){
        this.name = groupname;
        this.childViewList.addAll(Arrays.asList(childViewList));
    }
}
