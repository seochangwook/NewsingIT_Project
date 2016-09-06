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
    private static final String SCRAP_LOCK = "SCRAP_LOCK";
    private static final String SCRAP_ID = "SCRAP_ID";
    private static final String KEY_TAGSEARCH_FLAG = "KEY_TAGSEARCH_FLAG";

    static int page_count = 1;

    String folder_name;
    String is_user_my;
    String folder_id;
    String flag_tag = null;
    boolean scrap_private;

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
            setData(request.getResults(), request.getResults().length);
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

            set_Taglist_Data(tagDetailListRequest.getResults(), tagDetailListRequest.getResults().length);
        }
    };

    private void getScrapContentListNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        Log.d("page count", "" + page_count);

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.server_domain))
                .addPathSegment("scraps")
                .addQueryParameter("category", folder_id)
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
                        newUserScrapCotentData.setLock(scrapContentList.get(i).getLock());
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

                        scrap_recyclerrefreshview.pullRefreshComplete();
                        scrap_recyclerView.removeHeaderView(headerview);

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
                String userSelect = userScrapContentData.userScrapContentDataList.get(position).getNcTitle().toString();
                boolean scrap_isprivate = userScrapContentData.userScrapContentDataList.get(position).getLock();

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

                //필요한 값을 정의한다.//
                intent.putExtra(SCRAP_ID, userScrapContentData.userScrapContentDataList.get(position).getId());
                intent.putExtra(SCRAP_LOCK, scrap_isprivate);

                // intent.putExtra("SCRAP_AUTHOR", userScrapContentData.userScrapContentDataList.get(position).getNcAuthor());

                startActivity(intent);
            }
        });

        if (flag_tag.equals("TAG")) //태그일 경우 스크랩 검색 조건이 다르므로 설정.//
        {
            Log.d("tag name", folder_name);

            //태그이름으로 세부검색 후 리스트 목록//
            getTagData(folder_id);
        } else //태그가 아닌 일반 폴더리스트에서 선택 후 온 경우//
        {
            getScrapContentListNetworkData();
        }
//
//      initDummyData();
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
                .host(getResources().getString(R.string.server_domain))
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
                        //newUserScrapCotentData.setLock(tagDetailListRequestResultsList.get(i).getLock());
                        newUserScrapCotentData.setId(tagDetailListRequestResultsList.get(i).getId());

                        //태그 검색은 기본적으로 다른 사용자임을 가정//
                        /*if (newUserScrapCotentData.getLock() == false) {
                            userScrapContentData.userScrapContentDataList.add(newUserScrapCotentData);
                        } else if (newUserScrapCotentData.getLock() == true) {
                            //다른 사람 개시물일 경우 잠금이 있을 시 보여주지 않는다.//
                        }*/

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


//    private void initDummyData() {
//        String[] titleList = {"갤럭시 7", "iot", "소프트뱅크", "드론", "VR", "테슬라"};
//
//        String[] contentList = {"갤럭시 7 너무 사고 싶당 홍채인식과 물 속에서 터치 펜 기능이 가능하다 그런데 누가 물속에서 폰을 할까"
//                , "iot 요즘 대세라는데... 사물 인터넷(Internet of Things, 약어로 IoT)은 각종 사물에 센서와 통신 기능을 내장하여 인터넷에 연결하는 기술을 의미한다. 여기서 사물이란 가전제품, 모바일 장비, 웨어러블 컴퓨터 등 다양한 임베디드 시스템이 된다. "
//                , "소프트뱅크 주식회사(영어: SoftBank Corporation, 일본어: ソフトバンク株式会社)는 1981년 9월 3일 일본 도쿄에서 설립된 고속 인터넷, 전자 상거래, 파이낸스, 기술 관련 분야에서 활동하는 일본의 기업 겸임 일본의 이동통신사이다. 사장은 한국계 일본인인 손 마사요시(손정의)이다."
//                , "무인 항공기(無人航空機, 영어: unmanned aerial vehicle, UAV) 또는 무인 항공기의 다른 이름으로 '벌이 윙윙거린다'는 것에서 \"드론\"(drone)이라고도 불리기도 한다. ta1 드론(영어: drone)은 조종사를 탑승하지 않고 지정된 임무를 수행할 수 있도록 제작한 비행체이다. 기체에 사람이 타지 않은 것으로 지상에는 원격 조종하는 조종사가 존재하고 있다는 점을 강조해 Uninhabited Aerial(Air) Vehicle의 약어로 지칭하는 경우도 있다."
//                , "수많은 미디어 기업들이 가상 현실 시장에 접근하고 있는 가운데 디즈니도 가상 현실 세계에 첫 발을 디뎠다. 디즈니는 현지 시각으로 16일, 디즈니 영화 장면들을 가상 현실 플랫폼에서 볼 수 있는 디즈니 무비 VR(Disney Movie VR)을 공개했다."
//                , "테슬라는 다음을 가리키는 말이다. 니콜라 테슬라는 미국의 과학자이다. SI 단위계 에서 테슬라는 자기장에 대한 유도단위이다. 엔비디아 테슬라는 엔비디아의 GPU이다. 테슬라 모터스"};
//
//        String[] dateList = {"16.08.29", "16.08.28", "16.08.27"
//                , "16.08.26", "16.08.25", "16.08.24"};
//        int[] likeList = {10, 10, 3, 4, 125, 23};
//        boolean[] likeFlagList = {false, false, false, false, false, false};
//
//        for (int i = 0; i < 6; i++) {
//            UserScrapContentData new_userScrapData = new UserScrapContentData();
//            new_userScrapData.setTitle(titleList[i]);
//            new_userScrapData.setNcTitle(contentList[i]);
//            new_userScrapData.setNcTime(dateList[i]);
//            new_userScrapData.setLike(likeList[i]);
//            new_userScrapData.setLikeFlag(likeFlagList[i]);
//
//            userScrapContentData.userScrapContentDataList.add(new_userScrapData);
//        }
//        mAdapter.setUserScrapContentData(userScrapContentData, is_user_my); //구분 플래그를 같이 넣어준다.//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        //mAdapter.notifyDataSetChanged();
    }
}
