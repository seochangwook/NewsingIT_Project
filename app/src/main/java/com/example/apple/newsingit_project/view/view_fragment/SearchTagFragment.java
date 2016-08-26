package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserScrapContentListActivity;
import com.example.apple.newsingit_project.data.view_data.SearchTagData;
import com.example.apple.newsingit_project.widget.adapter.SearchTagAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTagFragment extends Fragment {

    FamiliarRecyclerView recyclerView;
    SearchTagAdapter mAdapter;
    SearchTagData searchTagData;

    public SearchTagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_tag_layout, container, false);

        searchTagData = new SearchTagData();
        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.search_tag_rv_list);

        mAdapter = new SearchTagAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);


        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {

                String userSelect = searchTagData.searchTagDataList.get(position).getTag().toString();
                Toast.makeText(getActivity(), "# " + userSelect, Toast.LENGTH_SHORT).show();

                //선택한 태그를 가진 스크랩 목록 페이지로 이동//
                Intent intent = new Intent(getActivity(), UserScrapContentListActivity.class);
                intent.putExtra("KEY_FOLDER_NAME", userSelect);
                intent.putExtra("KEY_USER_IDENTIFY_FLAG", "1"); //검색은 다른 사용자의 내용들을 보는것이니 외부사용자로 간다.//

                startActivity(intent);

            }
        });

        initDummyData();

        return view;
    }

    private void initDummyData() {

        String[] tagList = {"사드", "사드 배치", "사드사드", "사드 반대", "사드 중국"};
        String[] countList = {"1,000개", "500개", "1,111개", "4,235,32개", "4,234개"};

        for (int i = 0; i < 5; i++) {
            SearchTagData new_searchTagData = new SearchTagData();
            new_searchTagData.tag = "# "+tagList[i];
            new_searchTagData.count = countList[i];
            searchTagData.searchTagDataList.add(new_searchTagData);
        }
        mAdapter.setSearchTagData(searchTagData);
    }

}
