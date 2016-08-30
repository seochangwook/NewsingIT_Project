package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateScrapContentActivity extends AppCompatActivity {

    TextInputLayout textInputLayout;
    AppCompatEditText appCompatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_scrap_content_activity_layout);
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

        //글자수 입력 제한,
        textInputLayout = (TextInputLayout) findViewById(R.id.text_layout_create_scrap);
        appCompatEditText = (AppCompatEditText) findViewById(R.id.text_layout_edittext_create_scrap);
        appCompatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 100) {
                    textInputLayout.setError("100글자 이하로 입력하세요");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });

        textInputLayout.setCounterEnabled(true);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setCounterMaxLength(100);

        Button btn = (Button) findViewById(R.id.btn_tag_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateScrapContentActivity.this, "해시 태그 등록", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_scrap, menu); //xml로 작성된 메뉴를 팽창//
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == R.id.lock_scrap) {
            Toast.makeText(CreateScrapContentActivity.this, "스크랩 잠금", Toast.LENGTH_SHORT).show();
        } else if (item_id == R.id.create_scrap) {
            Toast.makeText(CreateScrapContentActivity.this, "생성 완료", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);


    }
}