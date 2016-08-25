package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectNewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_news_detail_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        String title = intent.getStringExtra("NEWS_TITLE");
        setTitle(title);

        Button btn = (Button)findViewById(R.id.btn_go_detail);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //웹뷰 띄우기//


                Toast.makeText(SelectNewsDetailActivity.this, "해당 뉴스 링크로 이동", Toast.LENGTH_SHORT).show();
            }
        });

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
            Intent intent = new Intent(SelectNewsDetailActivity.this, CreateScrapContentActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}
