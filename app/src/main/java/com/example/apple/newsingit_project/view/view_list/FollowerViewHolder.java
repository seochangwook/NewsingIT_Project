package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowerData;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowerViewHolder  extends RecyclerView.ViewHolder {

    public FollowerData followerData;
    public TextView nameView, introView;
    public ImageButton btnFollower;
    public ImageView imgFollower;


    public FollowerViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView.findViewById(R.id.text_fer_name);
        // introView = (TextView)itemView.findViewById(R.id.text_fer_intro);
        btnFollower = (ImageButton) itemView.findViewById(R.id.btn_follower);
        imgFollower = (ImageView)itemView.findViewById(R.id.img_follower);
    }

    public void setFollowerData(FollowerData followerData, Context context){
        this.followerData = followerData;
        nameView.setText(followerData.getName());
        //  introView.setText(followerData.getAboutMe());
    }
}
