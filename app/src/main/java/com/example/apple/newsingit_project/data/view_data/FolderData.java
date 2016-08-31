package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 24..
 */
public class FolderData {
    //폴더 관련 데이터.//
    public int folder_id;
    public String folder_name; //폴더 이름//
    public boolean folder_private; //true이면 잠금, false이면 잠금해제.//
    public List<FolderData> folder_list = new ArrayList<>(); //폴더목록을 저장할 배열 선언.//
    String folder_imageUrl; //폴더 이미지.(추후 네트워크 적용 시 변경)//

    public String get_folder_name() {
        return this.folder_name;
    }

    public String get_folder_imageUrl()
    {
        return this.folder_imageUrl;
    }

    public void set_folder_imageUrl(String folder_imageUrl)
    {
        this.folder_imageUrl = folder_imageUrl;
    }

    public void set_get_folder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public void setFolder_private(boolean folder_private) {
        this.folder_private = folder_private;
    }

    public boolean get_folder_private() {
        return this.folder_private;
    }

    public int get_folderid() {
        return this.folder_id;
    }

    public void set_folderid(int folder_id) {
        this.folder_id = folder_id;
    }
}
