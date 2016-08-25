package com.example.apple.newsingit_project.data.view_data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 25..
 */
public class UserFolderData {
    public String folder_name; //폴더 이름//
    //String folder_imageUrl; //폴더 이미지.(추후 네트워크 적용 시 변경)//
    public int dummy_folder_image;
    public boolean folder_private; //true이면 잠금, false이면 잠금해제.//

    public List<UserFolderData> user_folder_list = new ArrayList<>(); //폴더목록을 저장할 배열 선언.//

    public String get_folder_name() {
        return this.folder_name;
    }

    /*public String get_folder_imageUrl()
    {
        return this.folder_imageUrl;
    }*/

    public int get_dummy_folder_image() {
        return this.dummy_folder_image;
    }

    public void set_dummy_folder_image(int dummy_folder_image) {
        this.dummy_folder_image = dummy_folder_image;
    }

    /*public void set_get_folder_imageUrl(String folder_imageUrl)
    {
        this.folder_imageUrl = folder_imageUrl;
    }*/

    public void set_get_folder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public void setFolder_private(boolean folder_private) {
        this.folder_private = folder_private;
    }

    public boolean get_folder_private() {
        return this.folder_private;
    }
}
