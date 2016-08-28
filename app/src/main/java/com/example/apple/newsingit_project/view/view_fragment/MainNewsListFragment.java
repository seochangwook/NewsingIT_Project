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
import com.example.apple.newsingit_project.data.view_data.Keyword1NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword1SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword2NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword2SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword3NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword3SectionData;
import com.example.apple.newsingit_project.data.view_data.KeywordSection;
import com.example.apple.newsingit_project.data.view_data.NewsContent;
import com.example.apple.newsingit_project.dialog.KeywordListActivity;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.NewsAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainNewsListFragment extends Fragment {
    /**
     * Data trans
     **/
    private static final int RC_KEYWORD = 1000;
    private static final String KEY_KEYWORD = "KEY_KEYWORD";
    /**
     * RecyclerView의 데이터와 어댑터 관련 변수
     **/
    NewsContent newsContent;
    KeywordSection keywordSection;
    NewsAdapter newsAdapter;
    /**
     * RecyclerView Touch이벤트 관련(Scroll 부분)
     **/
    FloatingActionButton topup_button;
    float startYPosition = 0; //기본적으로 스크롤은 Y축을 기준으로 계산.//
    float endYPosition = 0;
    boolean firstDragFlag = true;
    boolean motionFlag = true;
    boolean dragFlag = false; //현재 터치가 드래그인지 먼저 확인//
    private FamiliarRefreshRecyclerView mainnews_recyclerrefreshview;
    private FamiliarRecyclerView mainnews_recyclerview;
    /**
     * 기타 로딩 기능
     **/
    private ProgressDialog pDialog;

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
        mainnews_recyclerrefreshview.setLoadMoreView(new LoadMoreView(getActivity()));
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

        /** EmptyView를 설정 **/
        View emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_mainnews_empty_layout, null);
        mainnews_recyclerview.setEmptyView(emptyview, true);

        /** 뉴스 리스트 선택 이벤트 설정 **/
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

                    Toast.makeText(getActivity(), "click news id : " + news_id + "/news title : " + news_title, Toast.LENGTH_SHORT).show();
                }

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

                    Toast.makeText(getActivity(), "click news id : " + news_id + "/news title : " + news_title, Toast.LENGTH_SHORT).show();
                }

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

                    Toast.makeText(getActivity(), "click news id : " + news_id + "/news title : " + news_title, Toast.LENGTH_SHORT).show();
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

                        //기존 데이터를 전부 지운다.//
                        Data_Init();

                        //데이터를 다시 초기화//
                        initDummyData();

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
                        initDummyData();

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
        initDummyData();

        return view;
    }

    public void Data_Init() {
        //모든 배열을 초기화.//
        keywordSection.keyword1SectionDatas.clear();
        keywordSection.keyword2SectionDatas.clear();
        keywordSection.keyword3SectionDatas.clear();

        newsContent.keyword_1_news_content.clear();
        newsContent.keyword_2_news_content.clear();
        newsContent.keyword_3_news_content.clear();

        //초기화된 배열을 다시 리사이클뷰에 적용(실제 데이터의 관리는 어댑터에서 한다.)//
        newsAdapter.init_Data(keywordSection, newsContent);
    }

    private void initDummyData() {
        showpDialog();

        //데이터 할당.//
        //해당 부분을 네트워크로 데이터를 가져온다.//
        String keyword_data[] = {"iot", "라즈베리파이", "안드로이드"};

        //첫번째 키워드 데이터//
        Keyword1SectionData new_keyword_section_1 = new Keyword1SectionData();

        new_keyword_section_1.set_keyword_text(keyword_data[0]);

        keywordSection.keyword1SectionDatas.add(new_keyword_section_1); //키워드 할당.//

        Keyword1NewsContentData new_news1content_1 = new Keyword1NewsContentData();

        new_news1content_1.set_news_content_id(1);
        new_news1content_1.set_news_author("한국일보");
        new_news1content_1.set_news_title("센서 시장이 활성화");
        new_news1content_1.set_news_write_date("2016-08-27");
        new_news1content_1.set_news_content("최근 iot가 성장세를 보이며 센서 시장이 늘고 있고 사람들 또한 구매를 많이 합니다.");
        new_news1content_1.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_1_news_content.add(new_news1content_1);

        Keyword1NewsContentData new_news1content_2 = new Keyword1NewsContentData();

        new_news1content_2.set_news_content_id(2);
        new_news1content_2.set_news_author("조선일보");
        new_news1content_2.set_news_title("삼성전자 새로운 센서 개발");
        new_news1content_2.set_news_write_date("2016-08-27");
        new_news1content_2.set_news_content("삼성전자가 최근 새로운 센서 개발에 들어가면서 점차 iot시장에서 영향력을 발휘하고 있습니다.");
        new_news1content_2.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_1_news_content.add(new_news1content_2);

        Keyword1NewsContentData new_news1content_3 = new Keyword1NewsContentData();

        new_news1content_3.set_news_content_id(3);
        new_news1content_3.set_news_author("디지털통신");
        new_news1content_3.set_news_title("IoT 학교에서도 열풍");
        new_news1content_3.set_news_write_date("2016-08-27");
        new_news1content_3.set_news_content("최근 인근 여러 초등학교에서 IoT교육이 열풍이 불고 있습니다. 아두이노를 활용한 창작의 재미도 느낄 수 있습니다.");
        new_news1content_3.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_1_news_content.add(new_news1content_3);

        //두번째 키워드 데이터//
        Keyword2SectionData new_keyword_section_2 = new Keyword2SectionData();

        new_keyword_section_2.set_keyword_text(keyword_data[1]);

        keywordSection.keyword2SectionDatas.add(new_keyword_section_2);

        Keyword2NewsContentData new_news2content_1 = new Keyword2NewsContentData();

        new_news2content_1.set_news_content_id(4);
        new_news2content_1.set_news_author("동아일보");
        new_news2content_1.set_news_title("SBC의 새로운 매커니즘");
        new_news2content_1.set_news_write_date("2016-08-27");
        new_news2content_1.set_news_content("라즈베리파이를 기반으로 여러 연구단체에서는 더 성능이 좋은 SBC로 개발할려고 노력하고 있습니다.");
        new_news2content_1.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_2_news_content.add(new_news2content_1);

        Keyword2NewsContentData new_news2content_2 = new Keyword2NewsContentData();

        new_news2content_2.set_news_content_id(5);
        new_news2content_2.set_news_author("조선일보");
        new_news2content_2.set_news_title("라즈베리파이 새로운 모델 출시");
        new_news2content_2.set_news_write_date("2016-08-27");
        new_news2content_2.set_news_content("최근 라즈베리파이에 새로운 모델이 나왔습니다.");
        new_news2content_2.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_2_news_content.add(new_news2content_2);

        Keyword2NewsContentData new_news2content_3 = new Keyword2NewsContentData();

        new_news2content_3.set_news_content_id(6);
        new_news2content_3.set_news_author("디지털통신");
        new_news2content_3.set_news_title("라즈베리파이 기반에서 C언어 코딩수업");
        new_news2content_3.set_news_write_date("2016-08-27");
        new_news2content_3.set_news_content("C언어를 이용한 라즈베리파이 기반에서 채팅 프로그램을 개발할 수 있습니다.");
        new_news2content_3.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_2_news_content.add(new_news2content_3);

        //세번째 키워드 데이터//
        Keyword3SectionData new_keyword_section_3 = new Keyword3SectionData();

        new_keyword_section_3.set_keyword_text(keyword_data[2]);

        keywordSection.keyword3SectionDatas.add(new_keyword_section_3);

        Keyword3NewsContentData new_news3content_1 = new Keyword3NewsContentData();

        new_news3content_1.set_news_content_id(7);
        new_news3content_1.set_news_author("디지털타임");
        new_news3content_1.set_news_title("안드로이드 새로운 버전 출시");
        new_news3content_1.set_news_write_date("2016-08-28");
        new_news3content_1.set_news_content("안드로이드 새로운 버전인 NUGA 출시");
        new_news3content_1.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_3_news_content.add(new_news3content_1);

        Keyword3NewsContentData new_news3content_2 = new Keyword3NewsContentData();

        new_news3content_2.set_news_content_id(8);
        new_news3content_2.set_news_author("조선일보");
        new_news3content_2.set_news_title("우리도 앱을 만들어요!!");
        new_news3content_2.set_news_write_date("2016-08-27");
        new_news3content_2.set_news_content("최근 초등학교에선 앱인벤터를 이용한 안드로이드 앱을 개발하는 수업을 진행하고 있습니다.");
        new_news3content_2.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_3_news_content.add(new_news3content_2);

        Keyword3NewsContentData new_news3content_3 = new Keyword3NewsContentData();

        new_news3content_3.set_news_content_id(9);
        new_news3content_3.set_news_author("동아일보");
        new_news3content_3.set_news_title("Material Design에 대해서");
        new_news3content_3.set_news_write_date("2016-08-27");
        new_news3content_3.set_news_content("최근 안드로이드 개발팀에서 내놓은 Material Design에 대한 소개책이 인기입니다.");
        new_news3content_3.set_news_thumbnail_Url("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

        newsContent.keyword_3_news_content.add(new_news3content_3);

        newsAdapter.setNewsData(keywordSection, newsContent);

        hidepDialog();
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
