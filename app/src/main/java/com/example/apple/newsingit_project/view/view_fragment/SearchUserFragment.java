package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserInfoActivity;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.widget.adapter.SearchUserAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends Fragment {
    //Button click_button;

    FamiliarRecyclerView recyclerView;
    SearchUserAdapter mAdapter;
    SearchUserData searchUserData;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_user_layout, container, false);

        searchUserData = new SearchUserData();
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
                intent.putExtra("name", userSelect);
                startActivity(intent);

            }
        });

        initDummyData();

        return view;
    }

    private void initDummyData() {
        String[] nameList = {"서창욱", "임지수", "정다솜", "이혜람", "신미은", "김예진", "이임수"};

        for (int i = 0; i < 7; i++) {
            SearchUserData new_searchUserData = new SearchUserData();
            new_searchUserData.name = nameList[i];
            searchUserData.searchUserDataArrayList.add(new_searchUserData);
        }
        mAdapter.setSearchUserData(searchUserData);
    }

}
