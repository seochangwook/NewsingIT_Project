package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.scrapcontentlist.ScrapContentListRequestError;
import com.example.apple.newsingit_project.data.json_data.scrapcontentlist.ScrapContentListRequestErrorResults;
import com.example.apple.newsingit_project.data.json_data.tagdetaillist.TagDetailListRequest;
import com.example.apple.newsingit_project.data.json_data.tagdetaillist.TagDetailListRequestResults;
import com.example.apple.newsingit_project.data.view_data.UserScrapContentData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.UserScrapContentAdapter;
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

public class UserScrapContentListActivity extends AppCompatActivity {
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";
    private static final String SCRAP_ID = "SCRAP_ID";
    private static final String KEY_TAGSEARCH_FLAG = "KEY_TAGSEARCH_FLAG";

    static int page_count = 1;

    String folder_name;
    String is_user_my;
    String folder_id;
    String flag_tag = null;

    UserScrapContentData userScrapContentData;
    UserScrapContentAdapter mAdapter;
    NetworkManager networkManager;
    View headerview;
    View emptyview;
    private FamiliarRefreshRecyclerView scrap_recyclerrefreshview;
    private FamiliarRecyclerView scrap_recyclerView;
    private ProgressDialog pDialog;

    private Callback requestUserScrapContetnListCallback = new Callback() {
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

            ScrapContentListRequestError request = gson.fromJson(responseData, ScrapContentListRequestError.class);

            if (request.getResults().length == 0) {
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserScrapContentListActivity.this, "스크랩 항목이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                setData(request.getResults(), request.getResults().length);
            }
        }
    };
    private Callback requestSearchTagListCallback = new Callback() {
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

            TagDetailListRequest tagDetailListRequest = gson.fromJson(responseData, TagDetailListRequest.class);

            //page카운트 증가에 따른 정보 제공//
            if (tagDetailListRequest.getResults().length == 0) {
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserScrapContentListActivity.this, "스크랩 항목이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                            page_count -= 1; //다시 원래의 페이지카운트로 복귀//
                        }
                    });
                }
            } else {
                set_Taglist_Data(tagDetailListRequest.getResults(), tagDetailListRequest.getResults().length);
            }
        }
    };

    private void getScrapContentListNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        Log.d("page count", "" + page_count);

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("scraps")
                .addQueryParameter("category", folder_id) //해당 폴더의 세부 스크랩 정보를 알기 위해서 id값을 넣어준다.//
                //page값은 스와이프 유무에 따라 동적으로 변화된다.//
                .addQueryParameter("page", "" + page_count)
                .addQueryParameter("count", "20"); //count는 20개로 고정.//

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestUserScrapContetnListCallback);

        hidepDialog();
    }

    private void setData(final ScrapContentListRequestErrorResults[] results, final int size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<ScrapContentListRequestErrorResults> scrapContentList = new ArrayList<>();
                    scrapContentList.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        UserScrapContentData newUserScrapCotentData = new UserScrapContentData();

                        //if문으로 locked의 유무에 따라 배열에 넣고를 판단한다.//
                        newUserScrapCotentData.setTitle(scrapContentList.get(i).getTitle());
                        newUserScrapCotentData.setNcTitle(scrapContentList.get(i).getNc_title());
                        newUserScrapCotentData.setNcImgUrl(scrapContentList.get(i).getNc_img_url());
                        newUserScrapCotentData.setNcAuthor(scrapContentList.get(i).getNc_author());
                        newUserScrapCotentData.setNcTime(scrapContentList.get(i).getNc_ntime());
                        newUserScrapCotentData.setLike(scrapContentList.get(i).getFavorite_cnt());
                        newUserScrapCotentData.setLikeFlag(scrapContentList.get(i).getFavorite());
                        newUserScrapCotentData.setLock(scrapContentList.get(i).getLock()); //락 설정을 위해서 필요//
                        newUserScrapCotentData.setId(scrapContentList.get(i).getId());

                        if (is_user_my.equals("1")) //다른 사람 스크랩 목록//
                        {
                            if (newUserScrapCotentData.getLock() == false) {
                                userScrapContentData.userScrapContentDataList.add(newUserScrapCotentData);
                            } else if (newUserScrapCotentData.getLock() == true) {
                                //다른 사람 게시물일 경우 잠금이 있을 시 보여주지 않는다.//
                            }
                        } else if (is_user_my.equals("0")) //내 스크랩 목록//
                        {
                            //잠금여부 상관없이 보여준다.//
                            userScrapContentData.userScrapContentDataList.add(newUserScrapCotentData);
                        }
                    }

                    mAdapter.setUserScrapContentData(userScrapContentData, is_user_my);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scrap_content_list_layout);

        scrap_recyclerrefreshview = (FamiliarRefreshRecyclerView) findViewById(R.id.scrap_list_rv_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        folder_id = intent.getStringExtra(KEY_FOLDER_ID); //태그검색으로 들어올 경우 null이 된다.//
        //사용자 검색으로 들어올 경우 해당 사용자의 스크랩 목록을 봐야 하기에 사용자 폴더의 id값이 넘어온다.//
        folder_name = intent.getStringExtra(KEY_FOLDER_NAME);
        is_user_my = intent.getStringExtra(KEY_USER_IDENTIFY_FLAG);
        flag_tag = intent.getStringExtra(KEY_TAGSEARCH_FLAG); //태그로 검색 시 이 부분에 값이 "TAG"로 할당//

        page_count = 1;

        /** 리사이클뷰 설정 **/
        scrap_recyclerrefreshview.setLoadMoreView(new LoadMoreView(this, 2));
        scrap_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        scrap_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        scrap_recyclerView = scrap_recyclerrefreshview.getFamiliarRecyclerView();
        scrap_recyclerView.setItemAnimator(new DefaultItemAnimator());
        scrap_recyclerView.setHasFixedSize(true);

        /** Header, Empty 설정 **/
        emptyview = getLayoutInflater().inflate(R.layout.view_scraplist_emptyview, null);
        scrap_recyclerView.setEmptyView(emptyview, true);

        headerview = getLayoutInflater().inflate(R.layout.fix_headerview_layout, null);
        scrap_recyclerView.addHeaderView(headerview);

        /** Swape event **/
        scrap_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        /** Tag검색과 일반 카테고리 검색을 비교하여 업테이트 진행. **/

                        scrap_recyclerrefreshview.pullRefreshComplete();
                        scrap_recyclerView.removeHeaderView(headerview);

                        //위에서 새로고침은 page값 증가가 필요없다.//
                        if (flag_tag.equals("TAG")) //태그일 경우 스크랩 검색 조건이 다르므로 설정.//
                        {
                            Log.d("message", "tag load");

                            init_scrap_content_data(); //우선적으로 데이터 초기화.//

                            getTagData(folder_id); //해당 페이지의 개수만큼 다시 로드//
                        } else {
                            Log.d("message", "scrap load");

                            init_scrap_content_data(); //우선적으로 데이터 초기화.//

                            getScrapContentListNetworkData(); //해당 페이지의 개수만큼 다시 로드//
                        }

                    }
                }, 1000);
            }
        });

        scrap_recyclerrefreshview.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "스크랩 정보 불러오기...");

                        scrap_recyclerrefreshview.loadMoreComplete();

                        //아래에서 새로고침 시 현재 리스트에서 page count를 증가(페이지수는 1씩 증가)//
                        page_count += 1;

                        if (flag_tag.equals("TAG")) //태그일 경우 스크랩 검색 조건이 다르므로 설정.//
                        {
                            Log.d("message", "tag load");

                            //init_scrap_content_data(); //우선적으로 데이터 초기화.//

                            //추가검색이기에 데이터를 초기화하지 않는다.//
                            getTagData(folder_id); //해당 페이지의 개수만큼 다시 로드//
                        } else {
                            Log.d("message", "scrap load");

                            //init_scrap_content_data(); //우선적으로 데이터 초기화.//

                            getScrapContentListNetworkData(); //해당 페이지의 개수만큼 다시 로드//
                        }
                    }
                }, 1000);
            }
        });

        if (flag_tag != null) {
            Log.d("intent flag", flag_tag);
        } else if (flag_tag == null) {
            flag_tag = "";
        }

        if (folder_id != null) {
            Log.d("intent data", folder_id);
        }

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

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //리사이클러 뷰 설정//
        userScrapContentData = new UserScrapContentData();

        mAdapter = new UserScrapContentAdapter(this);

        scrap_recyclerView.setAdapter(mAdapter);

        scrap_recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                //클릭 시 개별 스크랩 콘텐츠로 이동//
                Intent intent = new Intent(UserScrapContentListActivity.this, UserSelectScrapContentActivity.class);

                if (is_user_my.equals("1")) //다른 사람의 스크랩에 들어갔을 경우.//
                {
                    intent.putExtra("KEY_USER_IDENTIFY_FLAG", "1");
                } else if (is_user_my.equals("0")) //나의 스크랩일 경우.//
                {
                    intent.putExtra("KEY_USER_IDENTIFY_FLAG", "0");
                }

                Log.d("json control", "tag scrap id" + userScrapContentData.userScrapContentDataList.get(position).getId());

                intent.putExtra(SCRAP_ID, "" + userScrapContentData.userScrapContentDataList.get(position).getId());

                startActivity(intent);
            }
        });

        /** Tag검색과 일반 카테고리 리스트를 통한 검색경로를 구분 **/
        if (flag_tag.equals("TAG")) //태그일 경우 스크랩 검색 조건이 다르므로 설정.//
        {
            Log.d("tag name", folder_name);

            //태그이름으로 세부검색 후 리스트 목록//
            getTagData(folder_id);
        } else //태그가 아닌 일반 폴더리스트에서 선택 후 온 경우//
        {
            getScrapContentListNetworkData();
        }
    }

    public void init_scrap_content_data() {
        userScrapContentData.userScrapContentDataList.clear();

        mAdapter.init_ScrapContent_Data(userScrapContentData, is_user_my);
    }

    public void getTagData(String folder_name) {
        /** 네트워크 설정 **/
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("search")
                .addQueryParameter("target", "4") //4는 태그상세 검색//
                .addQueryParameter("word", folder_name) //word는 검색단어(태그 상세검색 시 필요)//
                //page값은 스와이프 유무에 따라 동적으로 변화된다.//
                .addQueryParameter("page", "" + page_count)
                .addQueryParameter("count", "20"); //count는 20개로 고정.//

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestSearchTagListCallback);
    }

    public void set_Taglist_Data(final TagDetailListRequestResults tagDetailListRequestResults[], final int tag_detail_list_size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<TagDetailListRequestResults> tagDetailListRequestResultsList = new ArrayList<>();

                    tagDetailListRequestResultsList.addAll(Arrays.asList(tagDetailListRequestResults));

                    for (int i = 0; i < tag_detail_list_size; i++) {
                        UserScrapContentData newUserScrapCotentData = new UserScrapContentData();

                        newUserScrapCotentData.setTitle(tagDetailListRequestResultsList.get(i).getTitle());
                        newUserScrapCotentData.setNcTitle(tagDetailListRequestResultsList.get(i).getNc_title());
                        newUserScrapCotentData.setNcImgUrl(tagDetailListRequestResultsList.get(i).getNc_img_url());
                        newUserScrapCotentData.setNcAuthor(tagDetailListRequestResultsList.get(i).getNc_author());
                        newUserScrapCotentData.setNcTime(tagDetailListRequestResultsList.get(i).getNc_ntime());
                        newUserScrapCotentData.setLike(tagDetailListRequestResultsList.get(i).getFavorite_cnt());
                        newUserScrapCotentData.setLikeFlag(tagDetailListRequestResultsList.get(i).getFavorite());
                        newUserScrapCotentData.setId(tagDetailListRequestResultsList.get(i).getId());

                        userScrapContentData.userScrapContentDataList.add(newUserScrapCotentData);
                    }

                    mAdapter.setUserScrapContentData(userScrapContentData, is_user_my);
                }
            });
        }
    }


    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mAdapter.notifyDataSetChanged();
    }
}
