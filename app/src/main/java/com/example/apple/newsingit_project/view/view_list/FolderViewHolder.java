package com.example.apple.newsingit_project.view.view_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FolderData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

/**
 * Created by apple on 2016. 8. 24..
 */
public class FolderViewHolder extends RecyclerView.ViewHolder {
    public ImageView folder_imageview;
    public TextView folder_name_textview;
    public Button folder_private_button;
    public boolean folder_private;

    //폴더 데이터 클래스//
    FolderData folderData;

    /**
     * Network관련 변수
     **/
    NetworkManager networkManager;

    public FolderViewHolder(View itemView) {
        super(itemView);

        folder_imageview = (ImageView) itemView.findViewById(R.id.folder_imageview);
        folder_name_textview = (TextView) itemView.findViewById(R.id.folder_name_textview);
        folder_private_button = (Button) itemView.findViewById(R.id.folder_private_button);
    }

    public void set_Folder(FolderData folderData, Context context) {
        this.folderData = folderData;

        folder_private = folderData.get_folder_private();

        if (folder_private == false) {
            folder_private_button.setBackgroundResource(R.mipmap.btn_smalllock);
        }
//        else if (folder_private == true) {
//            folder_private_button.setBackgroundResource(android.R.drawable.ic_secure);
//        }

        //폴더 정보 불러오기//
        folder_name_textview.setText(folderData.get_folder_name());


        String folder_imageUrl = folderData.get_folder_imageUrl();

        Log.d("folder image", folder_imageUrl);

        if (folder_imageUrl.equals("default")) //이미지가 없는 경우//
        {
            Picasso.with(context)
                    .load(R.mipmap.folder_default_image)
                    .into(folder_imageview); //into로 보낼 위젯 선택.//
        } else {
            networkManager = NetworkManager.getInstance();

            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(folder_imageUrl)
                    .into(folder_imageview);
        }
    }
}
