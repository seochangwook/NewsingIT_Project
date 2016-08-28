package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.Keyword3NewsContentData;

/**
 * Created by apple on 2016. 8. 26..
 */
public class Keyword3NewsContentViewHolder extends RecyclerView.ViewHolder {
    public ImageView news_thumbnailUrl_imageview;
    public TextView news_title_textview;
    public TextView news_write_date_textview;
    public TextView news_author_textview;
    public TextView news_content_textview;
    public String thumbnailUrl_str;
    Keyword3NewsContentData newsContentData;

    public Keyword3NewsContentViewHolder(View itemView) {
        super(itemView);

        news_title_textview = (TextView) itemView.findViewById(R.id.news_3_title_textview);
        news_thumbnailUrl_imageview = (ImageView) itemView.findViewById(R.id.news_3_thumbnail_imageview);
        news_write_date_textview = (TextView) itemView.findViewById(R.id.news_3_write_time_textview);
        news_author_textview = (TextView) itemView.findViewById(R.id.news_3_author_textview);
        news_content_textview = (TextView) itemView.findViewById(R.id.news_3_content_textview);
    }

    public void set_NewsContent(Keyword3NewsContentData newsContentData, Context context) {
        this.newsContentData = newsContentData;

        //데이터값을 받아서 UI적인 처리//
        news_title_textview.setText(newsContentData.get_news_title());
        news_write_date_textview.setText(newsContentData.get_news_write_date());
        news_author_textview.setText(newsContentData.get_news_author());
        news_content_textview.setText(newsContentData.get_news_content());

        thumbnailUrl_str = newsContentData.get_news_thumbnailUrl();

        //이미지 설정.//
        /** 사용자 프로필 이미지 설정 **/
        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
        //Glide 라이브러리를 이용하여 이미지 로딩//
        //Glide 라이브러리를 이용하여 이미지 로딩//
        Glide.with(context)
                .load(thumbnailUrl_str)
                .into(news_thumbnailUrl_imageview);
    }
}
