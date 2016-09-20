package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.CreateFolderActivity;
import com.example.apple.newsingit_project.EditFolderActivity;
import com.example.apple.newsingit_project.EditMyInfoActivity;
import com.example.apple.newsingit_project.FollowerListActivity;
import com.example.apple.newsingit_project.FollowingListActivity;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.SearchTabActivity;
import com.example.apple.newsingit_project.UserScrapContentListActivity;
import com.example.apple.newsingit_project.data.json_data.myfolderlist.MyFolderListRequest;
import com.example.apple.newsingit_project.data.json_data.myfolderlist.MyFolderListRequestResults;
import com.example.apple.newsingit_project.data.json_data.myinfo.UserInfoRequest;
import com.example.apple.newsingit_project.data.json_data.myinfo.UserInfoRequestResult;
import com.example.apple.newsingit_project.data.view_data.FolderData;
import com.example.apple.newsingit_project.data.view_data.UserInfoData;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.FolderListAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragment extends Fragment {
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";
    private static final String KEY_IMG_DEFAULT_FLAG = "KEY_IMG_DEFAULT_FLAG";

    /**
     * Folder 정보 수정 시 필요한 정보
     **/
    private static final String KEY_FOLDER_IMG = "KEY_FOLDER_IMG";
    private static final String KEY_FOLDER_LOCKED = "KEY_FOLDER_LOCKED";
    /**
     * 나의 정보 수정 시 필요한 정보
     **/
    private static final String KEY_MY_IMG = "KEY_USER_IMG";
    private static final String KEY_MY_NAME = "KEY_USER_NAME";
    private static final String KEY_MY_ABOUTME = "KEY_USER_ABOUTME";
    /**
     * 초기 페이스북 디폴트 경로
     **/
    private static final String DEFAULT_FACEBOOK_IMG_PATH = "https://graph.facebook.com";
    /**
     * 네트워크 작업완료 응답을 위한 코드(갱신)
     **/
    private static final int RC_EDITMYINFO = 100;
    private static final int RC_EDITFOLLOWER = 200;
    private static final int RC_EDITFOLLOWING = 300;
    private static final int RC_EDITFOLDERLIST = 400;
    private static final int RC_EDITFOLDERINFO = 500;
    private static final int RC_READSCRAP = 600;

    static boolean emptyViewFlag = true;
    FontManager fontManager;

    //나의 정보 뷰 관련 변수//
    ImageView profile_imageview;
    // TextView profile_name_textview;
    TextView profile_my_introduce_textview;
    TextView follower_count_button;
    TextView following_count_button;
    Button my_info_replace_button;
    TextView btnScrapCount;
    String name;
    String key_default_img;

    TextView sffTextView, sffFollowingView, sffFollowerView;

    //폴더 관련 변수.//
    ImageButton folder_add_button;
    FolderData folderData; //폴더 데이터 클래스//
    FolderListAdapter folderListAdapter; //폴더 어댑태 클래스//
    String profileUrl;
    NetworkManager networkManager;
    /**
     * 공유 프래퍼런스 관련 변수
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    private FamiliarRefreshRecyclerView folder_recyclerrefreshview;
    private FamiliarRecyclerView folder_recyclerview;
    private ProgressDialog pDialog;
    private Callback requestMyInfoListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            Gson gson = new Gson();

            UserInfoRequest userInfoRequest = gson.fromJson(response_data, UserInfoRequest.class);

            setData(userInfoRequest.getResult());
            emptyViewFlag = true;
        }
    };
    private Callback requestmyfolderlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            Gson gson = new Gson();

            MyFolderListRequest myFolderListRequest = gson.fromJson(response_data, MyFolderListRequest.class);

            set_Folder_Data(myFolderListRequest.getResults(), myFolderListRequest.getResults().length);

            emptyViewFlag = true;
        }
    };


    public MyInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        fontManager = new FontManager(getActivity());

        profile_imageview = (ImageView) view.findViewById(R.id.profile_imageview);
        profile_my_introduce_textview = (TextView) view.findViewById(R.id.profile_my_introduce_textview);
        follower_count_button = (TextView) view.findViewById(R.id.follower_button);
        following_count_button = (TextView) view.findViewById(R.id.following_button);
        my_info_replace_button = (Button) view.findViewById(R.id.myinfo_replace_button);
        folder_add_button = (ImageButton) view.findViewById(R.id.category_add_button);
        btnScrapCount = (TextView) view.findViewById(R.id.scrapt_count_button);
        folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) view.findViewById(R.id.folder_rv_list);

        sffTextView = (TextView) view.findViewById(R.id.my_sff_text);
        sffFollowerView = (TextView) view.findViewById(R.id.my_sff_follower);
        sffFollowingView = (TextView) view.findViewById(R.id.my_sff_following);

        sffTextView.setTypeface(fontManager.getTypefaceMediumInstance());
        sffFollowerView.setTypeface(fontManager.getTypefaceMediumInstance());
        sffFollowingView.setTypeface(fontManager.getTypefaceMediumInstance());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        /** 폴더 리스트뷰 초기화 과정(로딩화면, 자원등록) **/
        folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(getActivity(), 3));
        folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        folder_recyclerview = folder_recyclerrefreshview.getFamiliarRecyclerView();
        folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        folder_recyclerview.setHasFixedSize(true);

        /** EmptyView 화면 설정. **/
        View emptyView;

        if (emptyViewFlag) {//폴더가 없는 일반적인 경우
            emptyView = getActivity().getLayoutInflater().inflate(R.layout.my_rv_list_emptyview, null, false);
            folder_recyclerview.setEmptyView(emptyView);
        } else { //네트워크 오류 시
            emptyView = getActivity().getLayoutInflater().inflate(R.layout.view_folder_error_empty, null, false);
            folder_recyclerview.setEmptyView(emptyView);
        }
        folder_recyclerview.setEmptyViewKeepShowHeadOrFooter(true);

        /** HeaderView 화면 설정 **/
        final View header_info_view = getActivity().getLayoutInflater().inflate(R.layout.fix_headerview_layout, null);

        /** 폴더 데이터 클래스 초기화 및 어댑터 초기화 **/
        folderData = new FolderData();
        folderListAdapter = new FolderListAdapter(getActivity());

        /** 공유 저장소 초기화 **/
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPrefs.edit();

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        folder_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        folder_recyclerrefreshview.pullRefreshComplete();
                        folderListAdapter.set_FolderDate(folderData); //설정.//

                        init_folder_list();
                        getMyFolderData();

                        folder_recyclerview.removeHeaderView(header_info_view);

                    }
                }, 1000);
            }
        });

        folder_recyclerrefreshview.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        folder_recyclerrefreshview.loadMoreComplete();
                        folderListAdapter.set_FolderDate(folderData); //설정.//

                    }
                }, 1000);
            }
        });

        /** Folder RecyclerView Adapter 등록 **/
        folder_recyclerview.setAdapter(folderListAdapter);

        /** RecyclerView 이벤트 처리 **/
        folder_recyclerrefreshview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String select_folder_name = folderData.folder_list.get(position).get_folder_name();
                String select_folder_id = "" + folderData.folder_list.get(position).get_folderid();

                Intent intent = new Intent(getActivity(), UserScrapContentListActivity.class);

                intent.putExtra(KEY_FOLDER_NAME, select_folder_name);
                intent.putExtra(KEY_FOLDER_ID, select_folder_id);
                intent.putExtra(KEY_USER_IDENTIFY_FLAG, "0"); //0이면 나의 경우//

                startActivityForResult(intent, RC_READSCRAP);
            }
        });

        folder_recyclerrefreshview.setOnItemLongClickListener(new FamiliarRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                //폴더를 삭제/수정하기 위해서 폴더의 id값이 필요.//
                int folder_id = folderData.folder_list.get(position).get_folderid();
                String folder_name = folderData.folder_list.get(position).get_folder_name();
                String folder_img = folderData.folder_list.get(position).get_folder_imageUrl();
                boolean folder_locked = folderData.folder_list.get(position).get_folder_private();

                Intent intent = new Intent(getActivity(), EditFolderActivity.class);

                //필요한 정보를 넘긴다.//
                intent.putExtra(KEY_FOLDER_ID, "" + folder_id);
                intent.putExtra(KEY_FOLDER_NAME, folder_name);
                intent.putExtra(KEY_FOLDER_IMG, folder_img);
                intent.putExtra(KEY_FOLDER_LOCKED, folder_locked);

                startActivityForResult(intent, RC_EDITFOLDERINFO);

                folder_recyclerview.addHeaderView(header_info_view);
                folder_recyclerview.smoothScrollToPosition(0);

                return true;
            }
        });

        /** 일반 버튼 처리 **/
        follower_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FollowerListActivity.class);

                startActivityForResult(intent, RC_EDITFOLLOWER);
            }
        });

        following_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FollowingListActivity.class);

                startActivityForResult(intent, RC_EDITFOLLOWING);
            }
        });

        my_info_replace_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String my_aboutme = profile_my_introduce_textview.getText().toString();
                String my_imgUrl = profileUrl;

                Intent intent = new Intent(getActivity(), EditMyInfoActivity.class);

                //필요한 정보를 넘겨준다.//
                intent.putExtra(KEY_MY_NAME, name);
                intent.putExtra(KEY_MY_ABOUTME, my_aboutme);
                intent.putExtra(KEY_MY_IMG, my_imgUrl);
                intent.putExtra(KEY_IMG_DEFAULT_FLAG, key_default_img); //1이면 디폴트, 0이면 일반//

                startActivityForResult(intent, RC_EDITMYINFO);
            }
        });

        folder_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CreateFolderActivity.class);

                startActivityForResult(intent, RC_EDITFOLDERLIST);

                folder_recyclerview.addHeaderView(header_info_view);
                folder_recyclerview.smoothScrollToPosition(0);
            }
        });

        getUserInfoNetworkData();

        getMyFolderData();

        //메뉴변경//
        setHasOptionsMenu(true);

        return view;
    }

    public void init_folder_list() {
        folderData.folder_list.clear();

        folderListAdapter.init_folder(folderData);

        folderListAdapter.notifyDataSetChanged();
    }

    public void getMyFolderData() {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/

        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me");
        builder.addPathSegment("categories");

        builder.addQueryParameter("usage", "profile");
        builder.addQueryParameter("page", "1");
        builder.addQueryParameter("count", "20");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmyfolderlistcallback);
    }

    public void set_Folder_Data(final MyFolderListRequestResults myFolderListRequestResults[], final int my_folder_list_size) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MyFolderListRequestResults> myFolderListRequestResultsList = new ArrayList<>();

                    myFolderListRequestResultsList.addAll(Arrays.asList(myFolderListRequestResults));

                    for (int i = 0; i < my_folder_list_size; i++) {
                        FolderData new_folderdata = new FolderData();

                        new_folderdata.setFolder_private(myFolderListRequestResultsList.get(i).getLocked());
                        new_folderdata.set_folder_imageUrl(myFolderListRequestResultsList.get(i).getImg_url());
                        new_folderdata.set_get_folder_name(myFolderListRequestResultsList.get(i).getName());
                        new_folderdata.set_folderid(myFolderListRequestResultsList.get(i).getId());

                        folderData.folder_list.add(new_folderdata);
                    }

                    folderListAdapter.set_FolderDate(folderData);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.my_info_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.search_menu_item) {
            Toast.makeText(getActivity(), "검색 화면으로 이동", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), SearchTabActivity.class);

            startActivityForResult(intent, RC_EDITMYINFO);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUserInfoNetworkData() {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("users")
                .addPathSegment("me");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestMyInfoListCallback);
    }

    public void setData(final UserInfoRequestResult userInfoRequestResult) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserInfoData userInfoData = new UserInfoData();
                    String aboutMe;
                    int followerCount, followingCount, scrapCount;

                    Log.d("message", "수정");

                    userInfoData.setName(userInfoRequestResult.getName());
                    userInfoData.setProfileUrl(userInfoRequestResult.getPf_url());
                    userInfoData.setAboutMe(userInfoRequestResult.getAboutme());
                    userInfoData.setFollowerCount(userInfoRequestResult.getFollowers());
                    userInfoData.setFollwingCount(userInfoRequestResult.getFollowings());
                    userInfoData.setScrapCount(userInfoRequestResult.getScrapings());

                    //공유 프래퍼런스 데이터도 초기화//
                    PropertyManager.getInstance().set_pf_Url(userInfoRequestResult.getPf_url());
                    PropertyManager.getInstance().set_name(userInfoRequestResult.getName());

                    name = userInfoData.getName();
                    profileUrl = userInfoData.getProfileUrl();
                    aboutMe = userInfoData.getAboutMe();
                    followerCount = userInfoData.getFollowerCount();
                    followingCount = userInfoData.getFollwingCount();
                    scrapCount = userInfoData.getScrapCount();

                    profile_my_introduce_textview.setText(aboutMe);
                    follower_count_button.setText("" + followerCount);
                    following_count_button.setText("" + followingCount);
                    btnScrapCount.setText("" + scrapCount);

                    profile_my_introduce_textview.setTypeface(fontManager.getTypefaceMediumInstance());
                    follower_count_button.setTypeface(fontManager.getTypefaceBoldInstance());
                    following_count_button.setTypeface(fontManager.getTypefaceBoldInstance());
                    btnScrapCount.setTypeface(fontManager.getTypefaceBoldInstance());


                    //이미지 분할 (기존 적용된 https와 다른 웹 사이트의 https의 충돌문제)//
                    //pf_url을 가지고 파싱//
                    //String profileUrl="https://graph.facebook.com/v2.6/865056173626962/picture?type=large";
                    Log.d("json control", profileUrl);

                    String parsing_imageurl = profileUrl.substring(0, 26); //문자열 자르기//

                    Log.d("json control", parsing_imageurl);

                    //페이스북 이미지는 일반적인 피카소로 적용//
                    if (parsing_imageurl.equals(DEFAULT_FACEBOOK_IMG_PATH)) {
                        Picasso.with(getActivity())
                                .load(profileUrl)
                                .transform(new CropCircleTransformation())
                                .into(profile_imageview); //into로 보낼 위젯 선택.//

                        key_default_img = "1"; //디폴트 이미지이면 1//
                    } else //현재 앱에서의 https이미지.//
                    {
                        Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

                        picasso.load(profileUrl)
                                .transform(new CropCircleTransformation())
                                .into(profile_imageview);

                        key_default_img = "0"; //디폴트가 아니면 0//
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RC_EDITMYINFO) //나의 정보에 대한 수정응답//
            {
                Log.d("json control", "나의 정보 갱신");

                //갱신작업 진행//
                getUserInfoNetworkData();
            } else if (requestCode == RC_EDITFOLLOWER) {
                Log.d("json control", "팔로워 정보 수 갱신");

                getUserInfoNetworkData();
            } else if (requestCode == RC_EDITFOLLOWING) {
                Log.d("json control", "팔로잉 정보 수 갱신");

                getUserInfoNetworkData();
            } else if (requestCode == RC_EDITFOLDERLIST) {
                Log.d("json control", "폴더 리스트 갱신");

                init_folder_list(); //리스트 초기화는 이중적용을 막기 위해서 초기화를 먼저 해준다.//

                getMyFolderData();
            } else if (requestCode == RC_EDITFOLDERINFO) {
                Log.d("json control", "폴더 정보 갱신");

                init_folder_list(); //리스트 초기화는 이중적용을 막기 위해서 초기화를 먼저 해준다.//

                getMyFolderData();
            } else if (requestCode == RC_READSCRAP) {
                Log.d("json control", "내 정보 / 폴더정보 수 갱신");

                getUserInfoNetworkData();

                init_folder_list(); //리스트 초기화는 이중적용을 막기 위해서 초기화를 먼저 해준다.//

                getMyFolderData();
            }
        }
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
