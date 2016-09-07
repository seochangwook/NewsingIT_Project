package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.followerlist.FollowerListRequest;
import com.example.apple.newsingit_project.data.json_data.followerlist.FollowerListRequestResults;
import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.FollowerListAdapter;
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

public class FollowerListActivity extends AppCompatActivity {

    FollowerListAdapter mAdapter;
    FollowerData followerData;
    SearchView search_edit;
    NetworkManager networkManager;
    private FamiliarRecyclerView recyclerview;
    private ProgressDialog pDialog;

    private Callback requestFollowerListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            Gson gson = new Gson();

            FollowerListRequest followerListRequest = gson.fromJson(responseData, FollowerListRequest.class);

            setData(followerListRequest.getResults(), followerListRequest.getResults().length);
        }
    };

    private void getFollowingListNetworkData() {

        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addQueryParameter("direction", "from")
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "10");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowerListCallback);

        hidepDialog();
    }

    private void searchFollowingNetworkData(String query) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.server_domain))
                .addPathSegment("follows")
                .addQueryParameter("direction", "from");
        //검색~query 전달~~//


        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowerListCallback);
    }



    private void setData(final FollowerListRequestResults[] results, final int size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<FollowerListRequestResults> followerList = new ArrayList<>();
                    followerList.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        FollowerData newFollowerData = new FollowerData();
                        newFollowerData.setId(followerList.get(i).getId());
                        newFollowerData.setProfileUrl(followerList.get(i).getPf_url());
                        newFollowerData.setAboutMe(followerList.get(i).getAboutme());
                        newFollowerData.setName(followerList.get(i).getName());
                        newFollowerData.setFlag(followerList.get(i).getFlag());

                        followerData.followerDataList.add(newFollowerData);
                    }
                    mAdapter.setFollowerData(followerData);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower_list_activity_layout);

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

        followerData = new FollowerData();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.follower_rv_list);

        /** HeaderView 설정 **/
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null);

        search_edit = (SearchView) headerView.findViewById(R.id.search_my_follow);

        /** EmptyView 설정 **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_follower_emptyview, null);

        recyclerview.setEmptyView(emptyview, true);
        recyclerview.addHeaderView(headerView, true);

        search_edit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FollowerListActivity.this, "onQueryTextSubmit", Toast.LENGTH_SHORT).show();

                //searchFollowingNetworkData(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(FollowerListActivity.this, "onQueryTextChange", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//
//        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                Log.d("input text", textView.getText().toString());
//                return true;
//            }
//        });

        mAdapter = new FollowerListAdapter(this);

        recyclerview.setAdapter(mAdapter);

        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String selectName = followerData.followerDataList.get(position).getName().toString();
                int selectId = followerData.followerDataList.get(position).getId();

                Toast.makeText(FollowerListActivity.this, "" + selectName, Toast.LENGTH_SHORT).show();

                //해당 사람의 마이 페이지로 이동//
                Intent intent = new Intent(FollowerListActivity.this, UserInfoActivity.class);
                intent.putExtra("USER_NAME", selectName);
                intent.putExtra("USER_ID", "" + selectId);
                startActivity(intent);
            }
        });

        getFollowingListNetworkData();

        //initDummyData();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }


//    private void initDummyData() {
//        String nameList[] = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
//        String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
//                ,"만두만두", "=v="};
//        for (int i = 0; i < 7; i++) {
//            FollowerData new_followerData = new FollowerData();
//            new_followerData.name = nameList[i];
//            new_followerData.aboutMe = introList[i];
//            followerData.followerDataList.add(new_followerData);
//        }
//
//        mAdapter.setFollowerData(followerData);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_follower, menu); //xml로 작성된 메뉴를 팽창//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
//        int item_id = item.getItemId();
//
//        if (item_id == R.id.search_menu_item) {
//            Toast.makeText(FollowerListActivity.this, "검색 화면으로 이동", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
}
