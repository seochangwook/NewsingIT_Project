package com.example.apple.newsingit_project.view.view_list;

import com.example.apple.newsingit_project.data.view_data.FolderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 25..
 */
public class FolderGroupItem {
    public String foldergroup_name;

    //그룹은 각 그룹에 따른 자식을 가질 수 있다.//
    public List<FolderItem> folder = new ArrayList<>();
}
