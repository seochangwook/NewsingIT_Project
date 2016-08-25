package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.widget.adapter.UserScrapContentAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

public class UserScrapContentListActivity extends AppCompatActivity {
    String folder_name;
    String is_user_my;

    UserScrapContentData userScrapContentData;
    UserScrapContentAdapter mAdapter;
    private FamiliarRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scrap_content_list_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        folder_name = intent.getStringExtra("KEY_FOLDER_NAME");
        is_user_my = intent.getStringExtra("KEY_USER_IDENTIFY_FLAG");

        if (is_user_my.equals("1")) //다른 사람의 스크랩에 들어올 경우.//
        {
            Log.d("who? : ", "other user");
        } else if (is_user_my.equals("0")) //나의 스크랩에 들어올 경우.//
        {
            Log.d("who? : ", "me");
        }

        /** Title명 설정 **/
        setTitle(folder_name + " News");

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //리사이클러 뷰 설정//
        userScrapContentData = new UserScrapContentData();
        recyclerView = (FamiliarRecyclerView) findViewById(R.id.scrap_list_rv_list);

        mAdapter = new UserScrapContentAdapter(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelect = userScrapContentData.userScrapContentDataList.get(position).getContent().toString();
                Toast.makeText(UserScrapContentListActivity.this, "" + userSelect, Toast.LENGTH_SHORT).show();

                //클릭 시 개별 스크랩 콘텐츠로 이동//
                Intent intent = new Intent(UserScrapContentListActivity.this, UserSelectScrapContentActivity.class);

                if (is_user_my.equals("1")) //다른 사람의 스크랩에 들어갔을 경우.//
                {
                    intent.putExtra("KEY_USER_IDENTIFY_FLAG", "1");
                } else if (is_user_my.equals("0")) //나의 스크랩일 경우.//
                {
                    intent.putExtra("KEY_USER_IDENTIFY_FLAG", "0");
                }

                startActivity(intent);
            }
        });

        initDummyData();
    }

    private void initDummyData() {
        String[] userScrapList = {"스크랩 리스트 1", "스크랩 리스트 2", "스크랩 리스트 3"
                , "스크랩 리스트 4", "스크랩 리스트 5", "스크랩 리스트 6"};
        for (int i = 0; i < 6; i++) {
            UserScrapContentData new_userScrapData = new UserScrapContentData();
            new_userScrapData.content = userScrapList[i];
            userScrapContentData.userScrapContentDataList.add(new_userScrapData);
        }
        mAdapter.setUserScrapContentData(userScrapContentData, is_user_my); //구분 플래그를 같이 넣어준다.//
    }

}
