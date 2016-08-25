package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserViewHolder extends RecyclerView.ViewHolder {

    public SearchUserData searchUserData;
    public TextView nameView;
    public Button btnSearchUser;
    public ImageView searchUserImgView;

    public SearchUserViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.text_search_user);
        btnSearchUser = (Button) itemView.findViewById(R.id.btn_search_user);
        searchUserImgView = (ImageView) itemView.findViewById(R.id.img_search_user);
    }

    public void setSearchUserData(SearchUserData searchUserData, Context context) {
        this.searchUserData = searchUserData;
        nameView.setText(searchUserData.getName());
    }
}