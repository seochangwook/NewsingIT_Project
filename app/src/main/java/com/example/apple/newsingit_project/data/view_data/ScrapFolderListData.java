package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 26..
 */
public class ScrapFolderListData {
    public String scrap_folder_list_data;

    public List<ScrapFolderListData> scrapfolderlist = new ArrayList<>();

    public String get_scrap_folder_list_data() {
        return this.scrap_folder_list_data;
    }

    public void set_scrap_folder_list_data(String scrap_folder_list_data) {
        this.scrap_folder_list_data = scrap_folder_list_data;
    }
}
