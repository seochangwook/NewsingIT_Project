package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.NoticeChild;
import com.example.apple.newsingit_project.data.view_data.NoticeGroup;
import com.example.apple.newsingit_project.widget.adapter.NoticeAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    NoticeAdapter mAdapter;
    List<NoticeGroup> group = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_activity_layout);

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

        mAdapter = new NoticeAdapter(group,this);

        expandableListView = (ExpandableListView)findViewById(R.id.notice_ex_list);
        expandableListView.setAdapter(mAdapter);

        //expandablelistview의 group indicator를 cusotom해주기 위해 기본 indicator를 제거한다//
        expandableListView.setGroupIndicator(null);

        //그룹을 닫았을 때 이벤트//
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                Toast.makeText(NoticeActivity.this, "그룹 닫힘 " + i, Toast.LENGTH_SHORT).show();
            }
        });

        //그룹을 펼쳤을 때 이벤트//
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                Toast.makeText(NoticeActivity.this, "그룹 열림 " + i, Toast.LENGTH_SHORT).show();

            }
        });

        //차일드 클릭 이벤트//
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listView, View view, int groupposition, int childposition, long id) {

                String userSelectNotice = group.get(groupposition).childList.get(childposition).name.toString();

                Toast.makeText(NoticeActivity.this, "child click : " + userSelectNotice ,Toast.LENGTH_SHORT ).show();

                return true;
            }
        });


        initData();
    }

    private void initData() {
        String[] childList = {"내용1", "내용2", "내용3", "내용4", "내용5", "내용6", "내용7", "내용8"};
        String[] groupList = {"공지사항1","공지사항2","공지사항3","공지사항4","공지사항5","공지사항6","공지사항7","공지사항8"};
        String[] groupDateList = {"08.11","08.12","08.13","08.14","08.15","08.16","08.17","08.18"};

        for(int i=0;i<8;i++){
            NoticeChild new_child = new NoticeChild(childList[i]);
            NoticeGroup new_group = new NoticeGroup(groupList[i], groupDateList[i], new_child);
            group.add(new_group);
        }

        mAdapter.setNoticeData(group);
    }

}
