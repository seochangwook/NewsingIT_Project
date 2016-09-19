package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SearchTabActivity;
import com.example.apple.newsingit_project.SelectNewsDetailActivity;
import com.example.apple.newsingit_project.data.json_data.mainnewslist.MainNewsListRequest;
import com.example.apple.newsingit_project.data.json_data.mainnewslist.MainNewsListRequestResults;
import com.example.apple.newsingit_project.data.json_data.mainnewslist.MainNewsListRequestResultsNewscontens;
import com.example.apple.newsingit_project.data.view_data.Keyword10NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword10SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword1NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword1SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword2NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword2SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword3NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword3SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword4NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword4SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword5NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword5SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword6NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword6SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword7NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword7SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword8NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword8SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword9NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword9SectionData;
import com.example.apple.newsingit_project.data.view_data.KeywordSection;
import com.example.apple.newsingit_project.data.view_data.NewsContent;
import com.example.apple.newsingit_project.dialog.KeywordListActivity;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.NewsAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainNewsListFragment extends Fragment {
    /**
     * Data trans
     **/
    private static final int RC_KEYWORD = 1000;
    private static final String KEY_KEYWORD = "KEY_KEYWORD";
    private static final String NEWS_ID = "NEWS_ID";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    /**
     * RecyclerView의 데이터와 어댑터 관련 변수
     **/
    NewsContent newsContent;
    KeywordSection keywordSection;
    NewsAdapter newsAdapter;

    FloatingActionButton topup_button;
    float startYPosition = 0; //기본적으로 스크롤은 Y축을 기준으로 계산.//
    float endYPosition = 0;
    boolean firstDragFlag = true;
    boolean motionFlag = true;
    boolean dragFlag = false; //현재 터치가 드래그인지 먼저 확인//
    /**
     * Network variable
     **/
    NetworkManager manager;
    /**
     * Header, emptyview
     **/
    View fix_headerview;
    View emptyview;
    private FamiliarRefreshRecyclerView mainnews_recyclerrefreshview;
    private FamiliarRecyclerView mainnews_recyclerview;
    /**
     * 기타 로딩 기능
     **/
    private ProgressDialog pDialog;

    private Callback requestmainnewslistcallback = new Callback() {
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

            if (response.code() == 403 || response.code() == 401) //인증이 안된 경우//
            {

            } else {
                Gson gson = new Gson();

                MainNewsListRequest mainNewsListRequest = gson.fromJson(response_data, MainNewsListRequest.class);

                setData(mainNewsListRequest.getResults(), mainNewsListRequest.getResults().length);
            }
        }
    };

    public MainNewsListFragment() //프래그먼트는 반드시 한개 이상의 생성자가 존재해야 한다.//
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_news_list, container, false);

        setHasOptionsMenu(true); //메뉴를 적용한다.//

        mainnews_recyclerrefreshview = (FamiliarRefreshRecyclerView) view.findViewById(R.id.main_rv_list);
        topup_button = (FloatingActionButton) view.findViewById(R.id.scroll_topup_button);

        //처음 스크롤 버튼은 보이지 않게 한다.//
        topup_button.setVisibility(View.GONE);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //리사이클뷰 설정//
        /** 메인뉴스 리스트뷰 초기화 과정(로딩화면, 자원등록) **/
        mainnews_recyclerrefreshview.setLoadMoreView(new LoadMoreView(getActivity(), 1));
        mainnews_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        mainnews_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        mainnews_recyclerview = mainnews_recyclerrefreshview.getFamiliarRecyclerView();
        mainnews_recyclerview.setItemAnimator(new DefaultItemAnimator());
        mainnews_recyclerview.setHasFixedSize(true);

        /** 객체할당 및 어댑터 초기화 **/
        newsContent = new NewsContent();
        keywordSection = new KeywordSection();
        newsAdapter = new NewsAdapter(getActivity());

        mainnews_recyclerview.setAdapter(newsAdapter); //어댑터 할당.//

        /** EmptyView, HeaderView를 설정 **/
        emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_mainnews_empty_layout, null);
        mainnews_recyclerview.setEmptyView(emptyview, true);

        fix_headerview = getActivity().getLayoutInflater().inflate(R.layout.view_mainnews_refreshheader, null);
        mainnews_recyclerview.addHeaderView(fix_headerview);

        /** 뉴스 리스트 선택 이벤트 설정(개수가 고정되어 있기에 직접할당,일반적으로는 동적할당) **/
        mainnews_recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                //정확한 영역에 데이터를 줄 경우 각 키워드별로 index를 따져서 나눔.(각 키워드마다 +5의 gap)//

                //키워드 1에 관련된 영역//
                if (position >= 1 && position <= 3) {
                    int end_position = 3;
                    int select_position = -1;

                    if (position == 1) {
                        select_position = end_position - end_position;
                    } else if (position == 2) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 3) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_1_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_1_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 2에 관련된 영역//
                if (position >= 8 && position <= 10) {
                    int end_position = 10;
                    int select_position = -1;

                    if (position == 8) {
                        select_position = end_position - end_position;
                    } else if (position == 9) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 10) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_2_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_2_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 3에 관련된 영역//
                if (position >= 15 && position <= 17) {
                    int end_position = 17;
                    int select_position = -1;

                    if (position == 15) {
                        select_position = end_position - end_position;
                    } else if (position == 16) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 17) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_3_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_3_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 4에 관련된 영역//
                if (position >= 22 && position <= 24) {
                    int end_position = 22;
                    int select_position = -1;

                    if (position == 22) {
                        select_position = end_position - end_position;
                    } else if (position == 23) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 24) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_4_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_4_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 5에 관련된 영역//
                if (position >= 29 && position <= 31) {
                    int end_position = 29;
                    int select_position = -1;

                    if (position == 29) {
                        select_position = end_position - end_position;
                    } else if (position == 30) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 31) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_5_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_5_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 6에 관련된 영역//
                if (position >= 36 && position <= 38) {
                    int end_position = 26;
                    int select_position = -1;

                    if (position == 36) {
                        select_position = end_position - end_position;
                    } else if (position == 37) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 38) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_6_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_6_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 7에 관련된 영역//
                if (position >= 43 && position <= 45) {
                    int end_position = 43;
                    int select_position = -1;

                    if (position == 43) {
                        select_position = end_position - end_position;
                    } else if (position == 44) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 45) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_7_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_7_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 8에 관련된 영역//
                if (position >= 50 && position <= 52) {
                    int end_position = 50;
                    int select_position = -1;

                    if (position == 50) {
                        select_position = end_position - end_position;
                    } else if (position == 51) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 52) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_8_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_8_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 9에 관련된 영역//
                if (position >= 57 && position <= 59) {
                    int end_position = 57;
                    int select_position = -1;

                    if (position == 57) {
                        select_position = end_position - end_position;
                    } else if (position == 58) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 59) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_9_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_9_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }

                //키워드 10에 관련된 영역//
                if (position >= 64 && position <= 66) {
                    int end_position = 64;
                    int select_position = -1;

                    if (position == 64) {
                        select_position = end_position - end_position;
                    } else if (position == 65) {
                        select_position = end_position - (end_position - 1);
                    } else if (position == 66) {
                        select_position = end_position - (end_position - 2);
                    }

                    int news_id = newsContent.keyword_10_news_content.get(select_position).get_news_content_id();
                    String news_title = newsContent.keyword_10_news_content.get(select_position).get_news_title();

                    Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                    //선택된 뉴스의 검색을 위해서 id값을 넘긴다.//
                    intent.putExtra(NEWS_TITLE, news_title);
                    intent.putExtra(NEWS_ID, "" + news_id);

                    startActivity(intent);
                }
            }
        });

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        mainnews_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        mainnews_recyclerrefreshview.pullRefreshComplete();
                        mainnews_recyclerview.removeHeaderView(fix_headerview);

                        //기존 데이터를 전부 지운다.//
                        Data_Init();

                        //데이터를 다시 초기화//
                        get_MainNewsData();

                    }
                }, 1000);
            }
        });

        mainnews_recyclerrefreshview.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        mainnews_recyclerrefreshview.loadMoreComplete();

                        //기존 데이터를 전부 지운다.//
                        Data_Init();

                        //데이터를 다시 초기화//
                        get_MainNewsData();

                    }
                }, 1000);
            }
        });

        /** 스크롤 관련 이벤트 처리 **/
        topup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainnews_recyclerview.smoothScrollToPosition(0); //리스트의 맨 처음으로 이동//

                topup_button.setVisibility(View.GONE);
            }
        });

        /** Scroll 처리를 위한 Touch 이벤트 구현 **/
        mainnews_recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //우선 사용자의 리스너 이벤트 종류를 받아온다.//

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        dragFlag = true;

                        //사용자는 보통 한 번 터치 후 내리거나 올리는 작업을 하기에 반복작업을 피하기 위해서 true/false로 한번만 되도록 구현.//
                        if (firstDragFlag) //첫번째 움직임을 가지고 판단하기 위해서//
                        {
                            startYPosition = motionEvent.getY();
                            firstDragFlag = false;
                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        endYPosition = motionEvent.getY();
                        firstDragFlag = true;

                        if (dragFlag) {
                            // 시작Y가 끝 Y보다 크다면 터치가 아래서 위로 이루어졌다는 것이고, 스크롤은 아래로내려갔다는 뜻이다.
                            if ((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
                                Log.d("scroll event : ", "scroll down");

                                topup_button.setVisibility(View.VISIBLE);
                            }

                            //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
                            else if ((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
                                Log.d("scroll event : ", "scroll up");

                                topup_button.setVisibility(View.GONE);
                            }
                        }

                        //다시 Y축에 대한 위치를 초기화.//
                        startYPosition = 0.0f;
                        endYPosition = 0.0f;
                        motionFlag = false;

                        break;
                    }
                }

                return false;
            }
        });

        /** Data Setting **/
        //initDummyData();

        showpDialog();

        get_MainNewsData();

        hidepDialog();

        return view;
    }

    public void get_MainNewsData() {
        /** 네트워크 작업 설정 **/
        //네트워크로 부터 데이터를 가져온다.//

        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("newscontents");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmainnewslistcallback);
    }

    public void setData(final MainNewsListRequestResults mainNewsListRequest[], final int mainNewsListRequest_size) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MainNewsListRequestResults> mainNewsListRequestResultses = new ArrayList<>();

                    mainNewsListRequestResultses.addAll(Arrays.asList(mainNewsListRequest));

                    //키워드와 뉴스 콘텐츠별로 데이터 셋팅(전체 키워드는 10개이므로 전체 10번돌면 종료)//
                    for (int i = 0; i < mainNewsListRequest_size; i++) {
                        //각 키워드 별로 구분(키워드 당 3개의 콘텐츠)//
                        if (i == 0) {
                            Keyword1SectionData new_keyword_section_1 = new Keyword1SectionData();

                            new_keyword_section_1.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword1SectionDatas.add(new_keyword_section_1);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword1NewsContentData new_news1content_1 = new Keyword1NewsContentData();

                                    new_news1content_1.set_news_author(author);
                                    new_news1content_1.set_news_content(contents);
                                    new_news1content_1.set_news_content_id(news_id);
                                    new_news1content_1.set_news_thumbnail_Url(img_Url);
                                    new_news1content_1.set_news_write_date(ntime);
                                    new_news1content_1.set_news_title(title);

                                    newsContent.keyword_1_news_content.add(new_news1content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword1NewsContentData new_news1content_2 = new Keyword1NewsContentData();

                                    new_news1content_2.set_news_author(author);
                                    new_news1content_2.set_news_content(contents);
                                    new_news1content_2.set_news_content_id(news_id);
                                    new_news1content_2.set_news_thumbnail_Url(img_Url);
                                    new_news1content_2.set_news_write_date(ntime);
                                    new_news1content_2.set_news_title(title);

                                    newsContent.keyword_1_news_content.add(new_news1content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword1NewsContentData new_news1content_3 = new Keyword1NewsContentData();

                                    new_news1content_3.set_news_author(author);
                                    new_news1content_3.set_news_content(contents);
                                    new_news1content_3.set_news_content_id(news_id);
                                    new_news1content_3.set_news_thumbnail_Url(img_Url);
                                    new_news1content_3.set_news_write_date(ntime);
                                    new_news1content_3.set_news_title(title);

                                    newsContent.keyword_1_news_content.add(new_news1content_3);
                                }
                            }
                        } else if (i == 1) {
                            Keyword2SectionData new_keyword_section_2 = new Keyword2SectionData();

                            new_keyword_section_2.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword2SectionDatas.add(new_keyword_section_2);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword2NewsContentData new_news2content_1 = new Keyword2NewsContentData();

                                    new_news2content_1.set_news_author(author);
                                    new_news2content_1.set_news_content(contents);
                                    new_news2content_1.set_news_content_id(news_id);
                                    new_news2content_1.set_news_thumbnail_Url(img_Url);
                                    new_news2content_1.set_news_write_date(ntime);
                                    new_news2content_1.set_news_title(title);

                                    newsContent.keyword_2_news_content.add(new_news2content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword2NewsContentData new_news2content_2 = new Keyword2NewsContentData();

                                    new_news2content_2.set_news_author(author);
                                    new_news2content_2.set_news_content(contents);
                                    new_news2content_2.set_news_content_id(news_id);
                                    new_news2content_2.set_news_thumbnail_Url(img_Url);
                                    new_news2content_2.set_news_write_date(ntime);
                                    new_news2content_2.set_news_title(title);

                                    newsContent.keyword_2_news_content.add(new_news2content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword2NewsContentData new_news2content_3 = new Keyword2NewsContentData();

                                    new_news2content_3.set_news_author(author);
                                    new_news2content_3.set_news_content(contents);
                                    new_news2content_3.set_news_content_id(news_id);
                                    new_news2content_3.set_news_thumbnail_Url(img_Url);
                                    new_news2content_3.set_news_write_date(ntime);
                                    new_news2content_3.set_news_title(title);

                                    newsContent.keyword_2_news_content.add(new_news2content_3);
                                }
                            }
                        } else if (i == 2) {
                            Keyword3SectionData new_keyword_section_3 = new Keyword3SectionData();

                            new_keyword_section_3.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword3SectionDatas.add(new_keyword_section_3);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword3NewsContentData new_news3content_1 = new Keyword3NewsContentData();

                                    new_news3content_1.set_news_author(author);
                                    new_news3content_1.set_news_content(contents);
                                    new_news3content_1.set_news_content_id(news_id);
                                    new_news3content_1.set_news_thumbnail_Url(img_Url);
                                    new_news3content_1.set_news_write_date(ntime);
                                    new_news3content_1.set_news_title(title);

                                    newsContent.keyword_3_news_content.add(new_news3content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword3NewsContentData new_news3content_2 = new Keyword3NewsContentData();

                                    new_news3content_2.set_news_author(author);
                                    new_news3content_2.set_news_content(contents);
                                    new_news3content_2.set_news_content_id(news_id);
                                    new_news3content_2.set_news_thumbnail_Url(img_Url);
                                    new_news3content_2.set_news_write_date(ntime);
                                    new_news3content_2.set_news_title(title);

                                    newsContent.keyword_3_news_content.add(new_news3content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword3NewsContentData new_news3content_3 = new Keyword3NewsContentData();

                                    new_news3content_3.set_news_author(author);
                                    new_news3content_3.set_news_content(contents);
                                    new_news3content_3.set_news_content_id(news_id);
                                    new_news3content_3.set_news_thumbnail_Url(img_Url);
                                    new_news3content_3.set_news_write_date(ntime);
                                    new_news3content_3.set_news_title(title);

                                    newsContent.keyword_3_news_content.add(new_news3content_3);
                                }
                            }
                        } else if (i == 3) {
                            Keyword4SectionData new_keyword_section_4 = new Keyword4SectionData();

                            new_keyword_section_4.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword4SectionDatas.add(new_keyword_section_4);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword4NewsContentData new_news4content_1 = new Keyword4NewsContentData();

                                    new_news4content_1.set_news_author(author);
                                    new_news4content_1.set_news_content(contents);
                                    new_news4content_1.set_news_content_id(news_id);
                                    new_news4content_1.set_news_thumbnail_Url(img_Url);
                                    new_news4content_1.set_news_write_date(ntime);
                                    new_news4content_1.set_news_title(title);

                                    newsContent.keyword_4_news_content.add(new_news4content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword4NewsContentData new_news4content_2 = new Keyword4NewsContentData();

                                    new_news4content_2.set_news_author(author);
                                    new_news4content_2.set_news_content(contents);
                                    new_news4content_2.set_news_content_id(news_id);
                                    new_news4content_2.set_news_thumbnail_Url(img_Url);
                                    new_news4content_2.set_news_write_date(ntime);
                                    new_news4content_2.set_news_title(title);

                                    newsContent.keyword_4_news_content.add(new_news4content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword4NewsContentData new_news4content_3 = new Keyword4NewsContentData();

                                    new_news4content_3.set_news_author(author);
                                    new_news4content_3.set_news_content(contents);
                                    new_news4content_3.set_news_content_id(news_id);
                                    new_news4content_3.set_news_thumbnail_Url(img_Url);
                                    new_news4content_3.set_news_write_date(ntime);
                                    new_news4content_3.set_news_title(title);

                                    newsContent.keyword_4_news_content.add(new_news4content_3);
                                }
                            }
                        } else if (i == 4) {
                            Keyword5SectionData new_keyword_section_5 = new Keyword5SectionData();

                            new_keyword_section_5.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword5SectionDatas.add(new_keyword_section_5);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword5NewsContentData new_news5content_1 = new Keyword5NewsContentData();

                                    new_news5content_1.set_news_author(author);
                                    new_news5content_1.set_news_content(contents);
                                    new_news5content_1.set_news_content_id(news_id);
                                    new_news5content_1.set_news_thumbnail_Url(img_Url);
                                    new_news5content_1.set_news_write_date(ntime);
                                    new_news5content_1.set_news_title(title);

                                    newsContent.keyword_5_news_content.add(new_news5content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword5NewsContentData new_news5content_2 = new Keyword5NewsContentData();

                                    new_news5content_2.set_news_author(author);
                                    new_news5content_2.set_news_content(contents);
                                    new_news5content_2.set_news_content_id(news_id);
                                    new_news5content_2.set_news_thumbnail_Url(img_Url);
                                    new_news5content_2.set_news_write_date(ntime);
                                    new_news5content_2.set_news_title(title);

                                    newsContent.keyword_5_news_content.add(new_news5content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword5NewsContentData new_news5content_3 = new Keyword5NewsContentData();

                                    new_news5content_3.set_news_author(author);
                                    new_news5content_3.set_news_content(contents);
                                    new_news5content_3.set_news_content_id(news_id);
                                    new_news5content_3.set_news_thumbnail_Url(img_Url);
                                    new_news5content_3.set_news_write_date(ntime);
                                    new_news5content_3.set_news_title(title);

                                    newsContent.keyword_5_news_content.add(new_news5content_3);
                                }
                            }
                        } else if (i == 5) {
                            Keyword6SectionData new_keyword_section_6 = new Keyword6SectionData();

                            new_keyword_section_6.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword6SectionDatas.add(new_keyword_section_6);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword6NewsContentData new_news6content_1 = new Keyword6NewsContentData();

                                    new_news6content_1.set_news_author(author);
                                    new_news6content_1.set_news_content(contents);
                                    new_news6content_1.set_news_content_id(news_id);
                                    new_news6content_1.set_news_thumbnail_Url(img_Url);
                                    new_news6content_1.set_news_write_date(ntime);
                                    new_news6content_1.set_news_title(title);

                                    newsContent.keyword_6_news_content.add(new_news6content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword6NewsContentData new_news6content_2 = new Keyword6NewsContentData();

                                    new_news6content_2.set_news_author(author);
                                    new_news6content_2.set_news_content(contents);
                                    new_news6content_2.set_news_content_id(news_id);
                                    new_news6content_2.set_news_thumbnail_Url(img_Url);
                                    new_news6content_2.set_news_write_date(ntime);
                                    new_news6content_2.set_news_title(title);

                                    newsContent.keyword_6_news_content.add(new_news6content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword6NewsContentData new_news6content_3 = new Keyword6NewsContentData();

                                    new_news6content_3.set_news_author(author);
                                    new_news6content_3.set_news_content(contents);
                                    new_news6content_3.set_news_content_id(news_id);
                                    new_news6content_3.set_news_thumbnail_Url(img_Url);
                                    new_news6content_3.set_news_write_date(ntime);
                                    new_news6content_3.set_news_title(title);

                                    newsContent.keyword_6_news_content.add(new_news6content_3);
                                }
                            }
                        } else if (i == 6) {
                            Keyword7SectionData new_keyword_section_7 = new Keyword7SectionData();

                            new_keyword_section_7.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword7SectionDatas.add(new_keyword_section_7);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword7NewsContentData new_news7content_1 = new Keyword7NewsContentData();

                                    new_news7content_1.set_news_author(author);
                                    new_news7content_1.set_news_content(contents);
                                    new_news7content_1.set_news_content_id(news_id);
                                    new_news7content_1.set_news_thumbnail_Url(img_Url);
                                    new_news7content_1.set_news_write_date(ntime);
                                    new_news7content_1.set_news_title(title);

                                    newsContent.keyword_7_news_content.add(new_news7content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword7NewsContentData new_news7content_2 = new Keyword7NewsContentData();

                                    new_news7content_2.set_news_author(author);
                                    new_news7content_2.set_news_content(contents);
                                    new_news7content_2.set_news_content_id(news_id);
                                    new_news7content_2.set_news_thumbnail_Url(img_Url);
                                    new_news7content_2.set_news_write_date(ntime);
                                    new_news7content_2.set_news_title(title);

                                    newsContent.keyword_7_news_content.add(new_news7content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword7NewsContentData new_news7content_3 = new Keyword7NewsContentData();

                                    new_news7content_3.set_news_author(author);
                                    new_news7content_3.set_news_content(contents);
                                    new_news7content_3.set_news_content_id(news_id);
                                    new_news7content_3.set_news_thumbnail_Url(img_Url);
                                    new_news7content_3.set_news_write_date(ntime);
                                    new_news7content_3.set_news_title(title);

                                    newsContent.keyword_7_news_content.add(new_news7content_3);
                                }
                            }
                        } else if (i == 7) {
                            Keyword8SectionData new_keyword_section_8 = new Keyword8SectionData();

                            new_keyword_section_8.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword8SectionDatas.add(new_keyword_section_8);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword8NewsContentData new_news8content_1 = new Keyword8NewsContentData();

                                    new_news8content_1.set_news_author(author);
                                    new_news8content_1.set_news_content(contents);
                                    new_news8content_1.set_news_content_id(news_id);
                                    new_news8content_1.set_news_thumbnail_Url(img_Url);
                                    new_news8content_1.set_news_write_date(ntime);
                                    new_news8content_1.set_news_title(title);

                                    newsContent.keyword_8_news_content.add(new_news8content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword8NewsContentData new_news8content_2 = new Keyword8NewsContentData();

                                    new_news8content_2.set_news_author(author);
                                    new_news8content_2.set_news_content(contents);
                                    new_news8content_2.set_news_content_id(news_id);
                                    new_news8content_2.set_news_thumbnail_Url(img_Url);
                                    new_news8content_2.set_news_write_date(ntime);
                                    new_news8content_2.set_news_title(title);

                                    newsContent.keyword_8_news_content.add(new_news8content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword8NewsContentData new_news8content_3 = new Keyword8NewsContentData();

                                    new_news8content_3.set_news_author(author);
                                    new_news8content_3.set_news_content(contents);
                                    new_news8content_3.set_news_content_id(news_id);
                                    new_news8content_3.set_news_thumbnail_Url(img_Url);
                                    new_news8content_3.set_news_write_date(ntime);
                                    new_news8content_3.set_news_title(title);

                                    newsContent.keyword_8_news_content.add(new_news8content_3);
                                }
                            }
                        } else if (i == 8) {
                            Keyword9SectionData new_keyword_section_9 = new Keyword9SectionData();

                            new_keyword_section_9.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keyword9SectionDatas.add(new_keyword_section_9);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword9NewsContentData new_news9content_1 = new Keyword9NewsContentData();

                                    new_news9content_1.set_news_author(author);
                                    new_news9content_1.set_news_content(contents);
                                    new_news9content_1.set_news_content_id(news_id);
                                    new_news9content_1.set_news_thumbnail_Url(img_Url);
                                    new_news9content_1.set_news_write_date(ntime);
                                    new_news9content_1.set_news_title(title);

                                    newsContent.keyword_9_news_content.add(new_news9content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword9NewsContentData new_news9content_2 = new Keyword9NewsContentData();

                                    new_news9content_2.set_news_author(author);
                                    new_news9content_2.set_news_content(contents);
                                    new_news9content_2.set_news_content_id(news_id);
                                    new_news9content_2.set_news_thumbnail_Url(img_Url);
                                    new_news9content_2.set_news_write_date(ntime);
                                    new_news9content_2.set_news_title(title);

                                    newsContent.keyword_9_news_content.add(new_news9content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword9NewsContentData new_news9content_3 = new Keyword9NewsContentData();

                                    new_news9content_3.set_news_author(author);
                                    new_news9content_3.set_news_content(contents);
                                    new_news9content_3.set_news_content_id(news_id);
                                    new_news9content_3.set_news_thumbnail_Url(img_Url);
                                    new_news9content_3.set_news_write_date(ntime);
                                    new_news9content_3.set_news_title(title);

                                    newsContent.keyword_9_news_content.add(new_news9content_3);
                                }
                            }
                        } else if (i == 9) {
                            Keyword10SectionData new_keyword_section_10 = new Keyword10SectionData();

                            new_keyword_section_10.set_keyword_text(mainNewsListRequestResultses.get(i).getKeyword());

                            keywordSection.keywor10dSectionDatas.add(new_keyword_section_10);

                            //콘텐츠 설정.//
                            int news_content_size = mainNewsListRequestResultses.get(i).getNewscontens().length;
                            MainNewsListRequestResultsNewscontens news_content[] = mainNewsListRequestResultses.get(i).getNewscontens();

                            for (int content_size = 0; content_size < news_content_size; content_size++) {
                                //첫번째 키워드에 대한 첫번째 컨텐츠.//
                                if (content_size == 0) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    //배열에 저장//
                                    Keyword10NewsContentData new_news10content_1 = new Keyword10NewsContentData();

                                    new_news10content_1.set_news_author(author);
                                    new_news10content_1.set_news_content(contents);
                                    new_news10content_1.set_news_content_id(news_id);
                                    new_news10content_1.set_news_thumbnail_Url(img_Url);
                                    new_news10content_1.set_news_write_date(ntime);
                                    new_news10content_1.set_news_title(title);

                                    newsContent.keyword_10_news_content.add(new_news10content_1);
                                } else if (content_size == 1) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword10NewsContentData new_news10content_2 = new Keyword10NewsContentData();

                                    new_news10content_2.set_news_author(author);
                                    new_news10content_2.set_news_content(contents);
                                    new_news10content_2.set_news_content_id(news_id);
                                    new_news10content_2.set_news_thumbnail_Url(img_Url);
                                    new_news10content_2.set_news_write_date(ntime);
                                    new_news10content_2.set_news_title(title);

                                    newsContent.keyword_10_news_content.add(new_news10content_2);
                                } else if (content_size == 2) {
                                    String title = news_content[content_size].getTitle();
                                    String author = news_content[content_size].getAuthor();
                                    String img_Url = news_content[content_size].getImg_url();
                                    //String img_Url = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                                    int news_id = news_content[content_size].getId();
                                    String contents = news_content[content_size].getContents();
                                    String ntime = news_content[content_size].getNtime();

                                    Keyword10NewsContentData new_news10content_3 = new Keyword10NewsContentData();

                                    new_news10content_3.set_news_author(author);
                                    new_news10content_3.set_news_content(contents);
                                    new_news10content_3.set_news_content_id(news_id);
                                    new_news10content_3.set_news_thumbnail_Url(img_Url);
                                    new_news10content_3.set_news_write_date(ntime);
                                    new_news10content_3.set_news_title(title);

                                    newsContent.keyword_10_news_content.add(new_news10content_3);
                                }
                            }
                        }
                    }

                    newsAdapter.setNewsData(keywordSection, newsContent);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RC_KEYWORD) {
                String select_keyword = data.getStringExtra(KEY_KEYWORD);
                Log.d("intent data", select_keyword);

                //스크롤을 이동하는 작업//
                move_scrollaction(select_keyword);
            }
        }
    }

    public void Data_Init() {
        //모든 배열을 초기화.//
        keywordSection.keyword1SectionDatas.clear();
        keywordSection.keyword2SectionDatas.clear();
        keywordSection.keyword3SectionDatas.clear();
        keywordSection.keyword4SectionDatas.clear();
        keywordSection.keyword5SectionDatas.clear();
        keywordSection.keyword6SectionDatas.clear();
        keywordSection.keyword7SectionDatas.clear();
        keywordSection.keyword8SectionDatas.clear();
        keywordSection.keyword9SectionDatas.clear();
        keywordSection.keywor10dSectionDatas.clear();

        newsContent.keyword_1_news_content.clear();
        newsContent.keyword_2_news_content.clear();
        newsContent.keyword_3_news_content.clear();
        newsContent.keyword_4_news_content.clear();
        newsContent.keyword_5_news_content.clear();
        newsContent.keyword_6_news_content.clear();
        newsContent.keyword_7_news_content.clear();
        newsContent.keyword_8_news_content.clear();
        newsContent.keyword_9_news_content.clear();
        newsContent.keyword_10_news_content.clear();

        //초기화된 배열을 다시 리사이클뷰에 적용(실제 데이터의 관리는 어댑터에서 한다.)//
        newsAdapter.init_Data(keywordSection, newsContent);
    }

    public void move_scrollaction(String select_keyword) {
        //제공되는 키워드목록과 메인 뉴스 리스트에 나타날 키워드 섹션과는 호환되는 가정//

        //각 index별로 비교(gap : 7)//
        //키워드1 - 0 / 키워드2 - 7 / ...//
        int move_position_index = 0; //없으면 최상단으로 디폴트//

        if (keywordSection.keyword1SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 1과 같다면 이동할 인덱스는 0//
            move_position_index = 0;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword2SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 2과 같다면 이동할 인덱스는 7//
            move_position_index = 7;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword3SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 3과 같다면 이동할 인덱스는 14//
            move_position_index = 14;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword4SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 4과 같다면 이동할 인덱스는 21//
            move_position_index = 21;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword5SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 5과 같다면 이동할 인덱스는 28//
            move_position_index = 28;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword6SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 6과 같다면 이동할 인덱스는 35//
            move_position_index = 35;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword7SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 7과 같다면 이동할 인덱스는 14//
            move_position_index = 42;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword8SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 8과 같다면 이동할 인덱스는 14//
            move_position_index = 49;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keyword9SectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 9과 같다면 이동할 인덱스는 14//
            move_position_index = 56;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        } else if (keywordSection.keywor10dSectionDatas.get(0).get_keyword_text().equals(select_keyword)) {
            //키워드 10과 같다면 이동할 인덱스는 14//
            move_position_index = 63;

            mainnews_recyclerview.smoothScrollToPosition(move_position_index); //리스트의 맨 처음으로 이동//
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.keyword_menu_item) {
            //키워드를 검색하기 위해서 다이얼로그 창 띄움//
            Intent intent = new Intent(getActivity(), KeywordListActivity.class);

            startActivityForResult(intent, RC_KEYWORD); //값을 전달받는다.//
        }

        if (item_id == R.id.search_menu_item) {
            Toast.makeText(getActivity(), "검색 화면으로 이동", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), SearchTabActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
