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

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserScrapContentListActivity;
import com.example.apple.newsingit_project.data.json_data.searchtaglist.SearchTagListRequest;
import com.example.apple.newsingit_project.data.json_data.searchtaglist.SearchTagListRequestResults;
import com.example.apple.newsingit_project.data.view_data.SearchTagData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.SearchTagAdapter;
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
public class SearchTagFragment extends Fragment {
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_TAGSEARCH_FLAG = "KEY_TAGSEARCH_FLAG";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";
    private static final int LOAD_MORE_TAG = 6;
    private static final String SEARCH_QUERY = "SEARCH_QUERY";

    String query;

    FamiliarRefreshRecyclerView familiarRefreshRecyclerView;
    FamiliarRecyclerView recyclerView;
    SearchTagAdapter mAdapter;

    SearchTagData searchTagData;

    NetworkManager networkManager;

    private ProgressDialog pDialog;

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

            if (response.code() == 401) {
                Log.d("json data", "ERROR 401");
            } else {
                Gson gson = new Gson();

                SearchTagListRequest searchTagListRequest = gson.fromJson(responseData, SearchTagListRequest.class);

                setData(searchTagListRequest.getResults(), searchTagListRequest.getResults().length);
            }
        }
    };

    public SearchTagFragment() {
        // Required empty public constructor
    }

    private void getSearchTagNetworkData(String query) {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("search")
                .addQueryParameter("target", "3")
                .addQueryParameter("word", query)
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "20");

        //  Log.d("searchQuery",searchQuery);

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestSearchTagListCallback);

        hidepDialog();
    }


    private void setData(final SearchTagListRequestResults[] results, final int size) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<SearchTagListRequestResults> searchTagListRequestResults = new ArrayList<>();
                    searchTagListRequestResults.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        SearchTagData newSearchTagData = new SearchTagData();

                        newSearchTagData.setId(searchTagListRequestResults.get(i).getId());
                        newSearchTagData.setTag(searchTagListRequestResults.get(i).getTag());
                        //newSearchTagData.setScrap_count(""+searchTagListRequestResults.get(i).getScrap_count());
                        newSearchTagData.setScrap_count("1");

                        searchTagData.searchTagDataList.add(newSearchTagData);
                    }

                    mAdapter.setSearchTagData(searchTagData);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_tag_layout, container, false);

        searchTagData = new SearchTagData();


        Bundle b = getArguments();
        query = b.getString("SEARCH_QUERY");

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        familiarRefreshRecyclerView = (FamiliarRefreshRecyclerView) view.findViewById(R.id.search_tag_rv_list);
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

                        initSearchTagDataList();

                        getSearchTagNetworkData(query);
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

                        initSearchTagDataList();

                        getSearchTagNetworkData(query);


                    }
                }, 1000);
            }
        });

        recyclerView = familiarRefreshRecyclerView.getFamiliarRecyclerView();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new SearchTagAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        /** EmptyView 설정 **/
        View emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_searchtag_emptyview, null);

        recyclerView.setEmptyView(emptyview, true);
        recyclerView.setEmptyViewKeepShowHeadOrFooter(true);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchTagData.searchTagDataList.get(position).getTag().toString();

                //선택한 태그를 가진 스크랩 목록 페이지로 이동//
                Intent intent = new Intent(getActivity(), UserScrapContentListActivity.class);

                intent.putExtra(KEY_FOLDER_NAME, userSelect); //태그명을 전달.//
                intent.putExtra(KEY_USER_IDENTIFY_FLAG, "1"); //검색은 다른 사용자의 내용들을 보는것이니 외부사용자로 간다.//
                intent.putExtra(KEY_TAGSEARCH_FLAG, "TAG"); //태그로 검색한다는 플래그.//

                startActivity(intent);

            }
        });


        if (query == null) {
            query = "";
        }

        getSearchTagNetworkData(query); //네트워크//

        return view;
    }

    private void initSearchTagDataList() {
        searchTagData.searchTagDataList.clear();
        mAdapter.initSearchTagData(searchTagData);
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
