package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserFolderData;
import com.example.apple.newsingit_project.view.view_list.UserFolderViewHolder;

/**
 * Created by apple on 2016. 8. 25..
 */
public class UserFolderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //폴더 데이터 클래스 선언.//
    UserFolderData userfolderData;
    Context context;

    //생성자로 데이터 클래스 할당과 프래그먼트 자원 초기화.//
    public UserFolderListAdapter(Context context) {
        this.context = context;

        userfolderData = new UserFolderData();
    }

    //어댑터랑 연동되는 객체를 설정.//
    public void set_UserFolderDate(UserFolderData userfolderDate) {
        if (this.userfolderData != userfolderDate) {
            this.userfolderData = userfolderDate;

            notifyDataSetChanged();
        }
    }

    //어댑터랑 연동되는 객체를 설정.//
    public void init_FolderDate(UserFolderData userfolderDate) {
        if (this.userfolderData != userfolderDate) {
            this.userfolderData = userfolderDate;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    //리사이클뷰에 들어갈 뷰 초기화.//
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_userfolder_list, parent, false);

        //뷰 홀더 객체 설정.//
        UserFolderViewHolder holder = new UserFolderViewHolder(view);

        return holder;
    }

    //리사이클뷰에 들어갈 뷰의 아이템값 초기화.//
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < userfolderData.user_folder_list.size()) {
            final UserFolderViewHolder user_folderViewHolder = (UserFolderViewHolder) holder;

            user_folderViewHolder.set_UserFolder(userfolderData.user_folder_list.get(position), context);
        }
    }

    //리사이클뷰에 나타낼 뷰의 개수를 정의.//
    @Override
    public int getItemCount() {
        return userfolderData.user_folder_list.size(); //해당 폴더 목록만큼 반환.//
    }
}
