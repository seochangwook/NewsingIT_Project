package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserInfoActivity;
import com.example.apple.newsingit_project.data.json_data.searchuserlist.SearchUserListRequest;
import com.example.apple.newsingit_project.data.json_data.searchuserlist.SearchUserListRequestResults;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.SearchUserAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
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

    FamiliarRecyclerView recyclerView;
    SearchUserAdapter mAdapter;
    SearchUserData searchUserData;
    NetworkManager networkManager;
    private ProgressDialog pDialog;
    private Callback requestSearchUserListCallback = new Callback() {
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

            SearchUserListRequest searchUserListRequest = gson.fromJson(responseData, SearchUserListRequest.class);

            setData(searchUserListRequest.getResults(), searchUserListRequest.getResults().length);
        }
    };

    public SearchUserFragment() {
        // Required empty public constructor
    }

    private void getSearchUserNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com")
                .addPathSegment("search")
                .addQueryParameter("target", "2")
                .addQueryParameter("word", "단어")
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "10");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestSearchUserListCallback);

        hidepDialog();
    }

    private void setData(final SearchUserListRequestResults[] results, final int size) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<SearchUserListRequestResults> searchUserListRequestResults = new ArrayList<>();
                    searchUserListRequestResults.addAll(Arrays.asList(results));

                    for (int i = 0; i < size; i++) {
                        SearchUserData newSearchUserData = new SearchUserData();
                        newSearchUserData.setId(searchUserListRequestResults.get(i).getId());
                        newSearchUserData.setProfileUrl(searchUserListRequestResults.get(i).getPf_url());
                        newSearchUserData.setAboutMe(searchUserListRequestResults.get(i).getAboutme());
                        newSearchUserData.setName(searchUserListRequestResults.get(i).getName());

                        searchUserData.searchUserDataArrayList.add(newSearchUserData);
                    }
                    mAdapter.setSearchUserData(searchUserData);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_user_layout, container, false);

        searchUserData = new SearchUserData();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.search_user_rv_list);

        mAdapter = new SearchUserAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

//        click_button = (Button) view.findViewById(R.id.user_mypage_click);
//
//        click_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
//
//                startActivity(intent);
//            }
//        });

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchUserData.searchUserDataArrayList.get(position).getName().toString();
                // Toast.makeText(getActivity(), "" + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 유저의 마이 페이지로 이동//
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("USER_NAME", userSelect);
                startActivity(intent);

            }
        });

        getSearchUserNetworkData();
        //initDummyData();

        return view;
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

//
//    private void initDummyData() {
//        String[] nameList = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};
//        String[] introList = {"저는 코딩이 취미입니다", "반갑습니다", "ㅇvㅇ", "^ㅇ^", "술x"
//                                ,"만두만두", "=v="};
//
//        for (int i = 0; i < 7; i++) {
//            SearchUserData new_searchUserData = new SearchUserData();
//            new_searchUserData.name = nameList[i];
//            new_searchUserData.aboutMe = introList[i];
//            searchUserData.searchUserDataArrayList.add(new_searchUserData);
//        }
//        mAdapter.setSearchUserData(searchUserData);
//    }

}
