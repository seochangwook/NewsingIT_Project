package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.userinfo.UserInfoRequest;
import com.example.apple.newsingit_project.data.json_data.userinfo.UserInfoRequestResult;
import com.example.apple.newsingit_project.data.view_data.UserFolderData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.UserFolderListAdapter;
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

public class UserInfoActivity extends AppCompatActivity {
    private static final String USER_ID = "USER_ID";
    private static final String USER_NAME = "USER_NAME";

    boolean dummy_follow_state = false; //팔로우 하지 않음이 기본 설정.//
    //사용자 정보 뷰 관련 변수//
    ImageView user_profile_imageview;
    TextView user_profile_name_textview;
    TextView user_profile_my_introduce_textview;
    Button user_follower_count_button;
    Button user_following_count_button;
    Button user_following_button;
    Button user_scrap_button;

    //사용자 폴더 관련 변수.//
    UserFolderData user_folderData; //폴더 데이터 클래스//
    UserFolderListAdapter user_folderListAdapter; //폴더 어댑태 클래스//

    String get_user_id = null;
    String get_user_name = null;

    /**
     * 네트워크 데이터
     **/
    String following_count;
    String follower_count;
    String scrap_count;
    String user_imgUrl;
    String user_name;
    String user_intro;
    /**
     * 네트워크 관련 변수
     **/
    NetworkManager networkManager;
    //리사이클뷰 관련 변수.//
    private FamiliarRefreshRecyclerView user_folder_recyclerrefreshview;
    private FamiliarRecyclerView user_folder_recyclerview;
    private ProgressDialog pDialog;
    private Callback requestuserinfocallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            Gson gson = new Gson();

            UserInfoRequest userInfoRequest = gson.fromJson(response_data, UserInfoRequest.class);

            set_UserInfo_Data(userInfoRequest.getResult());
        }
    };
    private Callback requestUserFolderListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_profile_imageview = (ImageView) findViewById(R.id.user_profile_imageview);
        user_profile_name_textview = (TextView) findViewById(R.id.user_profile_name_textview);
        user_profile_my_introduce_textview = (TextView) findViewById(R.id.user_profile_my_introduce_textview);
        user_follower_count_button = (Button) findViewById(R.id.user_follower_button);
        user_following_count_button = (Button) findViewById(R.id.user_following_button);
        user_following_button = (Button) findViewById(R.id.user_follow_button);
        user_scrap_button = (Button) findViewById(R.id.scrapt_count_button);

        user_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) findViewById(R.id.user_folder_rv_list);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent intent = getIntent();

        get_user_id = intent.getStringExtra(USER_ID);
        get_user_name = intent.getStringExtra(USER_NAME);

        /** 타이틀과 이름 값 초기화 **/
        setTitle(get_user_name);
        user_profile_name_textview.setText(get_user_name);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** 폴더 리스트뷰 초기화 과정(로딩화면, 자원등록) **/
        user_folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(this));
        user_folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        user_folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        user_folder_recyclerview = user_folder_recyclerrefreshview.getFamiliarRecyclerView();
        user_folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        user_folder_recyclerview.setHasFixedSize(true);

        /** EmptyView 화면 설정. **/
        View user_empty_list_view = getLayoutInflater().inflate(R.layout.user_rv_list_empty_view, null, false);
        user_folder_recyclerview.setEmptyView(user_empty_list_view);
        user_folder_recyclerview.setEmptyViewKeepShowHeadOrFooter(true);

        /** 폴더 데이터 클래스 초기화 및 어댑터 초기화 **/
        user_folderData = new UserFolderData();
        user_folderListAdapter = new UserFolderListAdapter(this);

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        user_folder_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        user_folder_recyclerrefreshview.pullRefreshComplete();
                    }
                }, 1000);
            }
        });

        user_folder_recyclerrefreshview.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        user_folder_recyclerrefreshview.loadMoreComplete();
                    }
                }, 1000);
            }
        });

        /** Folder RecyclerView Adapter 등록 **/
        user_folder_recyclerview.setAdapter(user_folderListAdapter);

        /** RecyclerView 이벤트 처리 **/
        user_folder_recyclerrefreshview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String select_user_folder_name = user_folderData.user_folder_list.get(position).get_folder_name();

                Toast.makeText(UserInfoActivity.this, select_user_folder_name + "폴더로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UserInfoActivity.this, UserScrapContentListActivity.class);

                intent.putExtra("KEY_FOLDER_NAME", select_user_folder_name);
                intent.putExtra("KEY_USER_IDENTIFY_FLAG", "1"); //다른 사용자일 경우 1 / 나일 경우 0//

                startActivity(intent);
            }
        });

        /** 기타 기능 이벤트 처리 **/
        user_follower_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, FollowerListActivity.class);

                //필요한 정보를 넘겨준다.//
                startActivity(intent);
            }
        });

        user_following_count_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, FollowingListActivity.class);

                startActivity(intent);
            }
        });

        user_following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dummy_follow_state == false) {
                    user_following_button.setBackgroundColor(getResources().getColor(R.color.bottom_bar_unselected));
                    user_following_button.setText("!팔로잉");

                    dummy_follow_state = true;
                } else if (dummy_follow_state == true) {
                    user_following_button.setBackgroundColor(getResources().getColor(R.color.button_transparent_background));
                    user_following_button.setText("+팔로우");

                    dummy_follow_state = false;
                }
            }
        });

        //Dummy Data 설정//
        //set_Dummy_Folder_Date();

        //유저 프로필 정보를 불러온다.//
        get_UserInfo_Data(get_user_id); //id값이 조건으로 필요하다.//
        get_User_Folder_Data(get_user_id); //id값이 조건으로 필요하다.//
    }

    public void get_User_Folder_Data(String user_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com")
                .addPathSegment("users")
                .addPathSegment(user_id)
                .addPathSegment("categories");

        builder.addQueryParameter("usage", "scrap");
        builder.addQueryParameter("page", "1");
        builder.addQueryParameter("count", "20");

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestUserFolderListCallback);
    }

    public void get_UserInfo_Data(String user_id) {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com");
        builder.addPathSegment("users");
        builder.addPathSegment(user_id);

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestuserinfocallback);
    }

    public void set_UserInfo_Data(final UserInfoRequestResult userInfoRequestResult) {
        //데이터 값을 할당.//
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    user_intro = userInfoRequestResult.getAboutme();
                    scrap_count = "" + userInfoRequestResult.getScrapings();
                    following_count = "" + userInfoRequestResult.getFollowings();
                    follower_count = "" + userInfoRequestResult.getFollowers();
                    //user_imgUrl = userInfoRequestResult.getPf_url();
                    user_imgUrl = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";

                    /*Log.d("user aboutMe", user_intro);
                    Log.d("user scrap count", scrap_count);
                    Log.d("user following", following_count);
                    Log.d("user follower", follower_count);
                    Log.d("user imgUrl", user_imgUrl);*/

                    //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
                    //파카소 라이브러리를 이용하여 이미지 로딩//
                    Picasso.with(UserInfoActivity.this)
                            .load(user_imgUrl)
                            .transform(new CropCircleTransformation())
                            .into(user_profile_imageview);

                    user_profile_my_introduce_textview.setText(user_intro);
                    user_follower_count_button.setText(follower_count);
                    user_following_count_button.setText(following_count);
                    user_scrap_button.setText(scrap_count);
                }
            });
        }
    }

    /*public void set_Dummy_Folder_Date() {
        //첫번째 폴더//
        UserFolderData new_user_folderdata_1 = new UserFolderData();

        boolean user_folder_private_1 = true;

        new_user_folderdata_1.setFolder_private(user_folder_private_1);
        new_user_folderdata_1.set_get_folder_name("사회이슈");
        new_user_folderdata_1.set_dummy_folder_image(android.R.drawable.ic_menu_close_clear_cancel);

        //user_folderData.user_folder_list.add(new_user_folderdata_1); //첫번째 폴더는 비공개이므로 생성 안함.//

        //두번째 폴더//
        UserFolderData new_user_folderdata_2 = new UserFolderData();

        boolean user_folder_private_2 = false;

        new_user_folderdata_2.setFolder_private(user_folder_private_2);
        new_user_folderdata_2.set_get_folder_name("IT/과학");
        new_user_folderdata_2.set_dummy_folder_image(R.mipmap.ic_launcher);

        user_folderData.user_folder_list.add(new_user_folderdata_2);

        user_folderListAdapter.set_UserFolderDate(user_folderData); //설정.//
    }*/

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
