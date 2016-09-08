package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    String query;

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
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            if (response.code() == 401) {
                Log.d("json data", "ERROR 401");
            } else {
                Gson gson = new Gson();

                SearchNewsListRequest searchNewsListRequest = gson.fromJson(responseData, SearchNewsListRequest.class);

                setData(searchNewsListRequest.getResults(), searchNewsListRequest.getResults().length);
            }
        }
    };

    public SearchNewsFragment() {
        // Required empty public constructor
    }

    private void getSearchNewsNetworkData(String query) {
        showpDialog();

        Log.d("search", "network");
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("search")
                .addQueryParameter("target", "1")
                .addQueryParameter("word", "")
                .addQueryParameter("page", "1")
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


        Bundle b = getArguments();
        query = b.getString("SEARCH_QUERY");

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
                        //   mAdapter.setSearchNewsData(searchNewsData);
                        Log.d("search", "pull refresh");


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
                        initSearchNewsDataList();

                        Log.d("search", "load more");
                        getSearchNewsNetworkData(query);


                        //   mAdapter.setSearchNewsData(searchNewsData);

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
        View emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_search_news_emptyview, null);
        recyclerView.setEmptyView(emptyview, true);
        recyclerView.setEmptyViewKeepShowHeadOrFooter(true);


        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchNewsData.searchNewsDataArrayList.get(position).getTitle().toString();
                int news_id = searchNewsData.searchNewsDataArrayList.get(position).getId();
                Toast.makeText(getActivity(), "제목 " + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 뉴스 콘텐츠 페이지로 이동//
                Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);

                intent.putExtra(NEWS_TITLE, userSelect);
                intent.putExtra(NEWS_ID, "" + news_id);

                startActivity(intent);

            }
        });


        if (query == null) {
            query = "";
        }
        // initDummyData(query); //더미//
        getSearchNewsNetworkData(query); //네트워크//

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

    private void initDummyData(String query) {

        if (query.equals("사드")) {
            int[] idList = {1, 2, 3, 4, 5};
            String[] titleList = {"사드", "사드 배치", "사드사드", "사드 반대", "사드 중국"};
            String[] contentList = {"VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                    , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?",
                    "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                    , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"};
            String[] dateList = {"2016.08.20 16:10", "2016.08.15 16:10", "2016.08.14 16:10", "2016.08.13 16:10"
                    , "2016.08.10 16:10", "2016.08.09 16:10"};

            for (int i = 0; i < 5; i++) {
                SearchNewsData new_searchNewsData = new SearchNewsData();
                new_searchNewsData.setId(idList[i]);
                new_searchNewsData.title = titleList[i];
                new_searchNewsData.author = contentList[i];
                new_searchNewsData.date = dateList[i];
                searchNewsData.searchNewsDataArrayList.add(new_searchNewsData);
            }
        } else {
            int[] idList = {1, 2, 3, 4, 5};
            String[] titleList = {"테스트", "사드 배치", "사드사드", "사드 반대", "사드 중국"};
            String[] contentList = {"VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                    , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?",
                    "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                    , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"};
            String[] dateList = {"2016.08.20 16:10", "2016.08.15 16:10", "2016.08.14 16:10", "2016.08.13 16:10"
                    , "2016.08.10 16:10", "2016.08.09 16:10"};

            for (int i = 0; i < 5; i++) {
                SearchNewsData new_searchNewsData = new SearchNewsData();
                new_searchNewsData.setId(idList[i]);
                new_searchNewsData.title = titleList[i];
                new_searchNewsData.author = contentList[i];
                new_searchNewsData.date = dateList[i];
                searchNewsData.searchNewsDataArrayList.add(new_searchNewsData);
            }
        }


        mAdapter.setSearchNewsData(searchNewsData);
    }

}
