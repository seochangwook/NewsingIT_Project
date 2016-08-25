package com.example.apple.newsingit_project.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.MainNewsData;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class MainNewsViewHolder extends RecyclerView.ViewHolder {

    MainNewsData mainNewsData;

    TextView newsView;

    public MainNewsViewHolder(View itemView) {
        super(itemView);
        newsView = (TextView) itemView.findViewById(R.id.text_main_news);
    }

    public void setMainNewsData(MainNewsData mainNewsData) {
        this.mainNewsData = mainNewsData;
        newsView.setText(mainNewsData.getNews());
    }
}
