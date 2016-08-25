package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.view.view_list.FollowingViewHolder;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowingListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FollowingData followingData;
    Context context;


    public FollowingListAdapter(Context context) {
        this.context = context;
        followingData = new FollowingData();

    }

    public void setFollowingData(FollowingData followingData){
        if (this.followingData != followingData) {
            this.followingData = followingData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (followingData == null) {
            return 0;
        }
        return followingData.followingDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_following_list, parent, false);
        FollowingViewHolder holder = new FollowingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (followingData.followingDataList.size() > 0) {
            if (position < followingData.followingDataList.size()) {
                final FollowingViewHolder followingViewHolder = (FollowingViewHolder) holder;
                final int pos = position;
                followingViewHolder.setFollowingData(followingData.followingDataList.get(position));

                followingViewHolder.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userSelectFollowing = followingData.followingDataList.get(pos).getName().toString();
                        Toast.makeText(context, "팔로잉 " + userSelectFollowing, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            position -= followingData.followingDataList.size();

        }
        throw new IllegalArgumentException("invalid position");
    }

}
