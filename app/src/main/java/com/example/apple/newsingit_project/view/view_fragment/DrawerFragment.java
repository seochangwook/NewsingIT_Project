package com.example.apple.newsingit_project.view.view_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apple.newsingit_project.LoginActivity;
import com.example.apple.newsingit_project.NoticeActivity;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.DrawerChild;
import com.example.apple.newsingit_project.data.view_data.DrawerGroup;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.DrawerAdapter;
import com.facebook.login.LoginManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    public static final String MENU_MY_PAGE = "마이페이지";
    public static final String MENU_SET_ALARM = "알림 설정";
    public static final String MENU_LOGOUT = "로그아웃";
    public static final String MENU_NOTICE = "공지사항";

    public static final String CHILD_NEW_SCRAP = "팔로잉 스크랩 알림";
    public static final String CHILD_LIKE_MY_SCRAP ="내 스크랩 좋아요";
    public static final String CHILD_FOLLOW_MY_PAGE = "팔로우 알림";

    DrawerGroup[] menuList = {

            new DrawerGroup(MENU_LOGOUT),
            new DrawerGroup(MENU_SET_ALARM,
                    new DrawerChild(CHILD_NEW_SCRAP),
                    new DrawerChild(CHILD_LIKE_MY_SCRAP),
                    new DrawerChild(CHILD_FOLLOW_MY_PAGE)),
            new DrawerGroup(MENU_MY_PAGE),
            new DrawerGroup(MENU_NOTICE)
    };

    ExpandableListView expandableListView;
    DrawerAdapter mAdapter;

    NetworkManager networkManager;
    LoginManager mLoginManager;

    /**
     * profile 정보
     **/
    ImageView profile_image;
    ImageButton alarm_imagebutton;

    /**
     * Shraed 저장소 관련 변수
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//

    private Callback requestLogoutCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "로그아웃 하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    public DrawerFragment() {
        // Required empty public constructor
    }

    private void getLogoutNetworkData() {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("logout");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestLogoutCallback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginManager = LoginManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        View drawerHeader = inflater.inflate(R.layout.view_drawer_header, container, false);

        mAdapter = new DrawerAdapter(menuList, getActivity());

        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        expandableListView.addHeaderView(drawerHeader);
        expandableListView.setAdapter(mAdapter);

        //expandablelistview의 group indicator를 cusotom해주기 위해 기본 indicator를 제거한다//
        expandableListView.setGroupIndicator(null);


        //그룹을 닫았을 때 이벤트//
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupposition) {

            }
        });

        //그룹을 펼쳤을 때 이벤트//
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupposition) {
                //로그아웃 - 로그인 화면으로 이동//
                if (groupposition == 0) {
                    //공유저장소 내용을 초기화.//
                    //프래퍼런스를 셋팅.//
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    mEditor = mPrefs.edit();

                    PropertyManager.getInstance().set_facebookid("");
                    PropertyManager.getInstance().set_name("");
                    PropertyManager.getInstance().set_pf_Url("");
                    PropertyManager.getInstance().set_nt_fs("");
                    PropertyManager.getInstance().set_nt_f("");
                    PropertyManager.getInstance().set_nt_s("");

                    getLogoutNetworkData(); //우리 앱 로그아웃

                    mLoginManager.logOut(); //페이스북 로그아웃

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                //공지사항 이동//
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

                return true;
            }
        });

        return view;
    }
}