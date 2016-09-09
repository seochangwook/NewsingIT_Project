package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.view_list.FollowerViewHolder;

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
public class FollowerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    FollowerData followerData;
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


    public FollowerListAdapter(Context context) {
        this.context = context;
        followerData = new FollowerData();

    }

    private void setFollowing(String userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows");

        RequestBody body = new FormBody.Builder()
                .add("ofid", "" + userId)
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .post(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);

    }

    private void deleteFollowing(String userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addPathSegment("ofid" + userId);

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);
    }

    public void setFollowerData(FollowerData followerData){
        if (this.followerData != followerData) {
            this.followerData = followerData;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (followerData == null) {
            return 0;
        }
        return followerData.followerDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_follower_list, parent, false);
        FollowerViewHolder holder = new FollowerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (followerData.followerDataList.size() > 0) {
            if (position < followerData.followerDataList.size()) {
                final FollowerViewHolder followerViewHolder = (FollowerViewHolder) holder;
                final int pos = position;
                followerViewHolder.setFollowerData(followerData.followerDataList.get(position), context);

                final boolean flag = followerData.followerDataList.get(pos).getFlag();

                if (flag) {//true이면 선택한 유저를 팔로우 한 상태//
                    //팔로잉 한 상태에서는 팔로우 해제//
                    followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_following);
                    //    followerViewHolder.btnFollower.setText("팔로우 해제");
                    //   followerViewHolder.btnFollower.setBackground(context.getResources().getDrawable(R.drawable.btn_follow_do));
                } else {//false이면 선택한 유저를 팔로우 하지 않은 상태//
                    //팔로잉 안 한 상태에서 팔로우 생성//
                    followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_follow);
                    //     followerViewHolder.btnFollower.setText("팔로우");
                    //   followerViewHolder.btnFollower.setBackground(context.getResources().getDrawable(R.drawable.btn_follow_cancel));
                }


                followerViewHolder.btnFollower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userSelectId = followerData.followerDataList.get(pos).getId();
                        Toast.makeText(context, "팔로워 " + userSelectId, Toast.LENGTH_SHORT).show();

                        if (flag) {
                            //팔로잉 한 상태에서는 팔로우 해제//
                            deleteFollowing(userSelectId);
                        } else {
                            //팔로잉 안 한 상태에서 팔로우 생성//
                            setFollowing(userSelectId);
                        }

                        //실제 네트워크 통신 전까지 안드로이드에서 값을 임시로 바꿔줌//
                        followerData.followerDataList.get(pos).setFlag(!flag);

                        notifyDataSetChanged();
                    }
                });
                return;
            }
            position -= followerData.followerDataList.size();

        }
        throw new IllegalArgumentException("invalid position");
    }
}
