package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserScrapContentListActivity;
import com.example.apple.newsingit_project.data.json_data.searchtaglist.SearchTagListRequest;
import com.example.apple.newsingit_project.data.json_data.searchtaglist.SearchTagListRequestResults;
import com.example.apple.newsingit_project.data.view_data.SearchTagData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.SearchTagAdapter;
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
public class SearchTagFragment extends Fragment {
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_TAGSEARCH_FLAG = "KEY_TAGSEARCH_FLAG";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";

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

            Gson gson = new Gson();

            SearchTagListRequest searchTagListRequest = gson.fromJson(responseData, SearchTagListRequest.class);

            setData(searchTagListRequest.getResults(), searchTagListRequest.getResults().length);
        }
    };

    public SearchTagFragment() {
        // Required empty public constructor
    }

    private void getSearchTagNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.server_domain))
                .addPathSegment("search")
                .addQueryParameter("target", "3")
                .addQueryParameter("word", "단어")
                .addQueryParameter("page", "1")
                .addQueryParameter("count", "10");

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

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.search_tag_rv_list);

        mAdapter = new SearchTagAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        /** EmptyView 설정 **/
        View emptyview = getActivity().getLayoutInflater().inflate(R.layout.view_searchtag_emptyview, null);

        recyclerView.setEmptyView(emptyview, true);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchTagData.searchTagDataList.get(position).getTag().toString();
                Toast.makeText(getActivity(), "# " + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 태그를 가진 스크랩 목록 페이지로 이동//
                Intent intent = new Intent(getActivity(), UserScrapContentListActivity.class);

                intent.putExtra(KEY_FOLDER_NAME, userSelect); //태그명을 전달.//
                intent.putExtra(KEY_USER_IDENTIFY_FLAG, "1"); //검색은 다른 사용자의 내용들을 보는것이니 외부사용자로 간다.//
                intent.putExtra(KEY_TAGSEARCH_FLAG, "TAG"); //태그로 검색한다는 플래그.//

                startActivity(intent);

            }
        });

        getSearchTagNetworkData();

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
//
//        String[] tagList = {"사드", "사드 배치", "사드사드", "사드 반대", "사드 중국"};
//        String[] countList = {"1,000개", "500개", "1,111개", "4,235,32개", "4,234개"};
//
//        for (int i = 0; i < 5; i++) {
//            SearchTagData new_searchTagData = new SearchTagData();
//            new_searchTagData.tag = "# "+tagList[i];
//            new_searchTagData.count = countList[i];
//            searchTagData.searchTagDataList.add(new_searchTagData);
//        }
//        mAdapter.setSearchTagData(searchTagData);
//    }

}
