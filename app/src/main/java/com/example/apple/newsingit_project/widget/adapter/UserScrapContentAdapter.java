package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.view.view_list.UserScrapContentViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    UserScrapContentData userScrapContentData;
    Context context;

    public UserScrapContentAdapter(Context context) {
        this.context = context;
        userScrapContentData = new UserScrapContentData();
    }

    public void setUserScrapContentData(UserScrapContentData userScrapContentData) {
        if (this.userScrapContentData != userScrapContentData) {
            this.userScrapContentData = userScrapContentData;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        if (userScrapContentData == null) {
            return 0;
        }
        return userScrapContentData.userScrapContentDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_scrap_list, parent, false);
        UserScrapContentViewHolder holder = new UserScrapContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (userScrapContentData.userScrapContentDataList.size() > 0) {
            if (position < userScrapContentData.userScrapContentDataList.size()) {
                final UserScrapContentViewHolder uvh = (UserScrapContentViewHolder) holder;
                final int pos = position;
                uvh.setUserScrapContent(userScrapContentData.userScrapContentDataList.get(position));

                uvh.settingButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //popup window//
                        String userSelect = userScrapContentData.userScrapContentDataList.get(pos).getContent().toString();
                        Toast.makeText(context, userSelect + " 스크랩 설정", Toast.LENGTH_SHORT).show();
                    }
                });

                return;
            }
            position -= userScrapContentData.userScrapContentDataList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }

}
