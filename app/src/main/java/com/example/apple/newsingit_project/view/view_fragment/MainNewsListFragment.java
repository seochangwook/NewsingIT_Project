package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SelectNewsDetailActivity;
import com.example.apple.newsingit_project.data.view_data.MainNewsData;
import com.example.apple.newsingit_project.widget.adapter.MainNewsAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainNewsListFragment extends Fragment {
    Button button;

    MainNewsAdapter mAdapter;
    MainNewsData mainNewsData;
    private FamiliarRecyclerView recyclerview;

    public MainNewsListFragment() //프래그먼트는 반드시 한개 이상의 생성자가 존재해야 한다.//
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_news_list, container, false);

        // button = (Button) view.findViewById(R.id.news_click_buton);

        setHasOptionsMenu(true); //메뉴를 적용한다.//

        //리사이클러 뷰//
        mainNewsData = new MainNewsData();

        recyclerview = (FamiliarRecyclerView) view.findViewById(R.id.main_rv_list);

        mAdapter = new MainNewsAdapter(getActivity());
        recyclerview.setAdapter(mAdapter);

        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelect = mainNewsData.mainNewsDataList.get(position).getNews().toString();

                Toast.makeText(getActivity(), userSelect + " 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);
                startActivity(intent);
            }
        });

        initDummyData();
        //리사이클러 뷰//

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SelectNewsDetailActivity.class);
//
//                startActivity(intent);
//            }
//        });
        return view;
    }

    private void initDummyData() {

        String nameList[] = {"메인 화면 뉴스 리스트 목록1", "메인 화면 뉴스 리스트 목록2", "메인 화면 뉴스 리스트 목록3"
                , "메인 화면 뉴스 리스트 목록4", "메인 화면 뉴스 리스트 목록5", "메인 화면 뉴스 리스트 목록6", "메인 화면 뉴스 리스트 목록7"};
        for (int i = 0; i < 7; i++) {
            MainNewsData new_mainNewsData = new MainNewsData();
            new_mainNewsData.news = nameList[i];
            mainNewsData.mainNewsDataList.add(new_mainNewsData);
        }

        mAdapter.setMainNewsData(mainNewsData);
    }
}
