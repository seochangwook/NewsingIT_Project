package com.example.apple.newsingit_project.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.apple.newsingit_project.data.json_data.myinfo.UserInfoRequest;
import com.example.apple.newsingit_project.data.json_data.myinfo.UserInfoRequestResult;
import com.example.apple.newsingit_project.data.view_data.FolderData;
import com.example.apple.newsingit_project.data.view_data.UserInfoData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.FolderListAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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
    //나의 정보 뷰 관련 변수//
    ImageView profile_imageview;
    TextView profile_name_textview;
    TextView profile_my_introduce_textview;
    Button follower_count_button;
    Button following_count_button;
    Button my_info_replace_button;
    Button btnScrapCount;

    //폴더 관련 변수.//
    Button folder_add_button;
    FolderData folderData; //폴더 데이터 클래스//
    FolderListAdapter folderListAdapter; //폴더 어댑태 클래스//
    String profileUrl;
    NetworkManager networkManager;
    private FamiliarRefreshRecyclerView folder_recyclerrefreshview;
    private FamiliarRecyclerView folder_recyclerview;
    private ProgressDialog pDialog;
    private Callback requestMyInfoListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            Gson gson = new Gson();

            UserInfoRequest userInfoRequest = gson.fromJson(response_data, UserInfoRequest.class);

            setData(userInfoRequest.getResult());
        }
    };

    public MyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        profile_imageview = (ImageView) view.findViewById(R.id.profile_imageview);
        profile_name_textview = (TextView) view.findViewById(R.id.profile_name_textview);
        profile_my_introduce_textview = (TextView) view.findViewById(R.id.profile_my_introduce_textview);
        follower_count_button = (Button) view.findViewById(R.id.follower_button);
        following_count_button = (Button) view.findViewById(R.id.following_button);
        my_info_replace_button = (Button) view.findViewById(R.id.myinfo_replace_button);
        folder_add_button = (Button) view.findViewById(R.id.category_add_button);
        btnScrapCount = (Button) view.findViewById(R.id.scrapt_count_button);
        folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) view.findViewById(R.id.folder_rv_list);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        /** 폴더 리스트뷰 초기화 과정(로딩화면, 자원등록) **/
        folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(getActivity()));
        folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        folder_recyclerview = folder_recyclerrefreshview.getFamiliarRecyclerView();
        folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        folder_recyclerview.setHasFixedSize(true);

        /** 폴더 데이터 클래스 초기화 및 어댑터 초기화 **/
        folderData = new FolderData();
        folderListAdapter = new FolderListAdapter(getActivity());

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


        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
        //파카소 라이브러리를 이용하여 이미지 로딩//
        Picasso.with(getActivity()) //profileUrl//
                .load("https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4") //url//
                .transform(new CropCircleTransformation())
                .into(profile_imageview);

        /** RecyclerView 이벤트 처리 **/
        folder_recyclerrefreshview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String select_folder_name = folderData.folder_list.get(position).get_folder_name();

                Toast.makeText(getActivity(), select_folder_name + "폴더로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), UserScrapContentListActivity.class);

                intent.putExtra("KEY_FOLDER_NAME", select_folder_name);
                intent.putExtra("KEY_USER_IDENTIFY_FLAG", "0"); //0이면 나의 경우//

                startActivity(intent);
            }
        });

        folder_recyclerrefreshview.setOnItemLongClickListener(new FamiliarRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String folder_name = folderData.folder_list.get(position).get_folder_name();
                Intent intent = new Intent(getActivity(), EditFolderActivity.class);
                startActivity(intent);

                //Toast.makeText(getActivity(), folder_name + "폴더 제거" + "" + position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        /** 일반 버튼 처리 **/
        follower_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "팔로워 리스트 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), FollowerListActivity.class);

                startActivity(intent);
            }
        });

        following_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "팔로잉 리스트 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), FollowingListActivity.class);

                startActivity(intent);
            }
        });

        my_info_replace_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "내 정보 수정하기 화면으로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), EditMyInfoActivity.class);

                startActivity(intent);
            }
        });

        folder_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "폴더 추가", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), CreateFolderActivity.class);

                startActivity(intent);
            }
        });

        getUserInfoNetworkData();

        //Dummy Data 설정//
        setDummyFolderData();


        //메뉴변경//
        setHasOptionsMenu(true);

        return view;
    }

    public void setDummyFolderData() {
        //첫번째 폴더//
        FolderData new_folderdata_1 = new FolderData();

        boolean folder_private_1 = true;

        new_folderdata_1.setFolder_private(folder_private_1);
        new_folderdata_1.set_get_folder_name("사회이슈");
        new_folderdata_1.set_dummy_folder_image(R.mipmap.ic_launcher);

        folderData.folder_list.add(new_folderdata_1);

        //첫번째 폴더//
        FolderData new_folderdata_2 = new FolderData();

        boolean folder_private_2 = false;

        new_folderdata_2.setFolder_private(folder_private_2);
        new_folderdata_2.set_get_folder_name("IT/과학");
        new_folderdata_2.set_dummy_folder_image(R.mipmap.ic_launcher);

        folderData.folder_list.add(new_folderdata_2);

        //첫번째 폴더//
        FolderData new_folderdata_3 = new FolderData();

        boolean folder_private_3 = true;

        new_folderdata_3.setFolder_private(folder_private_3);
        new_folderdata_3.set_get_folder_name("게임");
        new_folderdata_3.set_dummy_folder_image(R.mipmap.ic_launcher);

        folderData.folder_list.add(new_folderdata_3);

        folderListAdapter.set_FolderDate(folderData); //설정.//
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

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUserInfoNetworkData() {
        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http")
                .host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com")
                .addPathSegment("users")
                .addPathSegment("me");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        client.newCall(request).enqueue(requestMyInfoListCallback);

        hidepDialog();

    }

    public void setData(final UserInfoRequestResult userInfoRequestResult) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UserInfoData userInfoData = new UserInfoData();
                    String name, aboutMe;
                    int followerCount, followingCount, scrapCount;

                    userInfoData.setName(userInfoRequestResult.getName());
                    userInfoData.setProfileUrl(userInfoRequestResult.getPf_url());
                    userInfoData.setAboutMe(userInfoRequestResult.getAboutme());
                    userInfoData.setFollowerCount(userInfoRequestResult.getFollowers());
                    userInfoData.setFollwingCount(userInfoRequestResult.getFollowings());
                    userInfoData.setScrapCount(userInfoRequestResult.getScrapings());

                    name = userInfoData.getName();
                    profileUrl = userInfoData.getProfileUrl();
                    aboutMe = userInfoData.getAboutMe();
                    followerCount = userInfoData.getFollowerCount();
                    followingCount = userInfoData.getFollwingCount();
                    scrapCount = userInfoData.getScrapCount();

                    profile_name_textview.setText(name);
                    profile_my_introduce_textview.setText(aboutMe);
                    follower_count_button.setText("" + followerCount);
                    following_count_button.setText("" + followingCount);
                    btnScrapCount.setText("" + scrapCount);


                }
            });
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
