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
        String[] titleList = {"갤럭시 7", "iot", "소프트뱅크", "드론", "VR", "테슬라"};

        String[] contentList = {"갤럭시 7 너무 사고 싶당 홍채인식과 물 속에서 터치 펜 기능이 가능하다 그런데 누가 물속에서 폰을 할까"
                , "iot 요즘 대세라는데... 사물 인터넷(Internet of Things, 약어로 IoT)은 각종 사물에 센서와 통신 기능을 내장하여 인터넷에 연결하는 기술을 의미한다. 여기서 사물이란 가전제품, 모바일 장비, 웨어러블 컴퓨터 등 다양한 임베디드 시스템이 된다. "
                , "소프트뱅크 주식회사(영어: SoftBank Corporation, 일본어: ソフトバンク株式会社)는 1981년 9월 3일 일본 도쿄에서 설립된 고속 인터넷, 전자 상거래, 파이낸스, 기술 관련 분야에서 활동하는 일본의 기업 겸임 일본의 이동통신사이다. 사장은 한국계 일본인인 손 마사요시(손정의)이다."
                , "무인 항공기(無人航空機, 영어: unmanned aerial vehicle, UAV) 또는 무인 항공기의 다른 이름으로 '벌이 윙윙거린다'는 것에서 \"드론\"(drone)이라고도 불리기도 한다. ta1 드론(영어: drone)은 조종사를 탑승하지 않고 지정된 임무를 수행할 수 있도록 제작한 비행체이다. 기체에 사람이 타지 않은 것으로 지상에는 원격 조종하는 조종사가 존재하고 있다는 점을 강조해 Uninhabited Aerial(Air) Vehicle의 약어로 지칭하는 경우도 있다."
                , "수많은 미디어 기업들이 가상 현실 시장에 접근하고 있는 가운데 디즈니도 가상 현실 세계에 첫 발을 디뎠다. 디즈니는 현지 시각으로 16일, 디즈니 영화 장면들을 가상 현실 플랫폼에서 볼 수 있는 디즈니 무비 VR(Disney Movie VR)을 공개했다."
                , "테슬라는 다음을 가리키는 말이다. 니콜라 테슬라는 미국의 과학자이다. SI 단위계 에서 테슬라는 자기장에 대한 유도단위이다. 엔비디아 테슬라는 엔비디아의 GPU이다. 테슬라 모터스"};

        String[] dateList = {"16.08.29", "16.08.28", "16.08.27"
                , "16.08.26", "16.08.25", "16.08.24"};
        int[] likeList = {10, 10, 3, 4, 125, 23};
        boolean[] likeFlagList = {false, false, false, false, false, false};

        for (int i = 0; i < 6; i++) {
            UserScrapContentData new_userScrapData = new UserScrapContentData();
            new_userScrapData.setTitle(titleList[i]);
            new_userScrapData.setContent(contentList[i]);
            new_userScrapData.setDate(dateList[i]);
            new_userScrapData.setLike(likeList[i]);
            new_userScrapData.setLikeFlag(likeFlagList[i]);

            userScrapContentData.userScrapContentDataList.add(new_userScrapData);
        }
        mAdapter.setUserScrapContentData(userScrapContentData, is_user_my); //구분 플래그를 같이 넣어준다.//
    }

}
