package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchNewsData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.view.view_list.SearchNewsViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SearchNewsData searchNewsData;
    Context context;
    FontManager fontManager;

    public SearchNewsAdapter(Context context) {
        this.context = context;
        searchNewsData = new SearchNewsData();
        fontManager = new FontManager(context);
    }

    public void setSearchNewsData(SearchNewsData searchNewsData) {
        if (this.searchNewsData != searchNewsData) {
            this.searchNewsData = searchNewsData;
        }
        notifyDataSetChanged();
    }

    public void initSearchNewsData(SearchNewsData searchNewsData) {
        if (this.searchNewsData != searchNewsData) {
            this.searchNewsData = searchNewsData;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (searchNewsData == null) {
            return 0;
        }

        return searchNewsData.searchNewsDataArrayList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_news, parent, false);
        SearchNewsViewHolder searchNewsViewHolder = new SearchNewsViewHolder(view);
        return searchNewsViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (searchNewsData.searchNewsDataArrayList.size() > 0) {
            if (searchNewsData.searchNewsDataArrayList.size() > position) {
                SearchNewsViewHolder searchNewsViewHolder = (SearchNewsViewHolder) holder;
                searchNewsViewHolder.setSearchNewsData(searchNewsData.searchNewsDataArrayList.get(position), context);

                searchNewsViewHolder.authorView.setTypeface(fontManager.getTypefaceRegularInstance());
                searchNewsViewHolder.dateView.setTypeface(fontManager.getTypefaceRegularInstance());
                searchNewsViewHolder.titleView.setTypeface(fontManager.getTypefaceRegularInstance());

                return;
            }
            position -= searchNewsData.searchNewsDataArrayList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }
}
