package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class FollowerViewHolder  extends RecyclerView.ViewHolder {

    /**
     * 초기 페이스북 디폴트 경로
     **/
    private static final String DEFAULT_FACEBOOK_IMG_PATH = "https://graph.facebook.com";
    public FollowerData followerData;
    public TextView nameView;
    public ImageButton btnFollower;
    public ImageView imgFollower;
    NetworkManager networkManager;

    public FollowerViewHolder(View itemView) {
        super(itemView);

        nameView = (TextView)itemView.findViewById(R.id.text_fer_name);
        btnFollower = (ImageButton) itemView.findViewById(R.id.btn_follower);
        imgFollower = (ImageView)itemView.findViewById(R.id.img_follower);
    }

    public void setFollowerData(FollowerData followerData, Context context){
        this.followerData = followerData;
        nameView.setText(followerData.getName());

        String image_Url = followerData.getProfileUrl();

        Log.d("json control", image_Url);

        String parsing_imageurl = image_Url.substring(0, 26); //문자열 자르기//

        Log.d("json control", parsing_imageurl);

        //페이스북 이미지는 일반적인 피카소로 적용//
        if (parsing_imageurl.equals(DEFAULT_FACEBOOK_IMG_PATH)) {
            Picasso.with(context)
                    .load(image_Url)
                    .transform(new CropCircleTransformation())
                    .into(imgFollower); //into로 보낼 위젯 선택.//
        } else if (image_Url.equals("default")) //이미지가 없는 경우//
        {
            Picasso.with(context)
                    .load(R.mipmap.profile_image)
                    .into(imgFollower); //into로 보낼 위젯 선택.//
        } else {
            networkManager = NetworkManager.getInstance();

            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(image_Url)
                    .into(imgFollower);
        }
    }
}
