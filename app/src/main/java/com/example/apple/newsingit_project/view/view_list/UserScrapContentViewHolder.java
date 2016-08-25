package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentViewHolder extends RecyclerView.ViewHolder {

    public UserScrapContentData userScrapContentData;

    public TextView contentView;
    public ImageButton settingButton;

    public UserScrapContentViewHolder(View itemView) {
        super(itemView);
        contentView = (TextView) itemView.findViewById(R.id.text_scrap_list);
        settingButton = (ImageButton) itemView.findViewById(R.id.img_btn_scrap_setting);
    }

    public void setUserScrapContent(UserScrapContentData userScrapContentData, Context context, String who_flag) {
        this.userScrapContentData = userScrapContentData;
        contentView.setText(userScrapContentData.getContent());

        //사용자와 다른 사용자에 따른 기능 구분.//
        if (who_flag.equals("1")) //다른 유저의 스크랩에 들어올 경우//
        {
            settingButton.setVisibility(View.GONE);
        } else if (who_flag.equals("0")) //나의 스크랩에 들어올 경우.//
        {
            settingButton.setVisibility(View.VISIBLE);
        }

    }
}
