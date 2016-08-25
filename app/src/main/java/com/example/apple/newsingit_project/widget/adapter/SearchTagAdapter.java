package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchTagData;
import com.example.apple.newsingit_project.view.view_list.SearchTagViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SearchTagData searchTagData;
    Context context;

    public SearchTagAdapter(Context context) {
        this.context = context;
        searchTagData = new SearchTagData();
    }

    public void setSearchTagData(SearchTagData searchTagData) {
        if (this.searchTagData != searchTagData) {
            this.searchTagData = searchTagData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (searchTagData == null) {
            return 0;
        }
        return searchTagData.searchTagDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_tag, parent, false);
        SearchTagViewHolder searchTagViewHolder = new SearchTagViewHolder(view);
        return searchTagViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (searchTagData.searchTagDataList.size() > 0) {
            if (searchTagData.searchTagDataList.size() > position) {
                SearchTagViewHolder searchTagViewHolder = (SearchTagViewHolder) holder;
                searchTagViewHolder.setSearchTagData(searchTagData.searchTagDataList.get(position), context);
                return;
            }
            position -= searchTagData.searchTagDataList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }
}
