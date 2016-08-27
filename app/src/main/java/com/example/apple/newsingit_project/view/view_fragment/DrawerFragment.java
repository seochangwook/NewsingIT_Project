package com.example.apple.newsingit_project.view.view_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.apple.newsingit_project.LoginActivity;
import com.example.apple.newsingit_project.NoticeActivity;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.DrawerChild;
import com.example.apple.newsingit_project.data.view_data.DrawerGroup;
import com.example.apple.newsingit_project.widget.adapter.DrawerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    public static final String MENU_MY_PAGE = "마이페이지";
    public static final String MENU_SET_ALARM = "알림 설정";
    public static final String MENU_LOGOUT = "로그아웃";
    public static final String MENU_NOTICE = "공지사항";

    public static final String CHILD_NEW_SCRAP="새 스크랩";
    public static final String CHILD_LIKE_MY_SCRAP ="내 스크랩 좋아요";
    public static final String CHILD_FOLLOW_MY_PAGE = "마이페이지 팔로우";

    DrawerGroup[] menuList = {

            new DrawerGroup(MENU_MY_PAGE),
            new DrawerGroup(MENU_SET_ALARM,
                    new DrawerChild(CHILD_NEW_SCRAP),
                    new DrawerChild(CHILD_LIKE_MY_SCRAP),
                    new DrawerChild(CHILD_FOLLOW_MY_PAGE)),
            new DrawerGroup(MENU_LOGOUT),
            new DrawerGroup(MENU_NOTICE)
    };

    ExpandableListView expandableListView;
    DrawerAdapter mAdapter;

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new DrawerAdapter(menuList, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        View drawerHeader = inflater.inflate(R.layout.view_drawer_header, container, false);

        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        expandableListView.addHeaderView(drawerHeader);
        expandableListView.setAdapter(mAdapter);

        //expandablelistview의 group indicator를 cusotom해주기 위해 기본 indicator를 제거한다//
        expandableListView.setGroupIndicator(null);


        //그룹을 닫았을 때 이벤트//
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupposition) {
                //Toast.makeText(getActivity(), "닫혀라 "+groupposition ,Toast.LENGTH_SHORT ).show();
            }
        });

        //그룹을 펼쳤을 때 이벤트//
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupposition) {
                //  Toast.makeText(getActivity(), "공지사항 화면 이동" + groupposition, Toast.LENGTH_SHORT).show();
                if (groupposition == 2) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                if (groupposition == 3) {
                    Intent intent = new Intent(getActivity(), NoticeActivity.class);

                    startActivity(intent);
                }

            }
        });

        //차일드 클릭 이벤트//
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listView, View view, int groupposition, int childposition, long id) {

                String userSelectAlarm = menuList[groupposition].childViewList.get(childposition).name.toString();

                Toast.makeText(getActivity(), "child click : " + userSelectAlarm ,Toast.LENGTH_SHORT ).show();

                return true;
            }
        });

        return view;
    }
}