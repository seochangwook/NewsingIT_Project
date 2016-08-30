package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class UserSelectScrapContentActivity extends AppCompatActivity {
//    TextView newsContentView, myContentView;

    String is_me; //나에 대한 스크랩인지, 다르 사람의 스크랩인지 구분 플래그//

    /**
     * 해시태그 관련 변수
     **/
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    List<String> tags = new ArrayList<>();
    private TagGroup mBeautyTagGroup; //태그를 나타낼 스타일 뷰//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select_scrap_content_activity_layout);

        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_scrap_beauty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        is_me = intent.getStringExtra("KEY_USER_IDENTIFY_FLAG");

        if (is_me.equals("1")) //1이면 다른 사용자의 스크랩 리스트//
        {
            Log.d("whowho : ", "other user");
        } else if (is_me.equals("0")) {
            Log.d("whowho : ", "me");
        }

        set_Tag(); //태그 설정.//
    }

    public void set_Tag() {
        /** 해시태그 설정 **/
        tags.add("서창욱");
        tags.add("임지수");
        tags.add("뉴스잉");

        //해시태그 레이아웃에 추가//
        tag_layout_array.add("#" + tags.get(0).toString());
        tag_layout_array.add("#" + tags.get(1).toString());
        tag_layout_array.add("#" + tags.get(2).toString());

        mBeautyTagGroup.setTags(tag_layout_array);
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
            Toast.makeText(UserSelectScrapContentActivity.this, "뉴스 스크랩 공유", Toast.LENGTH_SHORT).show();
        }else if(item_id == R.id.setting_scrap){
            Toast.makeText(UserSelectScrapContentActivity.this, "뉴스 스크랩 설정", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserSelectScrapContentActivity.this, EditScrapContentActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
