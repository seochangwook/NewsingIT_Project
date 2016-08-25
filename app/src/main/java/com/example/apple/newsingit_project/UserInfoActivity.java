package com.example.apple.newsingit_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.UserFolderData;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.UserFolderListAdapter;
import com.squareup.picasso.Picasso;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserInfoActivity extends AppCompatActivity {
    boolean dummy_follow_state = false; //팔로우 하지 않음이 기본 설정.//
    //사용자 정보 뷰 관련 변수//
    ImageView user_profile_imageview;
    TextView user_profile_name_textview;
    TextView user_profile_my_introduce_textview;
    Button user_follower_count_button;
    Button user_following_count_button;
    Button user_following_button;

    //사용자 폴더 관련 변수.//
    UserFolderData user_folderData; //폴더 데이터 클래스//
    UserFolderListAdapter user_folderListAdapter; //폴더 어댑태 클래스//

    //리사이클뷰 관련 변수.//
    private FamiliarRefreshRecyclerView user_folder_recyclerrefreshview;
    private FamiliarRecyclerView user_folder_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("name");

        setTitle(title);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        user_profile_imageview = (ImageView) findViewById(R.id.user_profile_imageview);
        user_profile_name_textview = (TextView) findViewById(R.id.user_profile_name_textview);
        user_profile_my_introduce_textview = (TextView) findViewById(R.id.user_profile_my_introduce_textview);
        user_follower_count_button = (Button) findViewById(R.id.user_follower_button);
        user_following_count_button = (Button) findViewById(R.id.user_following_button);
        user_following_button = (Button) findViewById(R.id.user_follow_button);

        user_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) findViewById(R.id.user_folder_rv_list);

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

        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
        //파카소 라이브러리를 이용하여 이미지 로딩//
        Picasso.with(this)
                .load(R.drawable.facebook_people_image)
                .transform(new CropCircleTransformation())
                .into(user_profile_imageview);

        //Dummy Data 설정//
        set_Dummy_Folder_Date();
    }

    public void set_Dummy_Folder_Date() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userinfo, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.search_menu) {
            Intent intent = new Intent(UserInfoActivity.this, SearchTabActivity.class);

            startActivity(intent);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
