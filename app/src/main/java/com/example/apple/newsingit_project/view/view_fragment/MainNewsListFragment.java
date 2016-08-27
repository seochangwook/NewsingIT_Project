package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SearchTabActivity;
import com.example.apple.newsingit_project.data.view_data.Keyword1NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword1SectionData;
import com.example.apple.newsingit_project.data.view_data.Keyword2NewsContentData;
import com.example.apple.newsingit_project.data.view_data.Keyword2SectionData;
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
    NewsContent newsContent;
    KeywordSection keywordSection;

    NewsAdapter newsAdapter;

    private FamiliarRefreshRecyclerView mainnews_recyclerrefreshview;
    private FamiliarRecyclerView mainnews_recyclerview;

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

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        mainnews_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        mainnews_recyclerrefreshview.pullRefreshComplete();

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

                    }
                }, 1000);
            }
        });

        initDummyData();

        return view;
    }

    private void initDummyData() {
        //데이터 할당.//
        String keyword_data[] = {"iot", "라즈베리파이"};

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

        newsAdapter.setNewsData(keywordSection, newsContent);
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
            Intent intent = new Intent(getActivity(), KeywordListActivity.class);

            startActivity(intent);
        }

        if (item_id == R.id.search_menu_item) {
            Toast.makeText(getActivity(), "검색 화면으로 이동", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), SearchTabActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
