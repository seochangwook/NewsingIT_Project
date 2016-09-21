package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SelectNewsDetailActivity;
import com.example.apple.newsingit_project.data.json_data.searchnewslist.SearchNewsListRequest;
import com.example.apple.newsingit_project.data.json_data.searchnewslist.SearchNewsListRequestResults;
import com.example.apple.newsingit_project.data.view_data.SearchNewsData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.SearchNewsAdapter;
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
public class SearchNewsFragment extends Fragment {
    private static final String NEWS_ID = "NEWS_ID";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    private static final int LOAD_MORE_TAG = 5;
    private static final String FLAG = "FLAG";
    private static final String TAB_FLAG = "TAB_FLAG"; //탭을 구분지어주기 위한 플래그//
    private static final String SEARCH_QUERY = "SEARCH_QUERY";

    static int pageCount = 1;
    static boolean emptyViewFlag = true;

    String query;
    String tab_flag;

    FamiliarRefreshRecyclerView familiarRefreshRecyclerView;
    FamiliarRecyclerView recyclerView;

    SearchNewsAdapter mAdapter;
    SearchNewsData searchNewsData;

    NetworkManager networkManager;
    private ProgressDialog pDialog;
    private Callback requestSearchNewsListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            if (response.code() == 401) {
                Log.d("json data", "ERROR 401");
                emptyViewFlag = false;

            } else {
                if (tab_flag.equals("NEWS_TAB")) {
                    Gson gson = new Gson();

                    SearchNewsListRequest searchNewsListRequest = gson.fromJson(responseData, SearchNewsListRequest.class);

                    if (searchNewsListRequest.getResults().length == 0) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                    alertDialog
                                            .setTitle("Newsing Info")
                                            .setMessage("뉴스 검색결과가 존재하지 않습니다.")
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
                    } else {
                        setData(searchNewsListRequest.getResults(), searchNewsListRequest.getResults().length);

                        Log.d("json control:", "SearchNewsTab");
                    }

                    emptyViewFlag = true;
                }
            }
        }
    };

    public SearchNewsFragment() {
        // Required empty public constructor
    }

    private void getSearchNewsNetworkData(String query) {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("search")
                .addQueryParameter("target", "1")
                .addQueryParameter("word", query)
                .addQueryParameter("page", "" + pageCount)
                .addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestSearchNewsListCallback);

        hidepDialog();
    }

    private void setData(final SearchNewsListRequestResults[] results, final int size) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<SearchNewsListRequestResults> searchNewsListRequestResults = new ArrayList<>();
                    searchNewsListRequestResults.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {

                        SearchNewsData newSearchNewsData = new SearchNewsData();
                        newSearchNewsData.setId(searchNewsListRequestResults.get(i).getId());
                        newSearchNewsData.setTitle(searchNewsListRequestResults.get(i).getTitle());
                        newSearchNewsData.setAuthor(searchNewsListRequestResults.get(i).getAuthor());
                        newSearchNewsData.setDate(searchNewsListRequestResults.get(i).getNtime());

                        searchNewsData.searchNewsDataArrayList.add(newSearchNewsData);

                    }

                    mAdapter.setSearchNewsData(searchNewsData);
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_news_layout, container, false);

        pageCount = 1;

        Log.d("pageCount", "" + pageCount);

        Bundle b = getArguments();
        //값을 전달받는다.//
        query = b.getString(SEARCH_QUERY);
        tab_flag = b.getString(TAB_FLAG);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        searchNewsData = new SearchNewsData();

        familiarRefreshRecyclerView = (FamiliarRefreshRecyclerView) view.findViewById(R.id.search_news_rv_list);
        familiarRefreshRecyclerView.setId(android.R.id.list);
        familiarRefreshRecyclerView.setLoadMoreView(new LoadMoreView(getActivity(), LOAD_MORE_TAG));
        familiarRefreshRecyclerView.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        familiarRefreshRecyclerView.setLoadMoreEnabled(true);


        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        familiarRefreshRecyclerView.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        familiarRefreshRecyclerView.pullRefreshComplete();

                        initSearchNewsDataList();

                        getSearchNewsNetworkData(query);

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

                        pageCount += 1; //'더보기'의 기능을 구현하기 위해서 설정//

                        getSearchNewsNetworkData(query);
                    }
                }, 1000);
            }
        });

        recyclerView = familiarRefreshRecyclerView.getFamiliarRecyclerView();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new SearchNewsAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        /** EmptyView 설정 **/
        View emptyView;


        //검색 결과가 없을 때 & 처음 검색창 실행 시 그리고 네트워크 오류 시 empty view 구분
        if (emptyViewFlag) {
            emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_search_empty, null);
            recyclerView.setEmptyView(emptyView, true);
        } else { //false - 네트워크 오류
            emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_search_news_emptyview, null);
            recyclerView.setEmptyView(emptyView, true);
        }
        recyclerView.setEmptyViewKeepShowHeadOrFooter(true);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchNewsData.searchNewsDataArrayList.get(position).getTitle().toString();
                int news_id = searchNewsData.searchNewsDataArrayList.get(position).getId();

                //선택한 뉴스 콘텐츠 페이지로 이동//
                Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                intent.putExtra(NEWS_TITLE, userSelect);
                intent.putExtra(NEWS_ID, "" + news_id);
                intent.putExtra(FLAG, "0");

                startActivity(intent);

            }
        });


        if (query == null) {
            query = "";
        }
        if (query.length() >= 2) {
            getSearchNewsNetworkData(query); //네트워크//
        }

        return view;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initSearchNewsDataList() {
        searchNewsData.searchNewsDataArrayList.clear();
        mAdapter.initSearchNewsData(searchNewsData);
    }
}
