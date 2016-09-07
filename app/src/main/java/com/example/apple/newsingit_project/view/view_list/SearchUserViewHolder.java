package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserViewHolder extends RecyclerView.ViewHolder {

    public SearchUserData searchUserData;
    public TextView nameView, introView;
    // public Button btnSearchUser;
    public ImageView searchUserImgView;

    public String user_imgUrl;

    NetworkManager networkManager;

    public SearchUserViewHolder(View itemView) {
        super(itemView);
        nameView = (TextView) itemView.findViewById(R.id.text_search_user_name);
        introView = (TextView)itemView.findViewById(R.id.text_search_user_intro);
        // btnSearchUser = (Button) itemView.findViewById(R.id.btn_search_user);
        searchUserImgView = (ImageView) itemView.findViewById(R.id.img_search_user);
    }

    public void setSearchUserData(SearchUserData searchUserData, Context context) {
        this.searchUserData = searchUserData;
        nameView.setText(searchUserData.getName());
        introView.setText(searchUserData.getAboutMe());


        user_imgUrl = searchUserData.get_User_imgUrl();
        //이미지 뷰 작업.//
        if (user_imgUrl.equals("default")) {
            Picasso.with(context)
                    .load(R.mipmap.no_image)
                    .into(searchUserImgView); //into로 보낼 위젯 선택.//
        } else {
            //이미지를 로드.//
            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(user_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(searchUserImgView);
        }
    }
}
