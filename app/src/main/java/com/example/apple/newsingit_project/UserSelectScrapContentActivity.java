package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class UserSelectScrapContentActivity extends AppCompatActivity {

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_scrap, menu); //xml로 작성된 메뉴를 팽창//
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
