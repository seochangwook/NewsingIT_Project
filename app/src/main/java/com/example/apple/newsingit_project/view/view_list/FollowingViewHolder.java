package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowingViewHolder  extends RecyclerView.ViewHolder {
    public FollowingData followingData;
    public TextView nameView, introView;
    public ImageButton btnFollowing;
    public ImageView imgFollowing;

    NetworkManager networkManager;

    public FollowingViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView)itemView.findViewById(R.id.text_fing_name);
        // introView = (TextView)itemView.findViewById(R.id.text_fing_intro);
        btnFollowing = (ImageButton) itemView.findViewById(R.id.btn_following);
        imgFollowing = (ImageView)itemView.findViewById(R.id.img_following);
    }

    public void setFollowingData(FollowingData followingData, Context context){
        this.followingData = followingData;
        nameView.setText(followingData.getName());
        //  introView.setText(followingData.getAboutMe());

        String image_Url = followingData.getProfileUrl();

        if (image_Url.equals("default")) //이미지가 없는 경우//
        {
            Picasso.with(context)
                    .load(R.mipmap.profile_image)
                    .into(imgFollowing); //into로 보낼 위젯 선택.//
        } else {
            networkManager = NetworkManager.getInstance();

            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(image_Url)
                    .into(imgFollowing);
        }
    }
}
