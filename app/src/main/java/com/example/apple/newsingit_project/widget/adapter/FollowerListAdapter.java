package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.view.view_list.FollowerViewHolder;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    FollowerData followerData;
    Context context;


    public FollowerListAdapter(Context context) {
        this.context = context;
        followerData = new FollowerData();

    }

    public void setFollowerData(FollowerData followerData){
        if (this.followerData != followerData) {
            this.followerData = followerData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (followerData == null) {
            return 0;
        }
        return followerData.followerDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_follower_list, parent, false);
        FollowerViewHolder holder = new FollowerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (followerData.followerDataList.size() > 0) {
            if (position < followerData.followerDataList.size()) {
                final FollowerViewHolder fvh = (FollowerViewHolder)holder;
                final int pos = position;
                fvh.setFollowerData(followerData.followerDataList.get(position));

                fvh.btnFollower.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                        String userSelectFollower = followerData.followerDataList.get(pos).getName().toString();
                        Toast.makeText(context, "팔로워 " + userSelectFollower, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            position -= followerData.followerDataList.size();

        }
        throw new IllegalArgumentException("invalid position");
    }
}
