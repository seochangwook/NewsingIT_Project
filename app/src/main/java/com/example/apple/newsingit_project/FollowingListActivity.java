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
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.followinglist.FollowingListRequest;
import com.example.apple.newsingit_project.data.json_data.followinglist.FollowingListRequestResults;
import com.example.apple.newsingit_project.data.view_data.FollowingData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.FollowingListAdapter;
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

public class FollowingListActivity extends AppCompatActivity {
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_FOLLOW_FLAG = "USER_FOLLOW_FLAG";
    private static final int LOAD_MORE_TAG = 4;
    static int pageCount = 1;

    FollowingListAdapter mAdapter;
    FollowingData followingData;

    SearchView searchView;

    NetworkManager networkManager;

    FamiliarRefreshRecyclerView familiarRefreshRecyclerView;
    FontManager fontManager;
    private FamiliarRecyclerView recyclerview;
    private ProgressDialog pDialog;
    private Callback requestFollowingListCallback = new Callback() {
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

            FollowingListRequest followingListRequest = gson.fromJson(responseData, FollowingListRequest.class);

            //검색에 대한 결과를 출력.//
            if (followingListRequest.getResults().length == 0) //검색결과가 없는 경우//
            {
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FollowingListActivity.this);
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
            } else if (followingListRequest.getResults().length > 0) {
                //초기화//

                setData(followingListRequest.getResults(), followingListRequest.getResults().length);
            }
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
                .addQueryParameter("direction", "to")
                .addQueryParameter("word", "") //전체검색//
                .addQueryParameter("page", "" + pageCount)
                .addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowingListCallback);

        hidepDialog();
    }

    private void setData(final FollowingListRequestResults[] results, final int size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<FollowingListRequestResults> followingList = new ArrayList<>();
                    followingList.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        FollowingData newFollowingData = new FollowingData();

                        newFollowingData.setId("" + followingList.get(i).getId());
                        newFollowingData.setProfileUrl(followingList.get(i).getPf_url());
                        newFollowingData.setName(followingList.get(i).getName());
                        newFollowingData.setAboutMe(followingList.get(i).getAboutme());
                        newFollowingData.setFlag(followingList.get(i).getFlag());

                        followingData.followingDataList.add(newFollowingData);
                    }

                    mAdapter.setFollowingData(followingData);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_list_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);

                finish();
            }
        });

        pageCount = 1;

        followingData = new FollowingData();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        familiarRefreshRecyclerView = (FamiliarRefreshRecyclerView) findViewById(R.id.following_rv_list);
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

                        initFollowingList();

                        getFollowingListNetworkData();

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

                        getFollowingListNetworkData();
                    }
                }, 1000);

            }
        });

        recyclerview = familiarRefreshRecyclerView.getFamiliarRecyclerView();
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_follow_header, null, false);

        //SearchVIew 검색 창//
        searchView = (SearchView) headerView.findViewById(R.id.search_my_follow);

        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                pageCount = 1;

                initFollowingList();

                search_following_list(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //돋보기 모양의 검색 아이콘 클릭 시//
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //검색 창의 배경을 빈 배경으로 바꾼다.//
                searchView.setBackgroundResource(R.mipmap.searchbar_on);
            }
        });

        //x 아이콘 클릭 시//
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() { //검색 창의 배경을 힌트가 있는 배경으로 바꾼다.//
                searchView.setBackgroundResource(R.mipmap.searchbar_off);
                return false;
            }
        });


        /** EmptyView 설정 **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_following_emptyview, null);

        /** EmptyView 위젯 **/
        TextView empty_textview = (TextView) emptyview.findViewById(R.id.empty_msg_following);
        empty_textview.setTypeface(fontManager.getTypefaceRegularInstance());

        recyclerview.setEmptyView(emptyview, true);

        recyclerview.addHeaderView(headerView);

        mAdapter = new FollowingListAdapter(this);

        recyclerview.setAdapter(mAdapter);

        //옵저버 패턴으로 어댑터의 정보를 액티비티에서 받는다.//
        mAdapter.setOnFollowingButtonClick(new FollowingListAdapter.OnFollowingButtonClick() {
            @Override
            public void onFollowingButtonClick() {
                Log.d("json control:", "observer call");

                //어댑터에서 데이터 변경작업이 있을 시 다시 팔로잉 리스트를 초기화한다.//
                initFollowingList();

                getFollowingListNetworkData(); //초기화면은 전체검색 화면//

                Log.d("json control:", "following list update");
            }
        });

        //리사이클러 뷰 각 항목 클릭//
        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String selectName = followingData.followingDataList.get(position).getName().toString();
                String selectId = followingData.followingDataList.get(position).getId();
                String serlect_flag = "" + followingData.followingDataList.get(position).getFlag();

                Toast.makeText(FollowingListActivity.this, "" + selectName, Toast.LENGTH_SHORT).show();

                //해당 사람의 마이 페이지로 이동//
                Intent intent = new Intent(FollowingListActivity.this, UserInfoActivity.class);
                intent.putExtra(USER_NAME, selectName);
                intent.putExtra(USER_ID, "" + selectId);
                intent.putExtra(USER_FOLLOW_FLAG, serlect_flag);

                startActivity(intent);
            }
        });

        getFollowingListNetworkData(); //초기화면은 전체검색 화면//
    }

    private void initFollowingList() {
        followingData.followingDataList.clear();
        mAdapter.initFollowingData(followingData);
    }

    public void search_following_list(String query) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addQueryParameter("direction", "to")
                .addQueryParameter("word", query) //조건검색//
                .addQueryParameter("page", "" + pageCount)
                .addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestFollowingListCallback);
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