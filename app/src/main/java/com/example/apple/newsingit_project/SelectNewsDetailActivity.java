package com.example.apple.newsingit_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.newscontentdetail.NewsContentDetailRequest;
import com.example.apple.newsingit_project.data.json_data.newscontentdetail.NewsContentDetailRequestResult;
import com.example.apple.newsingit_project.data.json_data.newsdetailscrapfolderlist.NewsDetailScrapFolderListRequest;
import com.example.apple.newsingit_project.data.json_data.newsdetailscrapfolderlist.NewsDetailScrapFolderListRequestResults;
import com.example.apple.newsingit_project.data.view_data.ScrapFolderListData;
import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.widget.adapter.ScrapFolderListAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectNewsDetailActivity extends AppCompatActivity {
    private static final String NEWS_ID = "NEWS_ID";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_NEWS_AUTHOR = "KEY_NEWS_AUTHOR";
    private static final String KEY_NEWS_WRITE_TIME = "KEY_NEWS_WRITE_TIME";
    private static final String KEY_NEWS_CONTENT = "KEY_NEWS_CONTENT";
    private static final String KEY_NEWS_IMGURL = "KEY_NEWS_IMGURL";
    private static final String NEWS_KEYWORD = "NEWS_KEYWORD";
    private static final String FLAG = "FLAG";

    /**
     * 응답코드
     **/
    private static final int RC_CREATEFOLDER = 100;

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
     * UI관련 변수
     **/
    TextView newsTitleTextview;
    TextView newsAuthorTextview;
    TextView newsNtimeTextview;
    TextView newsContentTextview;
    ImageView news_content_imageview;
    String news_imageUrl;
    String news_link;
    String title;
    String news_id;
    String news_author;
    String news_ntime;
    String news_content;

    String keyword = "";
    String flag = "";


    FontManager fontManager;
    /**
     * 네트워크 관련 변수
     **/
    NetworkManager manager;
    /**
     * 폴더 리스트 관련 변수
     **/
    private FamiliarRefreshRecyclerView scrap_folder_recyclerrefreshview;
    private FamiliarRecyclerView scrap_folder_recyclerview;

    private Callback requestnewsdetailinfocallback = new Callback() {
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

            NewsContentDetailRequest newsContentDetailRequest = gson.fromJson(response_data, NewsContentDetailRequest.class);

            set_NewsDetail_Data(newsContentDetailRequest.getResult());
        }
    };
    private Callback requestscrapfolderlistcallback = new Callback() {
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

            NewsDetailScrapFolderListRequest newsDetailScrapFolderListRequest = gson.fromJson(response_data, NewsDetailScrapFolderListRequest.class);

            set_Scrapfolder_list(newsDetailScrapFolderListRequest.getResults(), newsDetailScrapFolderListRequest.getResults().length);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_news_detail_activity_layout);

        fontManager = new FontManager(SelectNewsDetailActivity.this);
        btn = (Button) findViewById(R.id.btn_go_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /** 팝업에서 사용되는 변수들 초기화 **/
        scrap_folderlist_view = getLayoutInflater().inflate(R.layout.scrap_folder_select, null);

        scrap_folder_create_button = (Button) scrap_folderlist_view.findViewById(R.id.create_scrap_folder_button);
        scrap_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) scrap_folderlist_view.findViewById(R.id.scrap_folder_rv_list);

        newsTitleTextview = (TextView) findViewById(R.id.text_news_headline);
        newsAuthorTextview = (TextView) findViewById(R.id.text_news_press);
        newsContentTextview = (TextView) findViewById(R.id.text_news_part);
        newsNtimeTextview = (TextView) findViewById(R.id.text_news_date);
        news_content_imageview = (ImageView) findViewById(R.id.img_news);

        newsTitleTextview.setTypeface(fontManager.getTypefaceMediumInstance());
        newsAuthorTextview.setTypeface(fontManager.getTypefaceRegularInstance());
        newsNtimeTextview.setTypeface(fontManager.getTypefaceRegularInstance());
        newsContentTextview.setTypeface(fontManager.getTypefaceRegularInstance());

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
        scrap_folder_recyclerrefreshview.setLoadMoreView(new LoadMoreView(this, 3));
        scrap_folder_recyclerrefreshview.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        scrap_folder_recyclerrefreshview.setLoadMoreEnabled(true); //등록//

        scrap_folder_recyclerview = scrap_folder_recyclerrefreshview.getFamiliarRecyclerView();
        scrap_folder_recyclerview.setItemAnimator(new DefaultItemAnimator());
        scrap_folder_recyclerview.setHasFixedSize(true);

        /** 폴더 데이터 클래스 초기화 및 어댑터 초기화 **/
        scrapfolderData = new ScrapFolderListData();
        scrapfolderListAdapter = new ScrapFolderListAdapter(this);

        /** 폴더 리스트 EmptyView **/
        View emptyview = getLayoutInflater().inflate(R.layout.view_scrapfolder_emptyview, null);

        /** EmptyView 위젯 **/
        TextView empty_msg_text = (TextView) emptyview.findViewById(R.id.empty_msg);
        empty_msg_text.setTypeface(fontManager.getTypefaceRegularInstance());

        scrap_folder_recyclerview.setEmptyView(emptyview, true);

        /** 폴더 리스트뷰 Refresh 이벤트 등록 **/
        scrap_folder_recyclerrefreshview.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        scrap_folder_recyclerrefreshview.pullRefreshComplete();

                        init_scrap_folder_list();

                        get_ScrapFolder_Data();
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
                String folder_id = "" + scrapfolderData.scrapfolderlist.get(position).get_scrap_folder_id();

                Intent intent = new Intent(SelectNewsDetailActivity.this, CreateScrapContentActivity.class);

                //필요한 정보를 넘겨준다.//
                //기존 프리뷰로 사용할 정보들을 넘겨주기.(뉴스 이미지, author, date, title, content)//
                intent.putExtra(KEY_FOLDER_ID, folder_id);
                intent.putExtra(KEY_NEWS_AUTHOR, news_author);
                intent.putExtra(KEY_NEWS_CONTENT, news_content);
                intent.putExtra(KEY_NEWS_IMGURL, news_imageUrl);
                intent.putExtra(NEWS_TITLE, title);
                intent.putExtra(NEWS_ID, news_id);
                intent.putExtra(KEY_NEWS_WRITE_TIME, news_ntime);

                startActivity(intent);

                scrap_folder_popup.dismiss();
            }
        });

        scrap_folder_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectNewsDetailActivity.this, CreateFolderActivity.class);

                startActivityForResult(intent, RC_CREATEFOLDER);
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

        title = intent.getStringExtra(NEWS_TITLE);
        news_id = intent.getStringExtra(NEWS_ID);
        flag = intent.getStringExtra(FLAG);

        if (flag.equals("1")) //정상경로로 접근//
        {
            keyword = intent.getStringExtra(NEWS_KEYWORD);

            setTitle(keyword);
        } else if (flag.equals("0")) //0이면 검색경로로 들어온 경우//
        {
            setTitle("뉴스검색");
        }

        newsTitleTextview.setText(title);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news_link));
                startActivity(intent);
            }
        });

        get_NewsDetail_info(news_id); //뉴스 세부정보//

        get_ScrapFolder_Data();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CREATEFOLDER) {
                Toast.makeText(SelectNewsDetailActivity.this, "폴더 추가를 완료하였습니다.", Toast.LENGTH_SHORT).show();

                init_scrap_folder_list();

                get_ScrapFolder_Data();
            }
        }
    }

    public void init_scrap_folder_list() {
        scrapfolderData.scrapfolderlist.clear();

        scrapfolderListAdapter.init_data(scrapfolderData);
    }

    public void get_ScrapFolder_Data() {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/
        manager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //나의 폴더 리스트이기에 me//
        builder.addPathSegment("categories");

        builder.addQueryParameter("usage", "scrap");
        builder.addQueryParameter("page", "1");
        builder.addQueryParameter("count", "20");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestscrapfolderlistcallback);
    }

    public void set_Scrapfolder_list(final NewsDetailScrapFolderListRequestResults newsDetailScrapFolderListRequestResults[], final int news_detail_folderlist_size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NewsDetailScrapFolderListRequestResults> newsContentDetailRequestResultList = new ArrayList<>();

                    newsContentDetailRequestResultList.addAll(Arrays.asList(newsDetailScrapFolderListRequestResults));

                    for (int i = 0; i < news_detail_folderlist_size; i++) {
                        ScrapFolderListData new_folderdata = new ScrapFolderListData();

                        new_folderdata.set_scrap_folder_list_data(newsContentDetailRequestResultList.get(i).getName());
                        new_folderdata.set_scrap_folder_id(newsContentDetailRequestResultList.get(i).getId());

                        scrapfolderData.scrapfolderlist.add(new_folderdata);
                    }

                    scrapfolderListAdapter.set_ScrapFolderList(scrapfolderData); //설정.//
                }
            });
        }
    }

    public void get_NewsDetail_info(String news_id) {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/
        manager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("newscontents");
        builder.addPathSegment(news_id);

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestnewsdetailinfocallback);
    }

    public void set_NewsDetail_Data(final NewsContentDetailRequestResult newsContentDetailRequestResult) {
        //실제 데이터에 네트워크로 받아온 값을 할당.//
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    news_imageUrl = newsContentDetailRequestResult.getImg_url();
                    news_author = newsContentDetailRequestResult.getAuthor();
                    news_link = newsContentDetailRequestResult.getLink();
                    news_ntime = newsContentDetailRequestResult.getNtime();
                    news_content = newsContentDetailRequestResult.getContent();

                    //이미지가 없으면 "" //
                    if (news_imageUrl.equals("")) {
                        Picasso.with(SelectNewsDetailActivity.this)
                                .load(R.mipmap.ic_image_default)
                                .into(news_content_imageview);
                    } else {
                        //이미지 설정.//
                        Picasso.with(SelectNewsDetailActivity.this)
                                .load(news_imageUrl)
                                .into(news_content_imageview);
                    }

                    newsAuthorTextview.setText(news_author);
                    newsContentTextview.setText(news_content);
                    newsNtimeTextview.setText(news_ntime);
                }
            });
        }
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
            scrap_folder_popup.showAtLocation(findViewById(R.id.scrap_news), Gravity.BOTTOM, 0, 0); //팝업창 띄우기//
        }
        if (item_id == R.id.share_news) {

            String scrap_title = newsTitleTextview.getText().toString();
            String scrap_content = newsContentTextview.getText().toString();

            Intent msg = new Intent(Intent.ACTION_SEND);

            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_SUBJECT, scrap_title);
            msg.putExtra(Intent.EXTRA_TEXT, scrap_content);
            msg.setType("text/plain");

            startActivity(Intent.createChooser(msg, "Newsing Share"));
        }

        return super.onOptionsItemSelected(item);
    }
}
