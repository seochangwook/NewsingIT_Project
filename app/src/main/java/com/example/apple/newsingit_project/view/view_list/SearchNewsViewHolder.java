package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchNewsData;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchNewsViewHolder extends RecyclerView.ViewHolder {

    SearchNewsData searchNewsData;
    TextView titleView, contentView, dateView;


    public SearchNewsViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.text_search_news_title);
        contentView = (TextView) itemView.findViewById(R.id.text_search_news_content);
        dateView = (TextView)itemView.findViewById(R.id.text_search_news_date);

    }

    public void setSearchNewsData(SearchNewsData searchNewsData, Context context) {
        this.searchNewsData = searchNewsData;
        titleView.setText(searchNewsData.getTitle());
        contentView.setText(searchNewsData.getContent());
        dateView.setText(searchNewsData.getDate());
    }
}
