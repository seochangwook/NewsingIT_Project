package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;
import me.gujun.android.taggroup.TagGroup;

public class CreateScrapContentActivity extends AppCompatActivity implements TagsEditText.TagsEditListener {

    private static final String TAG = "Tap_Sample_Activity";
    private static Boolean is_private = false; //기본적으로 비활성화 상태로 구성//

    TextInputLayout textInputLayout;
    AppCompatEditText appCompatEditText;
    Button tag_enroll_button; //태그 등록버튼.//
    List<String> tag_array = new ArrayList<>(); //태그배열(원본 에디터에서 가져온 데이터)//
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    /**
     * 해시태그 관련 변수
     **/
    private TagsEditText mTagsEditText; //태그를 지정할 수 있는 에디트 텍스트//
    private TagGroup mBeautyTagGroup; //태그를 나타낼 스타일 뷰//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_scrap_content_activity_layout);

        tag_enroll_button = (Button) findViewById(R.id.btn_tag_create);
        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** 해시테그 설정 **/
        mTagsEditText.setHint("태그를 등록해보세요.");
        mTagsEditText.setTagsWithSpacesEnabled(true);
        mTagsEditText.setTagsListener(CreateScrapContentActivity.this);
        mTagsEditText.setThreshold(1);

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

        /** 태그 에디터 설정 **/
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setCounterMaxLength(100);

        tag_enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateScrapContentActivity.this, "해시 태그 등록", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < tag_array.size(); i++) {
                    Log.d("tag name : ", "" + tag_array.get(i).toString());
                }

                int last_index = tag_array.size();

                if (last_index > 0) {
                    String original_tag_str = tag_array.get(last_index - 1).toString().replace("[", ""); //양쪽 공백제거.//
                    original_tag_str = original_tag_str.replace("]", "");

                    Log.d("original str : ", original_tag_str);

                    String str_data_array[] = original_tag_str.split(",");

                    for (int i = 0; i < str_data_array.length; i++) {
                        tag_layout_array.add("#" + str_data_array[i].trim());
                    }

                    for (int i = 0; i < tag_layout_array.size(); i++) {
                        //해시태그 모양 만들기.//
                        Log.d("array: ", "#" + tag_layout_array.get(i).toString());
                    }

                    //만들어진 태그값을 전용 레이아웃에 구성//
                    //mTagGroup.setTags(str_data_array);
                    mBeautyTagGroup.setTags(tag_layout_array);

                    //초기화.//
                    tag_layout_array.clear();
                    mTagsEditText.setText("");
                    mTagsEditText.setTags(null); //저장된 태그 내용 초기화.//
                } else {
                    Log.d("ERROR : ", "태그정보가 없습니다");
                }
            }
        });
    }

    /**
     * 태그 입력 이벤트 리스너
     **/
    public void onTagsChanged(Collection<String> tags) {
        Log.d(TAG, "Tags changed: ");
        Log.d(TAG, Arrays.toString(tags.toArray()));

        //Collection된 태그의 정보를 배열로 이동.//
        tag_array.addAll(Arrays.asList(String.valueOf(tags)));
    }

    /**
     * 태그 입력 이벤트 리스너
     **/
    public void onEditingFinished() {
        Log.d(TAG, "OnEditing finished");
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
            if (is_private == false) {
                Toast.makeText(CreateScrapContentActivity.this, "스크랩 잠금", Toast.LENGTH_SHORT).show();

                item.setIcon(android.R.drawable.ic_lock_lock);

                is_private = true;
            } else if (is_private == true) {
                Toast.makeText(CreateScrapContentActivity.this, "스크랩 해제", Toast.LENGTH_SHORT).show();

                item.setIcon(R.mipmap.sample_unlock_image);

                is_private = false;
            }
        } else if (item_id == R.id.getimage) {
            Toast.makeText(CreateScrapContentActivity.this, "이미지 가져오기", Toast.LENGTH_SHORT).show();
        } else if (item_id == R.id.create_scrap)
        {
            Toast.makeText(CreateScrapContentActivity.this, "생성 완료", Toast.LENGTH_SHORT).show();

            //네트워크로 데이터를 보낸다.//

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}