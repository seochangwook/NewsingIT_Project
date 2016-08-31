package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.AlarmData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.AlarmListAdapter;

import java.io.IOException;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlarmListActivity extends AppCompatActivity {

    AlarmListAdapter mAdapter;
    AlarmData alarmData;
    /**
     * Network 자원
     **/
    NetworkManager networkManager;
    private FamiliarRecyclerView recyclerview;
    private Callback requestalarmlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);
        }
    };

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
                String userSelect = alarmData.alarmDataList.get(position).getName().toString();
                String alarmCase = alarmData.alarmDataList.get(position).getCase().toString();

                if (alarmCase == "1") { //1 - 스크랩 좋아요
                    //당신의 게시물을 좋아합니다 -> 나의 해당 스크랩으로 이동//
                    Toast.makeText(AlarmListActivity.this, "나의 마이 페이지로 이동합니다", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AlarmListActivity.this, MainActivity.class);

                    intent.putExtra("KEY_FRAGMENT_NUMBER", "1");

                    setResult(RESULT_OK, intent);

                    finish();
                } else if (alarmCase == "2") {   //2 - 나를 팔로우
                    //당신을 팔로우 하였습니다 -> 나의 마이 페이지로 이동//
                    Toast.makeText(AlarmListActivity.this, "나의 마이 페이지로 이동합니다", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AlarmListActivity.this, MainActivity.class);

                    intent.putExtra("KEY_FRAGMENT_NUMBER", "1");

                    setResult(RESULT_OK, intent);

                    finish();

                } else if (alarmCase == "3") {  //3 - 새 스크랩
                    //xx가 새 스크랩을 하였습니다 -> 그 사람의 마이 페이지로 이동 or 그 사람의 새로운 스크랩으로 바로 이동//
                    Intent intent = new Intent(AlarmListActivity.this, UserInfoActivity.class);
                    //필요한 값을 전달한다.//
                    intent.putExtra("USER_NAME", userSelect);
                    startActivity(intent);
                }


            }
        });

        //initDummyData();

        //네트워크로 부터 데이터를 얻어온다.//
        get_Alarm_Data();
    }

    public void get_Alarm_Data() {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com");
        builder.addPathSegment("notifications");

        builder.addQueryParameter("page", "1");
        builder.addQueryParameter("count", "20");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestalarmlistcallback);
    }

    private void initDummyData() {
        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
        String contentList[] = {"님이 당신의 게시물을 좋아합니다", "님이 당신의 게시물을 좋아합니다", "님이 당신의 게시물을 좋아합니다"
                , "님이 당신의 게시물을 좋아합니다", "님이 당신을 팔로우 하였습니다", "님이 새 스크랩을 하였습니다"
                , "님이 당신의 게시물을 좋아합니다"};
        String dateList[] = {"1시간 전", "1시간 전", "2시간 전", "2시간 전", "4시간 전", "6시간 전", "9시간 전"};
        String acaseList[] = {"1", "1", "1", "1", "2", "3", "1"};

        for (int i = 0; i < 7; i++) {
            AlarmData new_alarmData = new AlarmData();
            new_alarmData.name = nameList[i];
            new_alarmData.content = contentList[i];
            new_alarmData.date = dateList[i];
            new_alarmData.acase = acaseList[i];

            alarmData.alarmDataList.add(new_alarmData);
        }
        mAdapter.setAlarmDataLIist(alarmData);
    }
}
