package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.widget.adapter.FollowerListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

public class FollowerListActivity extends AppCompatActivity {

    FollowerListAdapter mAdapter;
    FollowerData followerData;
    private FamiliarRecyclerView recyclerview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower_list_activity_layout);

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

        followerData = new FollowerData();

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.follower_rv_list);


        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null, false);
        recyclerview.addHeaderView(headerView);
        mAdapter = new FollowerListAdapter(this);

        recyclerview.setAdapter(mAdapter);

        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelectFollower = followerData.followerDataList.get(position).getName().toString();
                Toast.makeText(FollowerListActivity.this, "" + userSelectFollower, Toast.LENGTH_SHORT).show();

                //해당 사람의 마이 페이지로 이동//
            }
        });
        initDummyData();
    }

    private void initDummyData() {
        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
        for (int i = 0; i < 7; i++) {
            FollowerData new_followerData = new FollowerData();
            new_followerData.name = nameList[i];
            followerData.followerDataList.add(new_followerData);
        }

        mAdapter.setFollowerData(followerData);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_follower, menu); //xml로 작성된 메뉴를 팽창//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
//        int item_id = item.getItemId();
//
//        if (item_id == R.id.search_menu_item) {
//            Toast.makeText(FollowerListActivity.this, "검색 화면으로 이동", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
}
