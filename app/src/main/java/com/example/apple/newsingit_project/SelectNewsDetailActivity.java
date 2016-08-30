package com.example.apple.newsingit_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.view_data.ScrapFolderListData;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.view.view_fragment.WebViewFragment;
import com.example.apple.newsingit_project.widget.adapter.ScrapFolderListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

public class SelectNewsDetailActivity extends AppCompatActivity {
    Button btn;

    /**
     * Popup관련 변수
     **/
    Button scrap_folder_create_button;
    TextView textView;

    PopupWindow scrap_folder_popup;
    View scrap_folderlist_view;
    ScrapFolderListData scrapfolderData; //폴더 데이터 클래스//
    ScrapFolderListAdapter scrapfolderListAdapter; //폴더 어댑태 클래스//
    /**
     * 폴더 리스트 관련 변수
     **/
    private FamiliarRefreshRecyclerView scrap_folder_recyclerrefreshview;
    private FamiliarRecyclerView scrap_folder_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_news_detail_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /** 팝업에서 사용되는 변수들 초기화 **/

        scrap_folderlist_view = getLayoutInflater().inflate(R.layout.scrap_folder_select, null);

        scrap_folder_create_button = (Button) scrap_folderlist_view.findViewById(R.id.create_scrap_folder_button);
        scrap_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) scrap_folderlist_view.findViewById(R.id.scrap_folder_rv_list);

        //팝업창 설정.//
        scrap_folder_popup = new PopupWindow(scrap_folderlist_view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        scrap_folder_popup.setTouchable(true);
        scrap_folder_popup.setOutsideTouchable(true);
        scrap_folder_popup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        scrap_folder_popup.setAnimationStyle(R.style.PopupAnimationBottom);
        scrap_folder_popup.getContentView().setFocusableInTouchMode(true);
        scrap_folder_popup.getContentView().setFocusable(true);

        setSupportActionBar(toolbar);

        /** 폴더 리스트뷰 초기화 과정(로딩화면, 자원등록) **/
        scrap_folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(this));
        scrap_folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        scrap_folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        scrap_folder_recyclerview = scrap_folder_recyclerrefreshview.getFamiliarRecyclerView();
        scrap_folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        scrap_folder_recyclerview.setHasFixedSize(true);

        /** 폴더 데이터 클래스 초기화 및 어댑터 초기화 **/
        scrapfolderData = new ScrapFolderListData();
        scrapfolderListAdapter = new ScrapFolderListAdapter(this);

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        scrap_folder_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        scrap_folder_recyclerrefreshview.pullRefreshComplete();
                    }
                }, 1000);
            }
        });

        scrap_folder_recyclerrefreshview.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        scrap_folder_recyclerrefreshview.loadMoreComplete();
                    }
                }, 1000);
            }
        });

        /** Folder RecyclerView Adapter 등록 **/
        scrap_folder_recyclerview.setAdapter(scrapfolderListAdapter);

        scrap_folder_recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String folder_name = scrapfolderData.scrapfolderlist.get(position).get_scrap_folder_list_data();

                Toast.makeText(SelectNewsDetailActivity.this, folder_name + " 폴더 선택", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SelectNewsDetailActivity.this, CreateScrapContentActivity.class);

                //필요한 정보를 넘겨준다.//
                startActivity(intent);

                scrap_folder_popup.dismiss();
            }
        });

        scrap_folder_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectNewsDetailActivity.this, CreateFolderActivity.class);

                startActivity(intent);

                scrap_folder_popup.dismiss();
            }
        });

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //textView에 scroll 추가//
        textView = (TextView) findViewById(R.id.text_news_part);
        textView.setMovementMethod(new ScrollingMovementMethod());


        Intent intent = getIntent();
        String title = intent.getStringExtra("NEWS_TITLE");
        setTitle(title);
        btn = (Button) findViewById(R.id.btn_go_detail);
        //  btn.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //웹뷰 띄우기//
                WebViewFragment fragment = new WebViewFragment();

                Bundle bundle = new Bundle();
                bundle.putString("URL", "https://github.com/seochangwook/NewsingIT_Project");
                fragment.setArguments(bundle);

                //TO DO//
                //fragment를 덮어씌웠을 때//
                //1. 버튼이 사라져야 함
                //2. 메뉴가 바뀌어야 함(사라져야 함)
                //3. 다시 back 했을 때 원상 복구 되어야 함
                // btn.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.news_container, fragment)
                        .addToBackStack(null)
                        .commit();

                Toast.makeText(SelectNewsDetailActivity.this, "해당 뉴스 링크로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        //Dummy Data 설정//
        set_Dummy_ScrapFolder_Date();
    }

    public void set_Dummy_ScrapFolder_Date() {
        //첫번째 폴더//
        ScrapFolderListData new_folderdata_1 = new ScrapFolderListData();

        new_folderdata_1.set_scrap_folder_list_data("사회이슈");

        scrapfolderData.scrapfolderlist.add(new_folderdata_1);

        //두번째 폴더//
        ScrapFolderListData new_folderdata_2 = new ScrapFolderListData();

        new_folderdata_2.set_scrap_folder_list_data("정치");

        scrapfolderData.scrapfolderlist.add(new_folderdata_2);

        //세번째 폴더//
        ScrapFolderListData new_folderdata_3 = new ScrapFolderListData();

        new_folderdata_3.set_scrap_folder_list_data("게임");

        scrapfolderData.scrapfolderlist.add(new_folderdata_3);

        scrapfolderListAdapter.set_ScrapFolderList(scrapfolderData); //설정.//
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("message", "onresume");
        btn.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("message", "onstop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrap, menu); //xml로 작성된 메뉴를 팽창//
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == R.id.scrap_news) {
            Toast.makeText(SelectNewsDetailActivity.this, "뉴스 스크랩 하기", Toast.LENGTH_SHORT).show();

            //createNewsScrap이 없어서 임시로 유사한 수정 화면으로 이동//
            /*Intent intent = new Intent(SelectNewsDetailActivity.this, CreateScrapContentActivity.class);

            startActivity(intent);*/

            scrap_folder_popup.showAtLocation(findViewById(R.id.scrap_news), Gravity.BOTTOM, 0, 0);
        }

        return super.onOptionsItemSelected(item);
    }


}
