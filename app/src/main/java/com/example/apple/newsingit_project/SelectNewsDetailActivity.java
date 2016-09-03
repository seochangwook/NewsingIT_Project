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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.newscontentdetail.NewsContentDetailRequest;
import com.example.apple.newsingit_project.data.json_data.newscontentdetail.NewsContentDetailRequestResult;
import com.example.apple.newsingit_project.data.json_data.newsdetailscrapfolderlist.NewsDetailScrapFolderListRequest;
import com.example.apple.newsingit_project.data.json_data.newsdetailscrapfolderlist.NewsDetailScrapFolderListRequestResults;
import com.example.apple.newsingit_project.data.view_data.ScrapFolderListData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.LoadMoreView;
import com.example.apple.newsingit_project.view.view_fragment.WebViewFragment;
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
    TextView news_headline_title_textview;
    TextView news_author_textview;
    TextView news_ntime_textview;
    TextView news_content_textview;
    ImageView news_content_imageview;
    String news_imageUrl;
    String news_link;
    String title;
    String news_id;
    String news_author;
    String news_ntime;
    String news_content;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /** 팝업에서 사용되는 변수들 초기화 **/

        scrap_folderlist_view = getLayoutInflater().inflate(R.layout.scrap_folder_select, null);

        scrap_folder_create_button = (Button) scrap_folderlist_view.findViewById(R.id.create_scrap_folder_button);
        scrap_folder_recyclerrefreshview = (FamiliarRefreshRecyclerView) scrap_folderlist_view.findViewById(R.id.scrap_folder_rv_list);

        news_headline_title_textview = (TextView) findViewById(R.id.text_news_headline);
        news_author_textview = (TextView) findViewById(R.id.text_news_press);
        news_content_textview = (TextView) findViewById(R.id.text_news_part);
        news_ntime_textview = (TextView) findViewById(R.id.text_news_date);
        news_content_imageview = (ImageView) findViewById(R.id.img_news);

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
                String folder_id = "" + scrapfolderData.scrapfolderlist.get(position).get_scrap_folder_id();

                Toast.makeText(SelectNewsDetailActivity.this, folder_name + " 폴더 선택", Toast.LENGTH_SHORT).show();

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

                startActivity(intent);
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

        news_headline_title_textview.setText(title);

        btn = (Button) findViewById(R.id.btn_go_detail);
        //  btn.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //웹뷰 띄우기//
                WebViewFragment fragment = new WebViewFragment();

                Bundle bundle = new Bundle();
                bundle.putString("URL", news_link);
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
        //set_Dummy_ScrapFolder_Date();

        get_NewsDetail_info(news_id);
        get_ScrapFolder_Data();
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
        builder.host(getResources().getString(R.string.server_domain));
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
        builder.host(getResources().getString(R.string.server_domain));
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
                    //news_imageUrl = newsContentDetailRequestResult.getImg_url();
                    news_imageUrl = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";
                    news_author = newsContentDetailRequestResult.getAuthor();
                    news_link = newsContentDetailRequestResult.getLink();
                    news_ntime = newsContentDetailRequestResult.getNtime();
                    news_content = newsContentDetailRequestResult.getContent();

                    //이미지 설정.//
                    Picasso.with(SelectNewsDetailActivity.this)
                            .load(news_imageUrl)
                            .into(news_content_imageview);

                    news_author_textview.setText(news_author);
                    news_content_textview.setText(news_content);
                    news_ntime_textview.setText(news_ntime);
                }
            });
        }
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
        if (item_id == R.id.share_news) {
            Toast.makeText(SelectNewsDetailActivity.this, "뉴스 공유 하기", Toast.LENGTH_SHORT).show();

            Intent msg = new Intent(Intent.ACTION_SEND);

            msg.addCategory(Intent.CATEGORY_DEFAULT);

            msg.putExtra(Intent.EXTRA_SUBJECT, "서창욱");

            msg.putExtra(Intent.EXTRA_TEXT, "코딩이 취미 - 뉴스 스크랩");

            msg.putExtra(Intent.EXTRA_TITLE, "제목");

            msg.setType("text/plain");

            startActivity(Intent.createChooser(msg, "공유"));

        }

        return super.onOptionsItemSelected(item);
    }


}
