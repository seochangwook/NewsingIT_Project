package com.example.apple.newsingit_project.view.view_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainNewsListFragment extends Fragment {


    public MainNewsListFragment() //프래그먼트는 반드시 한개 이상의 생성자가 존재해야 한다.//
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_news_list, container, false);

        setHasOptionsMenu(true); //메뉴를 적용한다.//

        return view;
    }
}
