package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowingData;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowingViewHolder  extends RecyclerView.ViewHolder {
    public FollowingData followingData;
    public TextView nameView, introView;
    public Button btnFollowing;
    public ImageView imgFollowing;


    public FollowingViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView.findViewById(R.id.text_fing_name);
        introView = (TextView)itemView.findViewById(R.id.text_fing_intro);
        btnFollowing = (Button)itemView.findViewById(R.id.btn_following);
        imgFollowing = (ImageView)itemView.findViewById(R.id.img_following);
    }

    public void setFollowingData(FollowingData followingData, Context context){
        this.followingData = followingData;
        nameView.setText(followingData.getName());
        introView.setText(followingData.getAboutMe());
    }
}
