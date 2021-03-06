package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.apple.newsingit_project.data.json_data.followerlist.FollowerListRequest;
import com.example.apple.newsingit_project.data.json_data.followerlist.FollowerListRequestResults;
import com.example.apple.newsingit_project.data.view_data.FollowerData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.FollowerListAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FollowerListActivity extends AppCompatActivity {
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_FOLLOW_FLAG = "USER_FOLLOW_FLAG";
    private static final int LOAD_MORE_TAG = 4;

    static int pageCount = 1;

    FollowerListAdapter mAdapter;
    FollowerData followerData;
    SearchView search_edit;
    NetworkManager networkManager;

    FamiliarRefreshRecyclerView familiarRefreshRecyclerView;
    FontManager fontManager;
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

            //검색에 대한 결과를 출력.//
            if (followerListRequest.getResults().length == 0) //검색결과가 없는 경우//
            {
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FollowerListActivity.this);
                            alertDialog.setTitle("Newsing Search")
                                    .setMessage("검색결과가 존재하지 않습니다.")
                                    .setCancelable(false)
                                    .setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //yes

                                                }
                                            });

                            AlertDialog alert = alertDialog.create();
                            alert.show();
                        }
                    });
                }
            } else if (followerListRequest.getResults().length > 0) {
                setData(followerListRequest.getResults(), followerListRequest.getResults().length);
            }
        }
    };

    private void getFollowerListNetworkData() {

        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addQueryParameter("direction", "from")
                .addQueryParameter("word", "") //전체검색//
                .addQueryParameter("page", "" + pageCount)
                .addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowerListCallback);

        hidepDialog();
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

                        newFollowerData.setId("" + followerList.get(i).getId());
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

        fontManager = new FontManager(this);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 화면을 나갈때 응답을 준다.//
                setResult(RESULT_OK);

                finish();
            }
        });


        pageCount = 1;

        followerData = new FollowerData();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        familiarRefreshRecyclerView = (FamiliarRefreshRecyclerView) findViewById(R.id.follower_rv_list);
        familiarRefreshRecyclerView.setId(android.R.id.list);
        familiarRefreshRecyclerView.setLoadMoreView(new LoadMoreView(this, LOAD_MORE_TAG));
        familiarRefreshRecyclerView.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        familiarRefreshRecyclerView.setLoadMoreEnabled(true);

        familiarRefreshRecyclerView.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        familiarRefreshRecyclerView.pullRefreshComplete();

                        initFollowerList();

                        getFollowerListNetworkData();

                    }
                }, 1000);
            }
        });

        familiarRefreshRecyclerView.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        familiarRefreshRecyclerView.loadMoreComplete();

                        pageCount += 1;

                        getFollowerListNetworkData();
                    }
                }, 1000);

            }
        });


        recyclerview = familiarRefreshRecyclerView.getFamiliarRecyclerView();
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        /** HeaderView 설정 **/
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null);

        search_edit = (SearchView) headerView.findViewById(R.id.search_my_follow);

        //search view의 text color를 바꿔 줌//
        int id = search_edit.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search_edit.findViewById(id);
        textView.setTextColor(Color.WHITE);

        /** EmptyView 설정 **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_follower_emptyview, null);

        /** EmptyView 위젯 **/
        TextView empty_textview = (TextView) emptyview.findViewById(R.id.empty_msg_follower);
        empty_textview.setTypeface(fontManager.getTypefaceRegularInstance());

        recyclerview.setEmptyView(emptyview, true);
        recyclerview.addHeaderView(headerView, true);

        search_edit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                pageCount = 1;

                initFollowerList();
                //검색어를 가지고 검색을 한다.//
                search_follower_list(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //돋보기 모양의 검색 아이콘 클릭 시//
        search_edit.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //검색 창의 배경을 빈 배경으로 바꾼다.//
                search_edit.setBackgroundResource(R.mipmap.searchbar_on);
            }
        });

        //x 아이콘 클릭 시//
        search_edit.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() { //검색 창의 배경을 힌트가 있는 배경으로 바꾼다.//
                search_edit.setBackgroundResource(R.mipmap.searchbar_off);
                return false;
            }
        });

        mAdapter = new FollowerListAdapter(this);

        recyclerview.setAdapter(mAdapter);


        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String selectName = followerData.followerDataList.get(position).getName().toString();
                String selectId = followerData.followerDataList.get(position).getId();
                String serlect_flag = "" + followerData.followerDataList.get(position).getFlag();

                //해당 사람의 마이 페이지로 이동//
                Intent intent = new Intent(FollowerListActivity.this, UserInfoActivity.class);

                intent.putExtra(USER_NAME, selectName);
                intent.putExtra(USER_ID, "" + selectId);
                intent.putExtra(USER_FOLLOW_FLAG, serlect_flag);

                startActivity(intent);
            }
        });

        getFollowerListNetworkData(); //초기 화면에 들어올 시는 전체 리스트를 검색.//
    }

    private void initFollowerList() {
        followerData.followerDataList.clear();
        mAdapter.initFollowerData(followerData);
    }

    public void search_follower_list(String query) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addQueryParameter("direction", "from")
                .addQueryParameter("word", query) //조건검색//
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowerListCallback);
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}
