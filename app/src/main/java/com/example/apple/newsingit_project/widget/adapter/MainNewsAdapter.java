package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.MainNewsData;
import com.example.apple.newsingit_project.view.view_list.MainNewsViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class MainNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    MainNewsData mainNewsData;
    Context context;

    public MainNewsAdapter(Context context) {
        this.context = context;
        mainNewsData = new MainNewsData();
    }

    public void setMainNewsData(MainNewsData mainNewsData) {
        if (this.mainNewsData != mainNewsData) {
            this.mainNewsData = mainNewsData;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        if (mainNewsData == null) {
            return 0;
        }
        return mainNewsData.mainNewsDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_news, parent, false);
        MainNewsViewHolder holder = new MainNewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mainNewsData.mainNewsDataList.size() > 0) {
            if (position < mainNewsData.mainNewsDataList.size()) {
                MainNewsViewHolder mvh = (MainNewsViewHolder) holder;
                mvh.setMainNewsData(mainNewsData.mainNewsDataList.get(position));
                return;
            }
            position -= mainNewsData.mainNewsDataList.size();

        }
        throw new IllegalArgumentException("invalid position");
    }

}
