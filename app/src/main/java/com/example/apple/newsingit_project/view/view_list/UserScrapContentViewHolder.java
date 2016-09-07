package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentViewHolder extends RecyclerView.ViewHolder {

    public UserScrapContentData userScrapContentData;

    public TextView ncTitleView, titleView, dateView, likeView, authorView;
    public ImageButton settingButton, likeButton;
    public ImageView scrap_news_imageview;

    NetworkManager networkManager;

    public UserScrapContentViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView)itemView.findViewById(R.id.text_scrap_list_title);
        ncTitleView = (TextView) itemView.findViewById(R.id.text_scrap_list_nctitle);
        dateView = (TextView)itemView.findViewById(R.id.text_scrap_list_date);
        likeView = (TextView)itemView.findViewById(R.id.text_scrap_list_like);
        settingButton = (ImageButton) itemView.findViewById(R.id.img_btn_scrap_setting);
        likeButton = (ImageButton)itemView.findViewById(R.id.img_btn_scrap_list_like);
        authorView = (TextView) itemView.findViewById(R.id.text_scrap_list_author);
        scrap_news_imageview = (ImageView) itemView.findViewById(R.id.img_scrap_list);
    }

    public void setUserScrapContent(UserScrapContentData userScrapContentData, Context context, String who_flag) {
        this.userScrapContentData = userScrapContentData;
        titleView.setText(userScrapContentData.getTitle());
        ncTitleView.setText(userScrapContentData.getNcTitle());
        dateView.setText(userScrapContentData.getNcTime());
        likeView.setText("" + userScrapContentData.getLike());
        authorView.setText(userScrapContentData.getNcAuthor());

        String news_image_url = userScrapContentData.getNcImgUrl();

        //사용자와 다른 사용자에 따른 기능 구분.//
        if (who_flag.equals("1")) //다른 유저의 스크랩에 들어올 경우//
        {
            settingButton.setVisibility(View.GONE);
        } else if (who_flag.equals("0")) //나의 스크랩에 들어올 경우.//
        {
            settingButton.setVisibility(View.VISIBLE);
            //좋아요 기능 활성화 하면 안돼//
        }

        if (news_image_url.equals("default")) {
            Picasso.with(context)
                    .load(R.mipmap.no_image)
                    .into(scrap_news_imageview); //into로 보낼 위젯 선택.//
        } else {
            //이미지 처리 작업.//
            //이미지를 로드.//
            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(news_image_url)
                    .into(scrap_news_imageview);
        }
    }
}
