package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    final boolean[] isOk = {false};
    FollowingData followingData;
    Context context;
    NetworkManager networkManager;
    int pos;
    FollowingViewHolder followingViewHolder;
    boolean flag;
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

            if (response.code() == 401) {

            } else if (response.code() == 200) {
                isOk[0] = true;
            }
        }
    };
    private Callback requestDeleteFollowingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            if (response.code() == 401) {

            } else if (response.code() == 200) {

            }
        }
    };

    public FollowingListAdapter(Context context) {
        this.context = context;
        followingData = new FollowingData();
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

        client.newCall(request).enqueue(requestDeleteFollowingCallback);
    }


    public void setFollowingData(FollowingData followingData){

        if (this.followingData != followingData) {
            this.followingData = followingData;
        }
        notifyDataSetChanged();
    }

    public void initFollowingData(FollowingData followingData) {
        if (this.followingData != followingData) {
            this.followingData = followingData;
        }
        notifyDataSetChanged();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (followingData.followingDataList.size() > 0) {
            if (position < followingData.followingDataList.size()) {
                followingViewHolder = (FollowingViewHolder) holder;
                followingViewHolder.setFollowingData(followingData.followingDataList.get(position), context);

                pos = position;
                flag = followingData.followingDataList.get(pos).getFlag();
                Log.d("json position", "" + position);

                if (flag == true) {//true이면 선택한 유저를 팔로우 한 상태//
                    //팔로잉 한 상태에서는 팔로우 해제//
                    followingViewHolder.btnFollowing.setImageResource(R.mipmap.btn_following);
                } else if (flag == false) {//false이면 선택한 유저를 팔로우 하지 않은 상태//
                    //팔로잉 안 한 상태에서 팔로우 생성//
                    followingViewHolder.btnFollowing.setImageResource(R.mipmap.btn_follow);
                }

                followingViewHolder.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String userSelectId = followingData.followingDataList.get(position).getId();
                        final boolean flag = followingData.followingDataList.get(position).getFlag();
                        String name = followingData.followingDataList.get(position).getName();

                        if (flag == true) //true이면 선택한 유저를 팔로우 한 상태//
                        {
                            //팔로잉 한 상태에서는 팔로우 해제//
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setMessage(name + " 님의 팔로우를 취소 하시겠어요?").setCancelable(false).setPositiveButton("팔로우 취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //yes
                                            deleteFollowing(userSelectId);
                                            followingData.followingDataList.get(position).setFlag(!flag);
                                            notifyDataSetChanged();
                                        }
                                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //no

                                }
                            });
                            AlertDialog alert = alertDialog.create();
                            alert.show();

                        } else if (flag == false) //false이면 선택한 유저를 팔로우 하지 않은 상태//
                        {
                            Log.d("json control", "(팔로잉리스트)" + flag);
                            //팔로잉 안 한 상태에서 팔로우 생성//
                            Log.d("json control", "(팔로잉리스트)팔로잉을 현재 하지 않은 상태이므로 팔로잉을 생성");

                            setFollowing(userSelectId);
                            followingData.followingDataList.get(position).setFlag(!flag);
                            notifyDataSetChanged();

                        }
                        notifyDataSetChanged();
                    }
                });

                return;
            }

            //position -= followingData.followingDataList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }


}
