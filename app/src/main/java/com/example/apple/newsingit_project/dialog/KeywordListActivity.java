package com.example.apple.newsingit_project.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.KeywordData;
import com.example.apple.newsingit_project.widget.adapter.KeywordListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

public class KeywordListActivity extends Activity {

    //전달할 값의 키//
    private static final String KEY_KEYWORD = "KEY_KEYWORD";
    KeywordListAdapter mAdapter;
    KeywordData keywordData;
    private FamiliarRecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyword_list_activity_layout);

        keywordData = new KeywordData();

        recyclerview = (FamiliarRecyclerView) findViewById(R.id.rv_list);

        mAdapter = new KeywordListAdapter(this);
        recyclerview.setAdapter(mAdapter);

        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {

            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String userSelectKeyword = keywordData.keywordDataList.get(position).getKeyword().toString();

                Toast.makeText(KeywordListActivity.this, "[" + userSelectKeyword + "]로 리스트 이동", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();

                intent.putExtra(KEY_KEYWORD, userSelectKeyword);

                setResult(RESULT_OK, intent);

                finish();
            }
        });

        initDummyData();
    }

    private void initDummyData() {

        String keywordDummyList[] = {"갤럭시 7", "iot", "소프트뱅크", "드론", "VR", "테슬라", "핀테크", "안드로이드", "라즈베리파이", "아이폰 7"};

        for (int i = 0; i < 10; i++) {
            KeywordData new_keywordData = new KeywordData();
            new_keywordData.keyword = keywordDummyList[i];
            keywordData.keywordDataList.add(new_keywordData);
        }

        mAdapter.setKeywordDataList(keywordData);
    }

}
