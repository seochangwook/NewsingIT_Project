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

public class UserSelectScrapContentActivity extends AppCompatActivity {
    String is_me; //나에 대한 스크랩인지, 다르 사람의 스크랩인지 구분 플래그//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_select_scrap_content_activity_layout);

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

        Intent intent = getIntent();

        is_me = intent.getStringExtra("KEY_USER_IDENTIFY_FLAG");

        if (is_me.equals("1")) //1이면 다른 사용자의 스크랩 리스트//
        {
            Log.d("whowho : ", "other user");
        } else if (is_me.equals("0")) {
            Log.d("whowho : ", "me");
        }
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
