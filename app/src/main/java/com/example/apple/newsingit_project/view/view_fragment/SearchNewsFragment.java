package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SelectNewsDetailActivity;
import com.example.apple.newsingit_project.data.view_data.SearchNewsData;
import com.example.apple.newsingit_project.widget.adapter.SearchNewsAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchNewsFragment extends Fragment {
    FamiliarRecyclerView recyclerView;
    SearchNewsAdapter mAdapter;
    SearchNewsData searchNewsData;

    public SearchNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_news_layout, container, false);

        searchNewsData = new SearchNewsData();
        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.search_news_rv_list);

        mAdapter = new SearchNewsAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchNewsData.searchNewsDataArrayList.get(position).getTitle().toString();
                Toast.makeText(getActivity(), "제목 " + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 뉴스 콘텐츠 페이지로 이동//
                Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);
                intent.putExtra("NEWS_TITLE", userSelect);
                startActivity(intent);

            }
        });

        initDummyData();

        return view;
    }

    private void initDummyData() {

        String[] titleList = {"사드", "사드 배치", "사드사드", "사드 반대", "사드 중국"};
        String[] contentList = {"VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?",
                "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?", "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"
                , "VR-AR 모방작 안돼...새로운 장르의 국산 게임들은?"};
        String[] dateList = {"2016.08.20 16:10","2016.08.15 16:10","2016.08.14 16:10","2016.08.13 16:10"
                                ,"2016.08.10 16:10","2016.08.09 16:10"};

        for (int i = 0; i < 5; i++) {
            SearchNewsData new_searchNewsData = new SearchNewsData();
            new_searchNewsData.title = titleList[i];
            new_searchNewsData.content = contentList[i];
            new_searchNewsData.date = dateList[i];
            searchNewsData.searchNewsDataArrayList.add(new_searchNewsData);
        }
        mAdapter.setSearchNewsData(searchNewsData);
    }

}
