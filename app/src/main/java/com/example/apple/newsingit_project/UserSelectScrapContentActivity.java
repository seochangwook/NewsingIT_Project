package com.example.apple.newsingit_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.selectscrapcontent.SelectScrapContentRequest;
import com.example.apple.newsingit_project.data.json_data.selectscrapcontent.SelectScrapContentRequestResult;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.google.gson.Gson;

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
//    TextView newsContentView, myContentView;
    String is_me; //나에 대한 스크랩인지, 다르 사람의 스크랩인지 구분 플래그//

    /**
     * 해시태그 관련 변수
     **/
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    List<String> tags = new ArrayList<>();
    TextView titleView, ncTitleView, ncContentView, contentView, authorView, likeView;
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

            Gson gson = new Gson();

            SelectScrapContentRequest scrapContentListRequest = gson.fromJson(responseData, SelectScrapContentRequest.class);

            setData(scrapContentListRequest.getResult());
        }
    };

    private void getSelectScrapContentNetworkData(int id) {

        showpDialog();

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com")
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

                    for (int i = 0; i < result.getTags().length; i++) {
                        tags.add(tagList.get(i));
                        tag_layout_array.add("#" + tags.get(i).toString());
                    }
                    mBeautyTagGroup.setTags(tag_layout_array);
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
        // authorView = (TextView)findViewById(R.id.text_scrap_press);
        likeView = (TextView) findViewById(R.id.text_scrap_like_cnt);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //textview에 scroll 추가//
//        newsContentView = (TextView) findViewById(R.id.text_scrap_content);
//        myContentView = (TextView) findViewById(R.id.text_scrap_my_content);
//        newsContentView.setMovementMethod(new ScrollingMovementMethod());
//        myContentView.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();

        int scrapId = intent.getIntExtra("SCRAP_ID", 1);

        is_me = intent.getStringExtra("KEY_USER_IDENTIFY_FLAG");

        if (is_me.equals("1")) //1이면 다른 사용자의 스크랩 리스트//
        {
            Log.d("whowho : ", "other user");
        } else if (is_me.equals("0")) {
            Log.d("whowho : ", "me");
        }

        // set_Tag(); //태그 설정.//

        getSelectScrapContentNetworkData(scrapId);

    }

//    public void set_Tag() {
//        /** 해시태그 설정 **/
//        tags.add("서창욱");
//        tags.add("임지수");
//        tags.add("뉴스잉");
//
//        //해시태그 레이아웃에 추가//
//        tag_layout_array.add("#" + tags.get(0).toString());
//        tag_layout_array.add("#" + tags.get(1).toString());
//        tag_layout_array.add("#" + tags.get(2).toString());
//
//        mBeautyTagGroup.setTags(tag_layout_array);
//    }

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
            Toast.makeText(UserSelectScrapContentActivity.this, "뉴스 스크랩 공유", Toast.LENGTH_SHORT).show();

            Intent msg = new Intent(Intent.ACTION_SEND);

            msg.addCategory(Intent.CATEGORY_DEFAULT);

            msg.putExtra(Intent.EXTRA_SUBJECT, "서창욱");

            msg.putExtra(Intent.EXTRA_TEXT, "코딩이 취미");

            msg.putExtra(Intent.EXTRA_TITLE, "제목");

            msg.setType("text/plain");

            startActivity(Intent.createChooser(msg, "공유"));
        }else if(item_id == R.id.setting_scrap){
            Toast.makeText(UserSelectScrapContentActivity.this, "뉴스 스크랩 설정", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserSelectScrapContentActivity.this, EditScrapContentActivity.class);

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
