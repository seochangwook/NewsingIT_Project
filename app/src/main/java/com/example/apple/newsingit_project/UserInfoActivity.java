package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.userfolderlist.UserFolderListRequest;
import com.example.apple.newsingit_project.data.json_data.userfolderlist.UserFolderListRequestResults;
import com.example.apple.newsingit_project.data.json_data.userinfo.UserInfoRequest;
import com.example.apple.newsingit_project.data.json_data.userinfo.UserInfoRequestResult;
import com.example.apple.newsingit_project.data.view_data.UserFolderData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.UserFolderListAdapter;
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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity {
    public static final int USER_INFO_REQUEST_CODE = 10;
    private static final String USER_ID = "USER_ID";
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    /**
     * 초기 페이스북 디폴트 경로
     **/
    private static final String DEFAULT_FACEBOOK_IMG_PATH = "https://graph.facebook.com";
    static boolean emptyViewFlag = true;
    /**
     * 네트워크 작업완료 응답을 위한 코드(갱신)
     **/


    boolean follow_state = false; //팔로우 하지 않음이 기본 설정.//
    //사용자 정보 뷰 관련 변수//
    ImageView user_profile_imageview;
    TextView user_profile_my_introduce_textview;
    TextView user_follower_count_button;
    TextView user_following_count_button;
    TextView sffTextView, sffFollowerView, sffFollowingView;
    ImageButton user_following_button;
    TextView user_scrap_button;

    //사용자 폴더 관련 변수.//
    UserFolderData user_folderData; //폴더 데이터 클래스//
    UserFolderListAdapter user_folderListAdapter; //폴더 어댑태 클래스//
    String name;
    String get_user_id = null;
    /**
     * 네트워크 데이터
     **/
    String following_count;
    String follower_count;
    String scrap_count;
    String user_imgUrl;
    String user_name;
    String user_intro;
    String follow_flag;
    /**
     * 네트워크 관련 변수
     **/
    NetworkManager networkManager;

    FontManager fontManager;
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

            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            if (response.code() == 404) {
                Log.d("json control", "유저 검색 실패");

                if (this != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserInfoActivity.this);
                            alertDialog.setTitle("Newsing Search")
                                    .setMessage("등록되지 않은 사용자 입니다")
                                    .setCancelable(false)
                                    .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });

                            AlertDialog alert = alertDialog.create();
                            alert.show();
                        }
                    });
                }
                emptyViewFlag = false;
            } else {
                Gson gson = new Gson();

                UserInfoRequest userInfoRequest = gson.fromJson(response_data, UserInfoRequest.class);

                set_UserInfo_Data(userInfoRequest.getResult());
                emptyViewFlag = true;
            }
        }
    };

    private Callback requestUserFolderListCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            Gson gson = new Gson();

            UserFolderListRequest userFolderListRequest = gson.fromJson(responseData, UserFolderListRequest.class);

            set_User_Folder_Data(userFolderListRequest.getResults(), userFolderListRequest.getResults().length);

            emptyViewFlag = true;
        }
    };
    private Callback requestSetFollowingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
            emptyViewFlag = true;
        }
    };
    private Callback requestUnSetFollowingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
            emptyViewFlag = false;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
            emptyViewFlag = true;

        }
    };

    public void set_User_Folder_Data(final UserFolderListRequestResults userFolderListRequestResults[], final int user_folder_size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<UserFolderListRequestResults> userFolderListRequestResultsList = new ArrayList<>();

                    userFolderListRequestResultsList.addAll(Arrays.asList(userFolderListRequestResults));

                    for (int i = 0; i < user_folder_size; i++) {
                        //폴더의 경우 private가 있을경우 상대방에서는 보이지 않아야 한다.//
                        if (userFolderListRequestResultsList.get(i).getLocked() == true) //잠금 활성화 상태//
                        {
                            //비공개 폴더는 배열에 넣지 않아샤 다른 사용자에게 보이지 않는다.//
                        } else if (userFolderListRequestResultsList.get(i).getLocked() == false) //잠금 비활성화 상태//
                        {
                            UserFolderData new_user_folderdata = new UserFolderData();

                            new_user_folderdata.setFolder_id(userFolderListRequestResultsList.get(i).getId());
                            new_user_folderdata.set_get_folder_name(userFolderListRequestResultsList.get(i).getName());
                            new_user_folderdata.set_get_folder_imageUrl(userFolderListRequestResultsList.get(i).getImg_url());
                            new_user_folderdata.setFolder_private(userFolderListRequestResultsList.get(i).getLocked());

                            //기본적으로 서버에서 비공개 처리폴더에 대한 정보는 오지 않는다.//
                            user_folderData.user_folder_list.add(new_user_folderdata);
                        }
                    }

                    user_folderListAdapter.set_UserFolderDate(user_folderData);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_layout);

        fontManager = new FontManager(UserInfoActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_profile_imageview = (ImageView) findViewById(R.id.user_profile_imageview);
        user_profile_my_introduce_textview = (TextView) findViewById(R.id.user_profile_my_introduce_textview);
        user_follower_count_button = (TextView) findViewById(R.id.user_follower_button);
        user_following_count_button = (TextView) findViewById(R.id.user_following_button);
        user_following_button = (ImageButton) findViewById(R.id.user_follow_button);
        user_scrap_button = (TextView) findViewById(R.id.scrapt_count_button);

        sffFollowerView = (TextView) findViewById(R.id.sff_follower);
        sffFollowingView = (TextView) findViewById(R.id.sff_following);
        sffTextView = (TextView) findViewById(R.id.sff_text);

        user_scrap_button.setTypeface(fontManager.getTypefaceBoldInstance());
        user_follower_count_button.setTypeface(fontManager.getTypefaceBoldInstance());
        user_following_count_button.setTypeface(fontManager.getTypefaceBoldInstance());
        user_profile_my_introduce_textview.setTypeface(fontManager.getTypefaceMediumInstance());

        sffTextView.setTypeface(fontManager.getTypefaceMediumInstance());
        sffFollowingView.setTypeface(fontManager.getTypefaceMediumInstance());
        sffFollowerView.setTypeface(fontManager.getTypefaceMediumInstance());

        user_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) findViewById(R.id.user_folder_rv_list);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //전달되는 값을 받아온다.//
        Intent intent = getIntent();

        get_user_id = intent.getStringExtra(USER_ID); //사용자의 id를 얻어온다.//
        name = intent.getStringExtra("USER_NAME");

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
        user_folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(this, 3));
        user_folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        user_folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        user_folder_recyclerview = user_folder_recyclerrefreshview.getFamiliarRecyclerView();
        user_folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        user_folder_recyclerview.setHasFixedSize(true);

        /** EmptyView 화면 설정. **/
        View emptyView;

        if (emptyViewFlag) {
            emptyView = getLayoutInflater().inflate(R.layout.user_rv_list_empty_view, null, false);
            /** EmptyView 위젯 **/
            TextView empty_textview = (TextView) emptyView.findViewById(R.id.empty_msg_user);
            empty_textview.setTypeface(fontManager.getTypefaceRegularInstance());

            user_folder_recyclerview.setEmptyView(emptyView);
        } else { //네트워크 에러//
            emptyView = getLayoutInflater().inflate(R.layout.view_network_error_empty, null, false);

            TextView error_textview = (TextView) emptyView.findViewById(R.id.error_msg_text);
            error_textview.setTypeface(fontManager.getTypefaceRegularInstance());

            user_folder_recyclerview.setEmptyView(emptyView);
        }


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

                        init_folder_list(); //다시 리스트 정보를 초기화.//

                        get_User_Folder_Data(get_user_id); //id값이 조건으로 필요하다.//

                        Log.d("json control", "사용자 폴더 리스트 초기화");
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
                String select_user_folder_id = "" + user_folderData.user_folder_list.get(position).getFolder_id();

                Toast.makeText(UserInfoActivity.this, select_user_folder_name + "폴더로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UserInfoActivity.this, UserScrapContentListActivity.class);

                intent.putExtra(KEY_FOLDER_NAME, select_user_folder_name);
                intent.putExtra(KEY_FOLDER_ID, select_user_folder_id);
                intent.putExtra("KEY_USER_IDENTIFY_FLAG", "1"); //다른 사용자일 경우 1 / 나일 경우 0//

                startActivityForResult(intent, USER_INFO_REQUEST_CODE);
            }
        });

        /** 기타 기능 이벤트 처리 **/
        user_following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow_state == false) //현재 내가 해당 유저를 팔로잉하고 있지 않을경우//
                {
                    user_following_button.setImageResource(R.mipmap.btn_following_600_72);

                    follow_state = true;

                    //팔로우 작업//
                    Log.d("json control", "팔로잉을 현재 하지 않은 상태미으로 팔로잉을 생성");

                    set_user_follow(get_user_id);

                } else if (follow_state == true) //현재 내가 해당 유저를 팔로잉하고 있는 경우//
                {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserInfoActivity.this);
                    alertDialog.setMessage(name + " 님의 팔로우를 취소 하시겠어요?").setCancelable(false).setPositiveButton("팔로우 취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //yes

                                    user_following_button.setImageResource(R.mipmap.btn_follow_600_72);

                                    follow_state = false;

                                    //팔로우 해제 작업//
                                    Log.d("json control", "팔로잉을 현재 한 상태이므로 팔로잉을 해제");

                                    unset_user_follow(get_user_id);

                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //no

                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        });

        //유저 프로필 정보를 불러온다.//
        get_UserInfo_Data(get_user_id); //id값이 조건으로 필요하다.//
        get_User_Folder_Data(get_user_id); //id값이 조건으로 필요하다.//
    }

    public void init_folder_list() {
        user_folderData.user_folder_list.clear();

        user_folderListAdapter.init_FolderDate(user_folderData);

        user_folderListAdapter.notifyDataSetChanged();
    }

    public void set_user_follow(String follow_user_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows");

        RequestBody body = new FormBody.Builder()
                .add("ofid", follow_user_id)
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .post(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);
    }

    public void unset_user_follow(String follow_user_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addPathSegment(follow_user_id);

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestUnSetFollowingCallback);
    }

    public void get_User_Folder_Data(String user_id) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("users")
                .addPathSegment(user_id)
                .addPathSegment("categories");

        builder.addQueryParameter("usage", "profile");
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
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
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
                    user_imgUrl = userInfoRequestResult.getPf_url();
                    follow_flag = "" + userInfoRequestResult.get_flag();
                    follow_state = userInfoRequestResult.get_flag();
                    user_name = userInfoRequestResult.getName();

                    Log.d("json control", user_imgUrl);

                    String parsing_imageurl = user_imgUrl.substring(0, 26); //문자열 자르기//

                    Log.d("json control", parsing_imageurl);

                    //페이스북 이미지는 일반적인 피카소로 적용//
                    if (parsing_imageurl.equals(DEFAULT_FACEBOOK_IMG_PATH)) {
                        Picasso.with(UserInfoActivity.this)
                                .load(user_imgUrl)
                                .transform(new CropCircleTransformation())
                                .into(user_profile_imageview); //into로 보낼 위젯 선택.//
                    } else {
                        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
                        Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

                        picasso.load(user_imgUrl)
                                .transform(new CropCircleTransformation())
                                .into(user_profile_imageview);
                    }

                    //타이틀 설정.//
                    /** 타이틀과 이름 값 초기화 **/
                    setTitle(user_name);

                    if (follow_flag.equals("true")) //팔로잉 되있는 상태.//
                    {
                        user_following_button.setImageResource(R.mipmap.btn_following_600_72);
                    } else if (follow_flag.equals("false")) //팔로우가 되있지 않은 상태.//
                    {
                        user_following_button.setImageResource(R.mipmap.btn_follow_600_72);
                    }

                    user_profile_my_introduce_textview.setText(user_intro);
                    user_follower_count_button.setText(follower_count);
                    user_following_count_button.setText(following_count);
                    user_scrap_button.setText(scrap_count);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_INFO_REQUEST_CODE && resultCode == UserScrapContentListActivity.USER_SCRAP_CONTENT_RESULT_OK) {
            init_folder_list();
            get_User_Folder_Data(get_user_id);
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
