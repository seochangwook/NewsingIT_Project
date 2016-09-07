package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.view_list.FollowingViewHolder;

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
 * Created by Tacademy on 2016-08-24.
 */
public class FollowingListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    FollowingData followingData, searchList;
    Context context;

    NetworkManager networkManager;
    private Callback requestSetFollowingCallback = new Callback() {
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

    public FollowingListAdapter(Context context) {
        this.context = context;
        followingData = new FollowingData();
        searchList = new FollowingData();

    }

    private void setFollowing(int userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows");

        RequestBody body = new FormBody.Builder()
                .add("id", "" + userId)
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .post(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);

    }

    private void deleteFollowing(int userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addPathSegment("" + userId);

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);
    }

    public void setFollowingData(FollowingData followingData){
        if (this.followingData != followingData) {
            this.followingData = followingData;
            searchList = followingData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (followingData == null) {
            return 0;
        }
        return followingData.followingDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_following_list, parent, false);
        FollowingViewHolder holder = new FollowingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (followingData.followingDataList.size() > 0) {
            if (position < followingData.followingDataList.size()) {
                final FollowingViewHolder followingViewHolder = (FollowingViewHolder) holder;
                final int pos = position;
                followingViewHolder.setFollowingData(followingData.followingDataList.get(position), context);

                final boolean flag = followingData.followingDataList.get(pos).getFlag();

                if (flag) {//true이면 선택한 유저를 팔로우 한 상태//
                    //팔로잉 한 상태에서는 팔로우 해제//
                    followingViewHolder.btnFollowing.setText("팔로우 해제");
                    followingViewHolder.btnFollowing.setBackground(context.getResources().getDrawable(R.drawable.btn_follow_do));
                } else {//false이면 선택한 유저를 팔로우 하지 않은 상태//
                    //팔로잉 안 한 상태에서 팔로우 생성//
                    followingViewHolder.btnFollowing.setText("팔로우");
                    followingViewHolder.btnFollowing.setBackground(context.getResources().getDrawable(R.drawable.btn_follow_cancel));
                }

                followingViewHolder.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int userSelectId = followingData.followingDataList.get(pos).getId();
                        Toast.makeText(context, "팔로잉 " + userSelectId, Toast.LENGTH_SHORT).show();

                        if (flag) { //true이면 선택한 유저를 팔로우 한 상태//
                            //팔로잉 한 상태에서는 팔로우 해제//
                            deleteFollowing(userSelectId);
                        } else { //false이면 선택한 유저를 팔로우 하지 않은 상태//
                            //팔로잉 안 한 상태에서 팔로우 생성//
                            setFollowing(userSelectId);
                        }

                        //실제 네트워크 통신 전까지 안드로이드에서 값을 임시로 바꿔줌//
                        followingData.followingDataList.get(pos).setFlag(!flag);

                        notifyDataSetChanged();
                    }
                });
                return;
            }
            position -= followingData.followingDataList.size();

        }
        throw new IllegalArgumentException("invalid position");
    }


}
