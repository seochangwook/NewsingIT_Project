package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserFolderData;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by apple on 2016. 8. 25..
 */
public class UserFolderViewHolder extends RecyclerView.ViewHolder {
    public ImageView user_folder_imageview;
    public TextView user_folder_name_textview;
    public Button user_folder_private_button;
    public boolean user_folder_private;
    public String folder_imgUrl;
    //폴더 데이터 클래스//
    UserFolderData userfolderData;

    public UserFolderViewHolder(View itemView) {
        super(itemView);

        user_folder_imageview = (ImageView) itemView.findViewById(R.id.user_folder_imageview);
        user_folder_name_textview = (TextView) itemView.findViewById(R.id.user_folder_name_textview);
        user_folder_private_button = (Button) itemView.findViewById(R.id.user_folder_private_button);
    }

    public void set_UserFolder(UserFolderData userfolderData, Context context) {
        this.userfolderData = userfolderData;

        user_folder_private = userfolderData.get_folder_private();

        if (user_folder_private == false) //공개 처리일 경우//
        {

            user_folder_private_button.setVisibility(View.GONE);

            folder_imgUrl = userfolderData.get_folder_imageUrl();

            //이미지를 로드.//
            Picasso.with(context)
                    .load(folder_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(user_folder_imageview);

        } else if (user_folder_private == true) //비공개 처리일 경우//
        {

            folder_imgUrl = userfolderData.get_folder_imageUrl();

            //이미지를 로드.//
            Picasso.with(context)
                    .load(folder_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(user_folder_imageview);
        }

        //폴더 정보 불러오기//
        user_folder_name_textview.setText(userfolderData.get_folder_name());
    }
}