package com.example.apple.newsingit_project.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.KeywordData;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class KeywordViewHolder extends RecyclerView.ViewHolder {
    KeywordData keywordData;
    TextView keywordView;


    public KeywordViewHolder(View itemView) {
        super(itemView);
        keywordView = (TextView) itemView.findViewById(R.id.text_keyword);
    }

    public void setKeywordData(KeywordData keywordData) {
        this.keywordData = keywordData;
        keywordView.setText(keywordData.keyword);
    }
}
