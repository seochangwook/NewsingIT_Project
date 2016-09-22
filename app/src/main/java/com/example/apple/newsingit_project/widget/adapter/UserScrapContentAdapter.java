package com.example.apple.newsingit_project.widget.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.dialog.ScrapContentEditDialog;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.view.view_list.UserScrapContentViewHolder;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class UserScrapContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String SCRAP_ID = "SCRAP_ID";
    /**
     * 응답코드
     **/
    private final static int RC_EDITSCRAPDIALOG = 100;
    UserScrapContentData userScrapContentData;
    Context context;
    String whoflag;

    FontManager fontManager;

    public UserScrapContentAdapter(Context context) {
        this.context = context;
        fontManager = new FontManager(context);
        userScrapContentData = new UserScrapContentData();
    }

    public void setUserScrapContentData(UserScrapContentData userScrapContentData, String whoflag) {
        if (this.userScrapContentData != userScrapContentData) {
            this.userScrapContentData = userScrapContentData;
            this.whoflag = whoflag;
        }

        notifyDataSetChanged();
    }

    public void init_ScrapContent_Data(UserScrapContentData userScrapContentData, String whoflag) {
        if (this.userScrapContentData != userScrapContentData) {
            this.userScrapContentData = userScrapContentData;
            this.whoflag = whoflag;
        }

        notifyDataSetChanged();
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

                uvh.setUserScrapContent(userScrapContentData.userScrapContentDataList.get(position), context, whoflag);

                uvh.settingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //popup window//
                        String userSelect = userScrapContentData.userScrapContentDataList.get(pos).getNcTitle().toString();
                        int scrapId = userScrapContentData.userScrapContentDataList.get(pos).getId();

                        //팝업창//
                        Intent intent = new Intent(context, ScrapContentEditDialog.class);
                        intent.putExtra(SCRAP_ID, "" + scrapId);

                        //필요한 정보를 전송.//
                        ((Activity) context).startActivityForResult(intent, RC_EDITSCRAPDIALOG);

                    }
                });

                //현재 사용자 구분에 따라 나눠준다.//
                if (whoflag.equals("1")) {
                    //다른 사람(내가 좋아요를 했는지 안했는지에 유무에 따라 결정)//
                    uvh.likeButton.setEnabled(false);

                    //좋아요 유무를 가져온다.//
                    String like_flag = "" + userScrapContentData.userScrapContentDataList.get(position).getLikeFlag();

                    if (like_flag.equals("false")) //그 해당사람의 좋아요가 아무도 누르지 않았을 경우//
                    {
                        uvh.likeButton.setImageResource(R.mipmap.btn_heart_stroke); //내 스크랩 좋아요는 항상 같은 이미지 사용//
                    } else if (like_flag.equals("true")) //해당 사람의 좋아요가 1개이상일 경우//
                    {
                        uvh.likeButton.setImageResource(R.mipmap.btn_heart_fill); //내 스크랩 좋아요는 항상 같은 이미지 사용//
                    }

                } else {
                    //내 스크랩(현재 내 좋아요의 수에 따라서 0개면 빈하트, 1개이상이면 채워진 하트로 만듬)//
                    uvh.likeButton.setEnabled(false);

                    uvh.likeButton.setImageResource(R.mipmap.btn_heart_fill); //내 스크랩 좋아요는 항상 같은 이미지 사용//
                }

                uvh.titleView.setTypeface(fontManager.getTypefaceBoldInstance());
                uvh.ncTitleView.setTypeface(fontManager.getTypefaceMediumInstance());
                uvh.likeView.setTypeface(fontManager.getTypefaceMediumInstance());
                uvh.authorView.setTypeface(fontManager.getTypefaceRegularInstance());
                uvh.dateView.setTypeface(fontManager.getTypefaceRegularInstance());

                return;
            }

            position -= userScrapContentData.userScrapContentDataList.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    //Adapter에서는 onActivityResult를 사용할 수 없으므로 Activity에서 콜백형식으로 이벤트를 받는다.//
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_EDITSCRAPDIALOG) {
                Log.d("json data", "스크랩 리스트 정보 수정 완료");

                userScrapContentData = new UserScrapContentData();

                notifyDataSetChanged();
            }
        }
    }
}
