package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.view.view_list.SearchUserViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SearchUserData searchUserData;
    Context context;

    public SearchUserAdapter(Context context) {
        this.context = context;
        searchUserData = new SearchUserData();
    }

    public void setSearchUserData(SearchUserData searchUserData) {
        if (this.searchUserData != searchUserData) {
            this.searchUserData = searchUserData;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    public void initSearchUserData(SearchUserData searchUserData) {
        if (this.searchUserData != searchUserData) {
            this.searchUserData = searchUserData;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (searchUserData == null) {
            return 0;
        }
        return searchUserData.searchUserDataArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_user, parent, false);
        SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(view);
        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (searchUserData.searchUserDataArrayList.size() > 0) {
            if (searchUserData.searchUserDataArrayList.size() > position) {
                SearchUserViewHolder searchUserViewHolder = (SearchUserViewHolder) holder;
                searchUserViewHolder.setSearchUserData(searchUserData.searchUserDataArrayList.get(position), context);

                return;
            }
            position -= searchUserData.searchUserDataArrayList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }

}
