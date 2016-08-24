package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserScrapContentListActivity extends AppCompatActivity {
    String folder_name;

    TextView scrapt_content_text;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_scrap_content_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        button = (Button) findViewById(R.id.click_button);
        scrapt_content_text = (TextView) findViewById(R.id.name_list_str);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        folder_name = intent.getStringExtra("KEY_FOLDER_NAME");

        /** Title명 설정 **/
        setTitle(folder_name + " News");

        scrapt_content_text.setText(folder_name + " 스크랩 리스트(리사이클뷰 생략)");

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserScrapContentListActivity.this, UserSelectScrapContentActivity.class);

                startActivity(intent);
            }
        });
    }

}
