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
import com.example.apple.newsingit_project.UserInfoActivity;
import com.example.apple.newsingit_project.data.json_data.searchuserlist.SearchUserRequest;
import com.example.apple.newsingit_project.data.json_data.searchuserlist.SearchUserRequestResults;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.SearchUserAdapter;
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
public class SearchUserFragment extends Fragment {
    //Button click_button;

    //인텐트 전달객체 변수 설정//
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";
    private static final int LOAD_MORE_TAG = 4;
    String query;

    FamiliarRefreshRecyclerView familiarRefreshRecyclerView;
    FamiliarRecyclerView recyclerView;

    SearchUserAdapter mAdapter;
    SearchUserData searchUserData;

    NetworkManager networkManager;
    private ProgressDialog pDialog;

    private Callback requestsearchusercallback = new Callback() {
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

            SearchUserRequest searchUserListRequest = gson.fromJson(responseData, SearchUserRequest.class);

            setData(searchUserListRequest.getResults(), searchUserListRequest.getResults().length);
        }
    };

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_user_layout, container, false);

        searchUserData = new SearchUserData();

        Bundle b = getArguments();
        query = b.getString("SEARCH_QUERY");

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        familiarRefreshRecyclerView = (FamiliarRefreshRecyclerView) view.findViewById(R.id.search_user_rv_list);
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
                        //  mAdapter.setSearchUserData(searchUserData);


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
                        initSearchUserDataList();
                        get_User_search_Data(query);

                        //   mAdapter.setSearchUserData(searchUserData);

                    }
                }, 1000);
            }
        });


        recyclerView = familiarRefreshRecyclerView.getFamiliarRecyclerView();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        mAdapter = new SearchUserAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        /** EmptyView 설정 **/
        View emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_search_emptyuser, null);

        recyclerView.setEmptyView(emptyview, true);
        recyclerView.setEmptyViewKeepShowHeadOrFooter(true);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchUserData.searchUserDataArrayList.get(position).getName().toString();
                // Toast.makeText(getActivity(), "" + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 유저의 마이 페이지로 이동//
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);

                intent.putExtra(USER_ID, "" + searchUserData.searchUserDataArrayList.get(position).getId());
                intent.putExtra(USER_NAME, searchUserData.searchUserDataArrayList.get(position).getName());

                startActivity(intent);

            }
        });

        if (query == null) {
            query = "";
        }
        // initDummyData(query); //더미//

        /** 데이터 셋팅 **/
        get_User_search_Data(query);

        return view;
    }

    private void initSearchUserDataList() {
        searchUserData.searchUserDataArrayList.clear();
        mAdapter.initSearchUserData(searchUserData);
    }

    public void get_User_search_Data(String query) {
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.server_domain));
        builder.addPathSegment("search");

        //전달할 파라미터 값을 생성//
        builder.addQueryParameter("target", "" + 2);
        builder.addQueryParameter("word", "" + query);
        builder.addQueryParameter("page", "" + 1);
        builder.addQueryParameter("count", "" + 20);

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestsearchusercallback);
    }

    public void setData(final SearchUserRequestResults user_list_data[], final int user_list_data_length) {
        //실제 데이터에 네트워크로 받아온 값을 할당.//
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showpDialog();

                    List<SearchUserRequestResults> searchUserRequestResultsList = new ArrayList<>();

                    searchUserRequestResultsList.addAll(Arrays.asList(user_list_data));

                    /*for(int i=0; i<user_list_data_length; i++)
                    {
                        Log.d("data message", ""+searchUserRequestResultsList.get(i).getId());
                        Log.d("data message", searchUserRequestResultsList.get(i).getAboutme());
                        Log.d("data message", searchUserRequestResultsList.get(i).getName());
                        Log.d("data message", searchUserRequestResultsList.get(i).getPf_url());
                    }*/

                    //실제 값에 할당//
                    for (int i = 0; i < user_list_data_length; i++) {
                        SearchUserData new_searchUserData = new SearchUserData();

                        new_searchUserData.setId(searchUserRequestResultsList.get(i).getId());
                        new_searchUserData.setName(searchUserRequestResultsList.get(i).getName());
                        new_searchUserData.setAboutMe(searchUserRequestResultsList.get(i).getAboutme());
                        new_searchUserData.setFlag(searchUserRequestResultsList.get(i).getFlag());
                        //new_searchUserData.set_User_imgUrl(searchUserRequestResultsList.get(i).getPf_url());
                        new_searchUserData.set_User_imgUrl("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4");

                        searchUserData.searchUserDataArrayList.add(new_searchUserData);
                    }

                    mAdapter.setSearchUserData(searchUserData); //어댑터에 할당//

                    hidepDialog();
                }
            });
        }
    }

    private void initDummyData(String query) {

        if (query.equals("서창욱")) {
            int[] idList = {1, 2, 3, 4, 5, 6, 7};
            String[] pfList = {"http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp"};
            boolean[] flagList = {true, true, true, false, false, false, true};

            String[] nameList = {"서창욱", "창욱서", "서씨", "이혜람", "신미은", "김예진", "이임수"};
            String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
                    , "만두만두", "=v="};

            for (int i = 0; i < 7; i++) {
                SearchUserData new_searchUserData = new SearchUserData();
                new_searchUserData.setId(idList[i]);
                new_searchUserData.setFlag(flagList[i]);
                new_searchUserData.set_User_imgUrl(pfList[i]);
                new_searchUserData.name = nameList[i];
                new_searchUserData.setAboutMe(introList[i]);
                searchUserData.searchUserDataArrayList.add(new_searchUserData);
            }

        } else if (query.equals("임지수")) {
            int[] idList = {1, 2, 3, 4, 5, 6, 7};
            String[] pfList = {"http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp"};
            boolean[] flagList = {true, true, true, false, false, false, true};

            String[] nameList = {"임지수", "지수", "임씨", "이혜람", "신미은", "김예진", "이임수"};
            String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
                    , "만두만두", "=v="};

            for (int i = 0; i < 7; i++) {
                SearchUserData new_searchUserData = new SearchUserData();
                new_searchUserData.setId(idList[i]);
                new_searchUserData.setFlag(flagList[i]);
                new_searchUserData.set_User_imgUrl(pfList[i]);
                new_searchUserData.name = nameList[i];
                new_searchUserData.setAboutMe(introList[i]);
                searchUserData.searchUserDataArrayList.add(new_searchUserData);
            }

        } else {
            int[] idList = {1, 2, 3, 4, 5, 6, 7};
            String[] pfList = {"http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp", "http://.../images/user/id/pf_picture.bmp"
                    , "http://.../images/user/id/pf_picture.bmp"};
            boolean[] flagList = {true, true, true, false, false, false, true};

            String[] nameList = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
            String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
                    , "만두만두", "=v="};

            for (int i = 0; i < 7; i++) {
                SearchUserData new_searchUserData = new SearchUserData();
                new_searchUserData.setId(idList[i]);
                new_searchUserData.setFlag(flagList[i]);
                new_searchUserData.set_User_imgUrl(pfList[i]);
                new_searchUserData.name = nameList[i];
                new_searchUserData.setAboutMe(introList[i]);
                searchUserData.searchUserDataArrayList.add(new_searchUserData);
            }
        }

        mAdapter.setSearchUserData(searchUserData);
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
