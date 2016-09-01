package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.followinglist.FollowingListRequest;
import com.example.apple.newsingit_project.data.json_data.followinglist.FollowingListRequestResults;
import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.FollowingListAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FollowingListActivity extends AppCompatActivity {

    FollowingListAdapter mAdapter;
    FollowingData followingData;
    NetworkManager networkManager;
    private FamiliarRecyclerView recyclerview;
    private ProgressDialog pDialog;
    private Callback requestFollowingListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            Gson gson = new Gson();

            FollowingListRequest followingListRequest = gson.fromJson(responseData, FollowingListRequest.class);

            setData(followingListRequest.getResults(), followingListRequest.getResults().length);
        }
    };


    private void getFollowingListNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com")
                .addPathSegment("follows")
                .addQueryParameter("direction", "to")
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "10");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowingListCallback);

        hidepDialog();
    }

    private void setData(final FollowingListRequestResults[] results, final int size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<FollowingListRequestResults> followingList = new ArrayList<>();
                    followingList.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        FollowingData newFollowingData = new FollowingData();
                        newFollowingData.setId(followingList.get(i).getId());
                        newFollowingData.setProfileUrl(followingList.get(i).getPf_url());
                        newFollowingData.setName(followingList.get(i).getName());
                        newFollowingData.setAboutMe(followingList.get(i).getAboutme());
                        newFollowingData.setFlag(followingList.get(i).getFlag());

                        followingData.followingDataList.add(newFollowingData);
                    }
                    mAdapter.setFollowingData(followingData);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_list_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        followingData = new FollowingData();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        recyclerview = (FamiliarRecyclerView) findViewById(R.id.following_rv_list);


        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null, false);
        recyclerview.addHeaderView(headerView);
        mAdapter = new FollowingListAdapter(this);

        recyclerview.setAdapter(mAdapter);


        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String selectName = followingData.followingDataList.get(position).getName().toString();
                int selectId = followingData.followingDataList.get(position).getId();
                Toast.makeText(FollowingListActivity.this, "" + selectName, Toast.LENGTH_SHORT).show();

                //해당 사람의 마이 페이지로 이동//
                Intent intent = new Intent(FollowingListActivity.this, UserInfoActivity.class);
                intent.putExtra("USER_NAME", selectName);
                intent.putExtra("USER_ID", "" + selectId);
                startActivity(intent);
            }
        });

        getFollowingListNetworkData();

        //initDummyData();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


//
//    private void initDummyData() {
//
//        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
//        String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
//                ,"만두만두", "=v="};
//        for (int i = 0; i < 7; i++) {
//            FollowingData new_followingData = new FollowingData();
//            new_followingData.name = nameList[i];
//            new_followingData.aboutMe = introList[i];
//            followingData.followingDataList.add(new_followingData);
//        }
//
//        mAdapter.setFollowingData(followingData);
//    }
}
