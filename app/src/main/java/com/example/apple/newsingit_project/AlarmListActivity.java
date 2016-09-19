package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.apple.newsingit_project.data.json_data.alarmlist.AlarmListRequest;
import com.example.apple.newsingit_project.data.json_data.alarmlist.AlarmListRequestResults;
import com.example.apple.newsingit_project.data.view_data.AlarmData;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.AlarmListAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlarmListActivity extends AppCompatActivity {
    private static final String SCRAP_ID = "SCRAP_ID";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";
    private static final String USER_ID = "USER_ID";

    AlarmListAdapter mAdapter;
    AlarmData alarmData;
    /**
     * Network 자원
     **/
    NetworkManager networkManager;

    private FamiliarRecyclerView recyclerview;

    private ProgressDialog pDialog;

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

            Gson gson = new Gson();

            AlarmListRequest alarmListRequest = gson.fromJson(response_data, AlarmListRequest.class);

            set_Alarm_Data(alarmListRequest.getResults(), alarmListRequest.getResults().length);
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        alarmData = new AlarmData();

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.alarm_rv_list);

        /** EmptyView 설정 **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_alarmlist_emptyview, null);

        recyclerview.setEmptyView(emptyview, true);

        mAdapter = new AlarmListAdapter(this);
        recyclerview.setAdapter(mAdapter);


        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                //필요한 데이터는 알람경우와 알람데이터이다.//
                int alarm_type = alarmData.alarmDataList.get(position).get_alarm_message_type();
                String alarm_data = "" + alarmData.alarmDataList.get(position).get_alarm_data();

                /** alarm data
                 * type == 1 (팔로잉 새 스크랩) -> 해당 스크랩 화면으로 이동(is_me는 user)
                 * - scrap_id
                 * type == 2 (내 스크랩 좋아요) -> 해당 스크랩 화면으로 이동(is_me는 my)
                 * - scrap_id
                 * type == 4 (팔로우) -> 해당 유저의 마이 페이지로 이동
                 * - user_id
                 */

                if (alarm_type == 1) {
                    //1 - 팔로잉 새 스크랩 (해당 스크랩 화면으로 이동)//
                    Intent intent = new Intent(AlarmListActivity.this, UserSelectScrapContentActivity.class);

                    //필요한 정보를 전달(스크랩 id, 현재 사용자 유무)
                    intent.putExtra(KEY_USER_IDENTIFY_FLAG, "1"); //팔로잉의 새 스크랩은 다른 사람의 경우가 된다.//
                    intent.putExtra(SCRAP_ID, alarm_data);

                    Log.d("json control", "scrap id:" + alarm_data);

                    startActivity(intent);
                } else if (alarm_type == 2) {
                    //2 - 내 스크랩 좋아요(해당 스크랩 화면으로 이동)//
                    Intent intent = new Intent(AlarmListActivity.this, UserSelectScrapContentActivity.class);

                    //필요한 정보를 전달(스크랩 id, 현재 사용자 유무)
                    intent.putExtra(KEY_USER_IDENTIFY_FLAG, "0"); //내 스크랩의 좋아요는 나의 경우가 된다.//
                    intent.putExtra(SCRAP_ID, alarm_data);

                    Log.d("json control", "scrap id:" + alarm_data);

                    startActivity(intent);
                } else if (alarm_type == 3) {
                    //3 - 팔로우(그 사람의 마이페이지로 이동//
                    Intent intent = new Intent(AlarmListActivity.this, UserInfoActivity.class);

                    intent.putExtra(USER_ID, alarm_data);

                    Log.d("json control", "user id:" + alarm_data);

                    startActivity(intent);
                }
            }
        });

        showpDialog();

        //네트워크로 부터 데이터를 얻어온다.//
        get_Alarm_Data();

        hidepDialog();

        //배지 카운터 설정(기기별 호환문제)//
        Intent i = new Intent("android.intent.action.BADGE_COUNT_UPDATE");

        i.putExtra("badge_count", 0); //다시 배지카운터를 0으로 초기화.//
        i.putExtra("badge_count_package_name", getApplicationContext().getPackageName());
        i.putExtra("badge_count_class_name", SplashActivity.class.getName());

        //변경된 값으로 다시 공유 저장소 값 초기화.//
        PropertyManager.getInstance().setBadge_number(0);

        sendBroadcast(i); //브로드캐스트를 이용.//
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
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
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


    public void set_Alarm_Data(final AlarmListRequestResults alarmListRequestResults[], final int alarm_request_result_size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<AlarmListRequestResults> alarmListRequestResultsList = new ArrayList<>();

                    alarmListRequestResultsList.addAll(Arrays.asList(alarmListRequestResults));

                    for (int i = 0; i < alarm_request_result_size; i++) {
                        AlarmData new_alarmData = new AlarmData();

                        new_alarmData.setAlarm_date(alarmListRequestResultsList.get(i).getDtime());
                        new_alarmData.set_alarm_message_type(alarmListRequestResultsList.get(i).getType());
                        new_alarmData.setAlarm_message(alarmListRequestResultsList.get(i).getMessage());
                        new_alarmData.set_alarm_data(alarmListRequestResultsList.get(i).getData_pk());

                        alarmData.alarmDataList.add(new_alarmData);
                    }

                    mAdapter.setAlarmDataLIist(alarmData);
                }
            });
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}