package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

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

            }
        });

        //그룹을 펼쳤을 때 이벤트//
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });

        //차일드 클릭 이벤트//
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listView, View view, int groupposition, int childposition, long id) {

                String userSelectNotice = group.get(groupposition).childList.get(childposition).name.toString();

                return true;
            }
        });


        initData();
    }

    private void initData() {
        String[] groupList = {"뉴스잉IT의 뉴스 출처 안내", "안녕하세요, 뉴스잉IT 입니다."};

        String[] childList = {"안녕하세요, 뉴스잉IT 입니다. 뉴스잉IT의 뉴스 출처를 알려드립니다.\n\n" +
                "파이낸셜뉴스\n" +
                "다음뉴스\n" +
                "중앙일보\n" +
                "블로터\n" +
                "ETNEWS 통신방송\n" +
                "ETNEWS 인터넷\n" +
                "ETNEWS 오늘의 뉴스\n" +
                "IT WORLD\n" +
                "ITCLE\n" +
                "MOBI INSIDE\n" +
                "이코노믹리뷰\n" +
                "tech holic\n" +
                "더기어\n" +
                "techneedle\n" +
                "IT 조선\n" +
                "노컷뉴스\n" +
                "경향신문",
                "안녕하세요\n" +
                        "\"키워드 기반 뉴스 큐레이팅 서비스\", 뉴스잉IT입니다!\n" +
                        "\n" +
                        "뉴스잉IT는 키워드를 중심으로 한정된 핵심뉴스와 트위터 정보를 제공하고, 스크랩 기능을 통해 뉴스 소비의 새로운 흐름을 제시하고자 합니다.\n" +
                        "\n" +
                        "뉴스잉IT를 시작으로 Movie, Economy 등의 분야로 뻗어 나가려 합니다.\n" +
                        "\n" +
                        "지속적인 업데이트를 통해 발전해 나가는 뉴스잉이 되겠습니다!"
        };


        String[] groupDateList = {"09.22", "09.22"};

        for (int i = 0; i < groupList.length; i++) {
            NoticeChild newChild = new NoticeChild(childList[i]);
            NoticeGroup newGroup = new NoticeGroup(groupList[i], groupDateList[i], newChild);
            group.add(newGroup);
        }

        mAdapter.setNoticeData(group);
    }

}
