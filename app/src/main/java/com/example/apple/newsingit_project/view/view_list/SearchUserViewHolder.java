package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    /**
     * 초기 페이스북 디폴트 경로
     **/
    private static final String DEFAULT_FACEBOOK_IMG_PATH = "https://graph.facebook.com";
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
        searchUserImgView = (ImageView) itemView.findViewById(R.id.img_search_user);
    }

    public void setSearchUserData(SearchUserData searchUserData, Context context) {
        this.searchUserData = searchUserData;
        nameView.setText(searchUserData.getName());
        introView.setText(searchUserData.getAboutMe());

        user_imgUrl = searchUserData.get_User_imgUrl();

        Log.d("json control", user_imgUrl);

        String parsing_imageurl = user_imgUrl.substring(0, 26); //문자열 자르기//

        Log.d("json control", parsing_imageurl);

        //페이스북 이미지는 일반적인 피카소로 적용//
        if (parsing_imageurl.equals(DEFAULT_FACEBOOK_IMG_PATH)) {
            Picasso.with(context)
                    .load(user_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(searchUserImgView); //into로 보낼 위젯 선택.//
        }

        //이미지 뷰 작업.//
        else if (user_imgUrl.equals("default")) {
            Picasso.with(context)
                    .load(R.mipmap.ic_image_default)
                    .into(searchUserImgView); //into로 보낼 위젯 선택.//
        } else {
            //이미지를 로드.//
            networkManager = NetworkManager.getInstance();

            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(user_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(searchUserImgView);
        }
    }
}
