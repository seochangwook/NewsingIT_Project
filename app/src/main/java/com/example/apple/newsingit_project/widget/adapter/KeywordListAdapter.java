package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.KeywordData;
import com.example.apple.newsingit_project.view.view_list.KeywordViewHolder;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class KeywordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    KeywordData keywordData;
    Context context;

    public KeywordListAdapter(Context context) {
        this.context = context;
        keywordData = new KeywordData();
    }

    public void setKeywordDataList(KeywordData keywordData) {
        if (this.keywordData != keywordData) {
            this.keywordData = keywordData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (keywordData == null) {
            return 0;
        }
        return keywordData.keywordDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_keyword_list, parent, false);
        KeywordViewHolder holder = new KeywordViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (keywordData.keywordDataList.size() > 0) {
            if (position < keywordData.keywordDataList.size()) {
                KeywordViewHolder keywordViewHolder = (KeywordViewHolder) holder;
                keywordViewHolder.setKeywordData(keywordData.keywordDataList.get(position));
                return;
            }
            position -= keywordData.keywordDataList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }

}
