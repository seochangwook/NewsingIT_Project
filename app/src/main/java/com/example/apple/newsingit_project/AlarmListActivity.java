package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.AlarmData;
import com.example.apple.newsingit_project.widget.adapter.AlarmListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

public class AlarmListActivity extends AppCompatActivity {

    AlarmListAdapter mAdapter;
    AlarmData alarmData;
    private FamiliarRecyclerView recyclerview;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_activity_layout);

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

        alarmData = new AlarmData();

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.alarm_rv_list);

        mAdapter = new AlarmListAdapter(this);
        recyclerview.setAdapter(mAdapter);

        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelectKeyword = alarmData.alarmDataList.get(position).getName().toString();
                Toast.makeText(AlarmListActivity.this, "" + userSelectKeyword, Toast.LENGTH_SHORT).show();
            }
        });
        initDummyData();
    }

    private void initDummyData() {
        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
        String contentList[] = {"님이 당신의 게시물을 좋아합니다", "님이 당신의 게시물을 좋아합니다", "님이 당신의 게시물을 좋아합니다"
                , "님이 당신의 게시물을 좋아합니다", "님이 당신을 팔로우 하였습니다", "님이 새 스크랩을 하였습니다"
                , "님이 당신의 게시물을 좋아합니다"};
        String dateList[] = {"1시간 전", "1시간 전", "2시간 전", "2시간 전", "4시간 전", "6시간 전", "9시간 전"};

        for (int i = 0; i < 7; i++) {
            AlarmData new_alarmData = new AlarmData();
            new_alarmData.name = nameList[i];
            new_alarmData.content = contentList[i];
            new_alarmData.date = dateList[i];

            alarmData.alarmDataList.add(new_alarmData);
        }
        mAdapter.setAlarmDataLIist(alarmData);
    }

}
