package com.example.apple.newsingit_project.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.json_data.keywordlist.KeywordListRequest;
import com.example.apple.newsingit_project.data.json_data.keywordlist.KeywordListRequestResults;
import com.example.apple.newsingit_project.data.view_data.KeywordData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.KeywordListAdapter;
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

public class KeywordListActivity extends Activity {

    //전달할 값의 키//
    private static final String KEY_KEYWORD = "KEY_KEYWORD";

    KeywordListAdapter mAdapter;
    KeywordData keywordData;
    /**
     * Network 관련 변수
     **/
    NetworkManager manager;
    private FamiliarRecyclerView recyclerview;
    private ProgressDialog pDialog;
    private Callback requestkeywordlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            //메인 UI에서 작업하기 위해서 runOnUiThread설정//
            runOnUiThread(new Runnable() {
                public void run() {
                    hidepDialog();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            //자동 파싱도구인 GSON을 가지고 파싱//
            Gson gson = new Gson();

            KeywordListRequest keywordListRequest = gson.fromJson(response_data, KeywordListRequest.class);

            //데이터 초기화부분.//
            set_Data(keywordListRequest.getResults(), keywordListRequest.getResults().length);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword_list_activity_layout);

        keywordData = new KeywordData();

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.rv_list);

        /** EmptyView 설정 **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_keywordlist_emptyview, null);

        recyclerview.setEmptyView(emptyview, true);

        mAdapter = new KeywordListAdapter(this);
        recyclerview.setAdapter(mAdapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelectKeyword = keywordData.keywordDataList.get(position).getKeyword().toString();

                Toast.makeText(KeywordListActivity.this, "[" + userSelectKeyword + "]로 리스트 이동", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();

                intent.putExtra(KEY_KEYWORD, userSelectKeyword);

                setResult(RESULT_OK, intent);

                finish();
            }
        });

        //initDummyData();
        get_Keyword_Data();
    }

    private void initDummyData()
    {
        String keywordDummyList[] = {"갤럭시 7", "iot", "소프트뱅크", "드론", "VR", "테슬라", "핀테크", "안드로이드", "라즈베리파이", "아이폰 7"};

        for (int i = 0; i < 10; i++) {
            KeywordData new_keywordData = new KeywordData();
            new_keywordData.keyword = keywordDummyList[i];
            keywordData.keywordDataList.add(new_keywordData);
        }

        mAdapter.setKeywordDataList(keywordData);
    }

    public void get_Keyword_Data() {
        //네트워크로 부터 데이터를 가져온다.//
        showpDialog();

        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의//
        builder.host(getResources().getString(R.string.server_domain)); //호스트를 설정.//
        builder.addPathSegment("keywords");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(KeywordListActivity.this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestkeywordlistcallback);
    }

    public void set_Data(final KeywordListRequestResults keywordListRequestResults[], final int keywordrequest_size) {
        //메인 UI에서 작업하기 위해서 runOnUiThread설정//
        runOnUiThread(new Runnable() {
            public void run() {
                List<KeywordListRequestResults> items = new ArrayList<>();

                //넘어온 배열에 정보를 그대로 이동.(우선적으로 리스트에 전보 이동)//
                items.addAll(Arrays.asList(keywordListRequestResults));

                //해당 목적에 맞는 배열로 실제 ui에 나타낼 데이터 셋팅//
                for (int i = 0; i < keywordrequest_size; i++) {
                    KeywordData new_keyworddata = new KeywordData();

                    new_keyworddata.setKeyword(items.get(i).getKeyword());

                    keywordData.keywordDataList.add(new_keyworddata);
                }

                mAdapter.setKeywordDataList(keywordData);

                hidepDialog();
            }
        });
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
