package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.data.json_data.selectscrapcontent.SelectScrapContentRequest;
import com.example.apple.newsingit_project.data.json_data.selectscrapcontent.SelectScrapContentRequestResult;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserSelectScrapContentActivity extends AppCompatActivity {
    /**
     * 전달변수
     **/
    private static final String NEWS_ID = "NEWS_ID";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_NEWS_AUTHOR = "KEY_NEWS_AUTHOR";
    private static final String KEY_NEWS_WRITE_TIME = "KEY_NEWS_WRITE_TIME";
    private static final String KEY_NEWS_CONTENT = "KEY_NEWS_CONTENT";
    private static final String KEY_NEWS_IMGURL = "KEY_NEWS_IMGURL";
    private static final String KEY_SCRAP_TAGS = "KEY_TAGS";
    private static final String KEY_SCRAP_CONTENT = "KEY_SCRAP_CONTENT";
    private static final String KEY_SCRAP_TITLE = "KEY_SCRAP_TITLE";
    private static final String SCRAP_LOCK = "SCRAP_LOCK";
    private static final String SCRAP_ID = "SCRAP_ID";
    private static final String KEY_USER_IDENTIFY_FLAG = "KEY_USER_IDENTIFY_FLAG";
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_TAGSEARCH_FLAG = "KEY_TAGSEARCH_FLAG";

    String is_me; //나에 대한 스크랩인지, 다른 사람의 스크랩인지 구분 플래그//
    String scrapId;
    boolean scrap_isprivate = false;

    /**
     * 해시태그 관련 변수
     **/
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    List<String> tags = new ArrayList<>();
    TextView titleView, ncTitleView, ncContentView, contentView, authorView, likeView, news_write_date;
    ImageView news_imageview;
    String nc_imageUrl = null; //후엔 디폴트 이미지 경로 저장.//
    NetworkManager networkManager;
    private TagGroup mBeautyTagGroup; //태그를 나타낼 스타일 뷰//
    private ProgressDialog pDialog;

    private Callback requestSelectScrapContentCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            if (is_me.equals("1")) //상대방 스크랩을 들어가는 경우 비공개 처리를 해준다.//
            {
                String response_parsing = responseData.substring(11, 21);

                Log.d("json control", response_parsing);

                if (response_parsing.equals("비공개 스크랩입니다")) {
                    if (this != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserSelectScrapContentActivity.this);
                                alertDialog.setTitle("Newsing Info")
                                        .setMessage("해당 스크랩은 비공개처리 입니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //yes
                                                        finish();
                                                    }
                                                });

                                AlertDialog alert = alertDialog.create();
                                alert.show();
                            }
                        });
                    }
                } else //사용자 스크랩이면서 비공개가 아닌 경우//
                {
                    Gson gson = new Gson();

                    SelectScrapContentRequest scrapContentListRequest = gson.fromJson(responseData, SelectScrapContentRequest.class);

                    setData(scrapContentListRequest.getResult());
                }
            } else if (is_me.equals("0"))//내 스크랩일때는 비공개처리를 해줄 필요 없다.//
            {
                Gson gson = new Gson();

                SelectScrapContentRequest scrapContentListRequest = gson.fromJson(responseData, SelectScrapContentRequest.class);

                setData(scrapContentListRequest.getResult());
            }
        }
    };

    private void getSelectScrapContentNetworkData(String id) {
        showpDialog();

        Log.d("json control", "srap id:" + id);
        Log.d("json control", is_me);

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("scraps")
                .addPathSegment("" + id); //id값 intent로 받아와야 함

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestSelectScrapContentCallback);

        hidepDialog();
    }

    private void setData(final SelectScrapContentRequestResult result) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<String> tagList = new ArrayList<String>();
                    tagList.addAll(Arrays.asList(result.getTags()));

                    titleView.setText(result.getTitle());
                    ncTitleView.setText(result.getNc_title());
                    ncContentView.setText(result.getNc_contents());
                    contentView.setText(result.getContent());
                    likeView.setText("" + result.getFavorite_cnt());
                    news_write_date.setText(result.getNc_ntime());
                    authorView.setText(result.getNc_author());

                    for (int i = 0; i < result.getTags().length; i++) {
                        tags.add(tagList.get(i));
                        tag_layout_array.add("#" + tags.get(i).toString());
                    }

                    mBeautyTagGroup.setTags(tag_layout_array);

                    //이미지 설정//
                    nc_imageUrl = result.getNc_img_url();
                    //nc_imageUrl = "https://my-project-1-1470720309181.appspot.com/displayimage?imageid=AMIfv95i7QqpWTmLDE7kqw3txJPVAXPWCNd3Mz4rfBlAZ8HVZHmvjqQGlFy5oz1pWgUpxnwnXOrebTBd7nHoTaVUngSzFilPTtbelOn1SwPuBMt_IgtFRKAt3b0oPblW0j542SFVZHCNbSkb4d9P9U221kumJhC_ZwCO85PXq5-oMdxl6Yn6-F4";

                    Picasso.with(UserSelectScrapContentActivity.this)
                            .load(nc_imageUrl)
                            .into(news_imageview);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select_scrap_content_activity_layout);

        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_scrap_beauty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleView = (TextView) findViewById(R.id.text_scrap_title);
        ncTitleView = (TextView) findViewById(R.id.text_scrap_nctitle);
        ncContentView = (TextView) findViewById(R.id.text_scrap_nccontent);
        contentView = (TextView) findViewById(R.id.text_scrap_my_content);
        authorView = (TextView) findViewById(R.id.text_scrap_press);
        likeView = (TextView) findViewById(R.id.text_scrap_like_cnt);
        news_write_date = (TextView) findViewById(R.id.text_scrap_date);
        news_imageview = (ImageView) findViewById(R.id.img_scrap_nc);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        scrapId = intent.getStringExtra(SCRAP_ID); //스크랩의 상세 정보를 검색하기 위해서 id값을 전달받는다.//
        is_me = intent.getStringExtra(KEY_USER_IDENTIFY_FLAG);

        //메뉴를 다르게 해주기 위해서 다른 사용자와 나의 경우를 구분//
        if (is_me.equals("1")) //1이면 다른 사용자의 스크랩 리스트//
        {
            Log.d("whowho : ", "other user");
        } else if (is_me.equals("0")) {
            Log.d("whowho : ", "me");
        }

        //태그 선택 이벤트 처리//
        mBeautyTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String select_tag) {
                Log.d("json control", "click tag:" + select_tag);

                //현재 스크랩 상세화면을 종료 후 검새조건 태그인 경우로 해서 스크랩 리스트로 이동//
                Intent intent = new Intent(UserSelectScrapContentActivity.this, UserScrapContentListActivity.class);

                //필요한 정보를 이동//
                intent.putExtra(KEY_FOLDER_NAME, select_tag); //태그명을 전달.//
                intent.putExtra(KEY_USER_IDENTIFY_FLAG, "1"); //검색은 다른 사용자의 내용들을 보는것이니 외부사용자로 간다.//
                intent.putExtra(KEY_TAGSEARCH_FLAG, "TAG"); //태그로 검색한다는 플래그.//

                startActivity(intent);

                finish();
            }
        });

        getSelectScrapContentNetworkData(scrapId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (is_me.equals("0")) //나에 스크랩에 접근한 경우//
        {
            getMenuInflater().inflate(R.menu.menu_select_scrap, menu); //xml로 작성된 메뉴를 팽창//
        } else if (is_me.equals("1")) //상대방 스크랩에 접근한 경우.//
        {
            getMenuInflater().inflate(R.menu.menu_user_select_scrap, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == R.id.share_scrap) {

            String scrap_title = titleView.getText().toString();
            String scrap_content = contentView.getText().toString();

            Intent msg = new Intent(Intent.ACTION_SEND);

            msg.addCategory(Intent.CATEGORY_DEFAULT);
            msg.putExtra(Intent.EXTRA_SUBJECT, scrap_title);
            msg.putExtra(Intent.EXTRA_TEXT, scrap_content);
            msg.setType("text/plain");

            startActivity(Intent.createChooser(msg, "Newsing Share"));
        } else if (item_id == R.id.setting_scrap) {

            //내가 스크랩한 정보//
            String scrap_name = titleView.getText().toString();
            String scrap_content = contentView.getText().toString();
            boolean scrap_isprivate_val = scrap_isprivate;
            String scrap_id = "" + scrapId;
            String scrap_tag_array[];

            //태그정보를 가지고 있는 배열 생성.//
            int tag_array_size = tags.size();

            scrap_tag_array = new String[tag_array_size];

            for (int i = 0; i < tag_array_size; i++) {
                scrap_tag_array[i] = tags.get(i).toString();
            }

            //프리뷰 뉴스 정보//
            String previce_newstitle = ncTitleView.getText().toString();
            String preview_newsimage = nc_imageUrl;
            String preview_news_author = authorView.getText().toString();
            String preview_news_write_date = news_write_date.getText().toString();
            String preview_news_content = ncContentView.getText().toString();

            Log.d("scrap name", scrap_name);
            Log.d("scrap content", scrap_content);
            Log.d("scrap private", "" + scrap_isprivate);
            Log.d("scrap id", "" + scrap_id);

            for (int i = 0; i < scrap_tag_array.length; i++) {
                Log.d("scrap tags", scrap_tag_array[i]);
            }

            Intent intent = new Intent(UserSelectScrapContentActivity.this, EditScrapContentActivity.class);

            //필요한 정보를 넘겨준다.//
            intent.putExtra(NEWS_TITLE, previce_newstitle);
            intent.putExtra(KEY_NEWS_CONTENT, preview_news_content);
            intent.putExtra(KEY_NEWS_IMGURL, preview_newsimage);
            intent.putExtra(KEY_NEWS_AUTHOR, preview_news_author);
            intent.putExtra(KEY_NEWS_WRITE_TIME, preview_news_write_date);

            intent.putExtra(KEY_SCRAP_TITLE, scrap_name);
            intent.putExtra(KEY_SCRAP_CONTENT, scrap_content);
            intent.putExtra(KEY_SCRAP_TAGS, scrap_tag_array);
            intent.putExtra(SCRAP_ID, scrap_id);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}
