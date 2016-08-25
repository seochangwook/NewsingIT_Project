package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.widget.adapter.FollowingListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

public class FollowingListActivity extends AppCompatActivity {

    FollowingListAdapter mAdapter;
    FollowingData followingData;
    private FamiliarRecyclerView recyclerview;

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

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.following_rv_list);


        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null, false);
        recyclerview.addHeaderView(headerView);
        mAdapter = new FollowingListAdapter(this);

        recyclerview.setAdapter(mAdapter);


        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelectFollower = followingData.followingDataList.get(position).getName().toString();
                Toast.makeText(FollowingListActivity.this, "" + userSelectFollower, Toast.LENGTH_SHORT).show();

                //해당 사람의 마이 페이지로 이동//
            }
        });
        initDummyData();
    }

    private void initDummyData() {

        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
        for (int i = 0; i < 7; i++) {
            FollowingData new_followingData = new FollowingData();
            new_followingData.name = nameList[i];
            followingData.followingDataList.add(new_followingData);
        }

        mAdapter.setFollowingData(followingData);
    }
}
