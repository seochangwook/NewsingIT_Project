package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchTagData;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchTagViewHolder extends RecyclerView.ViewHolder {

    SearchTagData searchTagData;
    Context context;
    TextView tagView;

    public SearchTagViewHolder(View itemView) {
        super(itemView);
        tagView = (TextView) itemView.findViewById(R.id.text_search_tag);
    }

    public void setSearchTagData(SearchTagData searchTagData, Context context) {
        this.searchTagData = searchTagData;
        tagView.setText(searchTagData.getTag());
    }
}
