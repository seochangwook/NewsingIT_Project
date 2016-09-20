package com.example.apple.newsingit_project.widget.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.dialog.ScrapContentEditDialog;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.view_list.UserScrapContentViewHolder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    /**
     * 팝업 관련 변수
     **/
    PopupWindow image_select_popup;
    View image_select_popup_view;
    /**
     * Network관련 변수
     **/
    NetworkManager networkManager;
    private Callback requestFavoriteCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);


        }
    };

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
                final boolean flag = userScrapContentData.userScrapContentDataList.get(pos).getLikeFlag();

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
                    //다른 사람//
                    uvh.likeButton.setEnabled(true);
                    //좋아요를 눌렀을 때와 누르지 않았을 때//

                    if (flag) {  //좋아요//
                        uvh.likeButton.setImageResource(R.mipmap.favorite_on);
                    } else { //좋아요 취소//
                        uvh.likeButton.setImageResource(R.mipmap.favorite_off);
                    }
                } else {
                    //내 스크랩//
                    uvh.likeButton.setEnabled(false);
                    //uvh.likeButton.setImageResource(R.mipmap.favorite_on); //내 스크랩 좋아요는 항상 같은 이미지 사용//

                    int favorite_count = Integer.parseInt("" + uvh.likeView.getText());

                    if (favorite_count == 0) {
                        uvh.likeButton.setImageResource(R.mipmap.favorite_off); //내 스크랩 좋아요는 항상 같은 이미지 사용//
                    } else if (favorite_count > 0) {
                        uvh.likeButton.setImageResource(R.mipmap.favorite_on); //내 스크랩 좋아요는 항상 같은 이미지 사용//
                    }
                }

                uvh.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = userScrapContentData.userScrapContentDataList.get(pos).getLike();
                        String select_scrap_id = "" + userScrapContentData.userScrapContentDataList.get(pos).getId();

                        //flag유무에 따라 서로 다른 이벤트 처리//
                        if (flag) { //좋아요 한 상태 -> 좋아요 취소 작업//
                            count--;

                            setFavoriteCancel(select_scrap_id);

                        } else { //좋아요 안 한 상태 -> 좋아요 할거야//
                            count++;

                            setFavoriteDo(select_scrap_id);

                            //푸시알람//
                            increease_like_pushalarm();
                        }

                        //실제 네트워크 통신 전까지 안드로이드에서 값을 임시로 바꿔줌//
                        userScrapContentData.userScrapContentDataList.get(pos).setLike(count); //좋아요 갯수 setting//
                        userScrapContentData.userScrapContentDataList.get(pos).setLikeFlag(!flag); //좋아요 flag setting//

                        notifyDataSetChanged();
                    }
                });

                uvh.titleView.setTypeface(fontManager.getTypefaceBoldInstance());
                uvh.authorView.setTypeface(fontManager.getTypefaceRegularInstance());
                uvh.dateView.setTypeface(fontManager.getTypefaceRegularInstance());
                uvh.likeView.setTypeface(fontManager.getTypefaceMediumInstance());
                uvh.ncTitleView.setTypeface(fontManager.getTypefaceMediumInstance());

                return;
            }

            position -= userScrapContentData.userScrapContentDataList.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    /**
     * 좋아요의 경우에 따라 이벤트 처리
     **/
    private void setFavoriteCancel(String select_scrap_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("scraps")
                .addPathSegment(select_scrap_id)
                .addPathSegment("favorites")
                .addPathSegment("me");

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestFavoriteCallback);

    }

    private void setFavoriteDo(String select_scrap_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("scraps")
                .addPathSegment(select_scrap_id)
                .addPathSegment("favorites");

        //POST방식으로 구성하게 되면 RequestBody가 필요(데이터 전달 시)//
        RequestBody body = new FormBody.Builder()
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFavoriteCallback);
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

    public void increease_like_pushalarm() {
        //푸시알람 설정//

    }
}
