package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.dialog.ScrapContentEditDialog;
import com.example.apple.newsingit_project.view.view_list.UserScrapContentViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    UserScrapContentData userScrapContentData;
    Context context;
    String whoflag;

    /**
     * 팝업 관련 변수
     **/
    PopupWindow image_select_popup;
    View image_select_popup_view;

    public UserScrapContentAdapter(Context context) {
        this.context = context;
        userScrapContentData = new UserScrapContentData();
    }

    public void setUserScrapContentData(UserScrapContentData userScrapContentData, String whoflag) {
        if (this.userScrapContentData != userScrapContentData) {
            this.userScrapContentData = userScrapContentData;
            this.whoflag = whoflag;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        if (userScrapContentData == null) {
            return 0;
        }
        return userScrapContentData.userScrapContentDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_scrap_list, parent, false);
        UserScrapContentViewHolder holder = new UserScrapContentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (userScrapContentData.userScrapContentDataList.size() > 0) {
            if (position < userScrapContentData.userScrapContentDataList.size()) {
                final UserScrapContentViewHolder uvh = (UserScrapContentViewHolder) holder;
                final int pos = position;
                final boolean flag = userScrapContentData.userScrapContentDataList.get(pos).getLikeFlag();

                uvh.setUserScrapContent(userScrapContentData.userScrapContentDataList.get(position), context, whoflag);

                //좋아요를 눌렀을 때와 누르지 않았을 때//
                //이미지 빈 하트 -> 채워진 하트//
                if (flag) {  //좋아요//
                    uvh.likeButton.setImageResource(android.R.drawable.star_big_on);
                } else { //좋아요 취소//
                    uvh.likeButton.setImageResource(android.R.drawable.star_big_off);
                }

                uvh.settingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //popup window//
                        String userSelect = userScrapContentData.userScrapContentDataList.get(pos).getContent().toString();

                        //팝업창//
                        Intent intent = new Intent(context, ScrapContentEditDialog.class);

                        //필요한 정보를 전송.//
                        context.startActivity(intent);
                    }
                });

                if (whoflag.equals("1")) { //다른 사람//
                    uvh.likeButton.setEnabled(true);
                } else { //내 스크랩//
                    uvh.likeButton.setEnabled(false);
                }


                uvh.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = userScrapContentData.userScrapContentDataList.get(pos).getLike();

                        if (flag) { //좋아요 한 상태 -> 좋아요 취소 작업//
                            count--;
                            setFavoriteCancel();
                        } else { //좋아요 안 한 상태 -> 좋아요 할거야//
                            count++;
                            setFavoriteDo();
                        }

                        userScrapContentData.userScrapContentDataList.get(pos).setLike(count); //좋아요 갯수 setting//
                        userScrapContentData.userScrapContentDataList.get(pos).setLikeFlag(!flag); //좋아요 flag setting//

                        notifyDataSetChanged();
                    }
                });

                return;
            }

            position -= userScrapContentData.userScrapContentDataList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }

    private void setFavoriteCancel() {

    }

    private void setFavoriteDo() {

    }


}
