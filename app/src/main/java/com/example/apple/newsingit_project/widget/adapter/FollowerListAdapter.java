package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                .add("ofid", userId)
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
                .addPathSegment(userId);

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

                followerViewHolder.setFollowerData(followerData.followerDataList.get(position), context);

                final int pos = position;
                boolean flag = followerData.followerDataList.get(pos).getFlag();

                Log.d("json control", "(팔로워리스트)" + flag);

                if (flag == true) //true이면 선택한 유저를 팔로우 한 상태//
                {
                    //팔로잉 한 상태에서는 팔로우 해제//
                    followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_following);
                } else if (flag == false) //false이면 선택한 유저를 팔로우 하지 않은 상태//
                {
                    //팔로잉 안 한 상태에서 팔로우 생성//
                    followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_follow);
                }


                followerViewHolder.btnFollower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userSelectId = followerData.followerDataList.get(pos).getId();
                        boolean flag = followerData.followerDataList.get(pos).getFlag();

                        if (flag == true) //현재 팔로잉을 한 상태.//
                        {
                            Log.d("json control", "(팔로워리스트)" + flag);

                            //팔로잉 한 상태에서는 팔로우 해제//
                            Log.d("json control", "(팔로워리스트)팔로잉을 현재 한 상태이므로 팔로잉을 해제");

                            deleteFollowing(userSelectId);

                            followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_follow);

                            notifyDataSetChanged();
                        } else if (flag == false) //현재 팔로잉이 되어있지 않은상태.//
                        {
                            Log.d("json control", "(팔로워리스트)" + flag);

                            //팔로잉 안 한 상태에서 팔로우 생성//
                            Log.d("json control", "(팔로워리스트)팔로잉을 현재 하지 않은 상태미으로 팔로잉을 생성");

                            setFollowing(userSelectId);

                            followerViewHolder.btnFollower.setImageResource(R.mipmap.btn_following);

                            notifyDataSetChanged();
                        }

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
