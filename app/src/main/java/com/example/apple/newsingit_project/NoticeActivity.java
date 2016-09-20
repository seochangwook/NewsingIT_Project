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
        String[] groupList = {"안녕하세요, 뉴스잉IT 입니다.", "뉴스잉IT의 뉴스 출처 안내", "뉴스잉 사랑해주세요"};

        String[] childList = {"안녕하세요, 뉴스잉IT 입니다.", "안녕하세요, 뉴스잉IT 입니다. 뉴스 출처를 알려드립니다.", "만드느라 힘들었음"};


        String[] groupDateList = {"09.20", "09.20", "09.20"};

        for (int i = 0; i < groupList.length; i++) {
            NoticeChild new_child = new NoticeChild(childList[i]);
            NoticeGroup new_group = new NoticeGroup(groupList[i], groupDateList[i], new_child);
            group.add(new_group);
        }

        mAdapter.setNoticeData(group);
    }

}
