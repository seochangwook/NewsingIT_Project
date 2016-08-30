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
        String[] groupList = {"알림 모아보는 새소식 기능 등 2.2.2 버전 업데이트 안내", "OS 간 대화내용 백업/복원 등 2.1.3 업데이트 안내"
                , "장문 메시지를 바로 볼 수 있는 텍스트뷰어, 그릅콜 중 친구 초대 기능 등 2.0.9 업데이트 안내", "기록하고 싶은 메시지를 나와의 채팅방으로 보내세요. (2.0.8 ) "
                , "친구 추가없이도 퀵하게, 링크로 대화하는 오픈채팅", "친구그룹 관리부터 백업/복원까지되는 2.0.7 안내 "
                , "2.0.6 업데이트 안내 '더 예쁘고 시원하게 바뀐 말풍선' ", "프로필 사진 편집과 대화 캡쳐 기능이 추가된 2.0.5 버전 업데이트 안내 "};

        String[] childList = {"카카오톡 사용자 여러분, 안녕하세요 또 다시 일 년의 절반을 지나온 6월, 한여름 무더위 대비는 잘 하고 계신가요.\n" +
                "샤워하고 누워 찬바람 쐬며 카톡하기 좋은 계절입니다. (그렇게 할 일 없이 빈둥대면 기분이 조크든요..)\n" +
                "폭염의 계절을 대비한 성공적 방콕, 업데이트된 카카오톡 PC 버전과 함께 하시죠."
                , "카카오톡 사용자 여러분, 안녕하세요. 봄날을 맞아 2.1.3 업데이트 안내 나갑니다."
                , "카카오톡 사용자 여러분, 안녕하세요. 요즘 이애란 선생님의 백세인생 인기가 대단한데요." +
                "2010년 3월생인 귀요미 카카오톡은, 이제 곧 일곱 살이 되는 어린이입니다.(카카오팀은 어른이… 평균 나이는 비밀입니다.)"
                , "안녕하세요. 카카오톡 사용자 여러분. \n겨울의 문턱에서 문득 생각.\n" +
                "여러분들이 가장 원하는 기능이 뭘까. \n" +
                "올해가기 전에 좋아하는걸 장만해드려야지..보일러 놔드리듯 (이런 오래된 사람…)\n" +
                "올 겨울 여러분을  훈훈하게해드릴 2.0.8 버전입니다. "
                , "안녕하세요. 카카오톡 사용자 여러분, \n" +
                "퀵하고 편하게 대화할 수 있는, 오픈채팅 기능을 안내해드립니다. "
                , "친한 친구들은 즐겨찾기 하면 되는데....모임, 회사, 동호회 친구들은 한번에 찾기 힘드셨죠?\n" +
                "친구들을 그룹별로 관리하면 참 좋을 텐데.. (특히 팀장님, 그리고 팀장님, 또 팀장님이랄까.) \n" +
                "그래서, 이때다 싶..어, 나왔습니다.  이제는 친구를 그룹별로 편리하게 관리해보세요."
                , "채팅방의 말풍선이 더 '예쁘고 시~원하게’ 바뀌었습니다~ \n" +
                "연속된 대화의 메시지의 경우, 말풍선을 모아 보여주어 대화의 가독성을 시원하게 높였구요\n" +
                "사진/영상 말풍선 사이즈를 키워, 클릭하지 않아도 이미지를 좀 더 쉽고 빠르게 확인 할 수 있다는 사~실 ! \n" +
                "그 밖에 스티커,링크,아이템 공유 등 더욱 예뻐진 다양한 말풍선을 확인해보세요~ "
                , "■ 프로필 사진 편집 기능이 추가되었습니다.\n" +
                "사진을 편집 없이 프로필에 등록하면 원하는 부분이 설정되지 않았었는데요,\n" +
                "이제 프로필 사진 편집 기능을 이용하면 사진에서 원하는 부분만 프로필로 설정할 수 있습니다.\n" +
                "이용방법\n" +
                "1. 내 미니 프로필> 프로필 편집 메뉴 클릭\n" +
                "2. 프로필 이미지 하단의 '편집' 버튼 클릭\n" +
                "3. 사진 선택\n" +
                "4. 편집 후 '확인'  버튼 클릭"};


        String[] groupDateList = {"08.11","08.12","08.13","08.14","08.15","08.16","08.17","08.18"};

        for(int i=0;i<8;i++){
            NoticeChild new_child = new NoticeChild(childList[i]);
            NoticeGroup new_group = new NoticeGroup(groupList[i], groupDateList[i], new_child);
            group.add(new_group);
        }

        mAdapter.setNoticeData(group);
    }

}
