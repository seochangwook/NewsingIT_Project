package com.example.apple.newsingit_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;
import me.gujun.android.taggroup.TagGroup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateScrapContentActivity extends AppCompatActivity implements TagsEditText.TagsEditListener {

    private static final String TAG = "json control";
    private static final String NEWS_ID = "NEWS_ID";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_NEWS_AUTHOR = "KEY_NEWS_AUTHOR";
    private static final String KEY_NEWS_WRITE_TIME = "KEY_NEWS_WRITE_TIME";
    private static final String KEY_NEWS_CONTENT = "KEY_NEWS_CONTENT";
    private static final String KEY_NEWS_IMGURL = "KEY_NEWS_IMGURL";
    static boolean is_touch_tag_input = false;
    private static Boolean is_private = false; //기본적으로 비활성화 상태로 구성//
    EditText scrapTitleEdittext;
    TextInputLayout textInputLayout;
    AppCompatEditText appCompatEditText;
    ImageButton tagEnrollButton; //태그 등록버튼.//
    List<String> tag_array = new ArrayList<>(); //태그배열(원본 에디터에서 가져온 데이터)//
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    FontManager fontManager;
    /**
     * Preview관련 위젯
     **/
    ImageView newsPreviewImage;
    TextView newsPreviewTitle;
    TextView newsPreviewWriteTime;
    TextView newsPreviewContent;
    TextView newsPreviewAuthor;
    /**
     * 스크랩 생성 관련 필요 변수
     **/
    String news_id;
    String folder_id;
    String news_preview_image_str;
    String news_preview_title_str;
    String news_preview_write_time_str;
    String news_preview_content_str;
    String news_preview_author_str;
    String tag_data_str[];
    String str_data_sample_tag_array[];
    NetworkManager networkManager;
    /**
     * 해시태그 관련 변수
     **/
    private TagsEditText mTagsEditText; //태그를 지정할 수 있는 에디트 텍스트//
    private TagGroup mBeautyTagGroup; //태그를 나타낼 스타일 뷰//
    private Callback requestcreatescrapcontentcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_scrap_content_activity_layout);

        fontManager = new FontManager(CreateScrapContentActivity.this);

        is_touch_tag_input = false;

        tagEnrollButton = (ImageButton) findViewById(R.id.btn_tag_create);
        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);
        scrapTitleEdittext = (EditText) findViewById(R.id.editText4);
        appCompatEditText = (TextInputEditText) findViewById(R.id.text_layout_edittext_create_scrap);

        newsPreviewImage = (ImageView) findViewById(R.id.news_preview_image);
        newsPreviewTitle = (TextView) findViewById(R.id.news_preview_title);
        newsPreviewWriteTime = (TextView) findViewById(R.id.news_preview_write_time);
        newsPreviewContent = (TextView) findViewById(R.id.news_preview_content);
        newsPreviewAuthor = (TextView) findViewById(R.id.news_preview_author);

        scrapTitleEdittext.setTypeface(fontManager.getTypefaceMediumInstance());
        appCompatEditText.setTypeface(fontManager.getTypefaceRegularInstance());
        mTagsEditText.setTypeface(fontManager.getTypefaceRegularInstance());

        newsPreviewAuthor.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewWriteTime.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewTitle.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewContent.setTypeface(fontManager.getTypefaceMediumInstance());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //인텐트로 값을 받아온다.//
        Intent intent = getIntent();

        news_id = intent.getStringExtra(NEWS_ID);
        folder_id = intent.getStringExtra(KEY_FOLDER_ID);
        news_preview_image_str = intent.getStringExtra(KEY_NEWS_IMGURL);
        news_preview_author_str = intent.getStringExtra(KEY_NEWS_AUTHOR);
        news_preview_content_str = intent.getStringExtra(KEY_NEWS_CONTENT);
        news_preview_title_str = intent.getStringExtra(NEWS_TITLE);
        news_preview_write_time_str = intent.getStringExtra(KEY_NEWS_WRITE_TIME);

        //위젯이 뿌려준다.//
        //이미지 설정.//
        if (news_preview_image_str.equals("")) {
            Picasso.with(CreateScrapContentActivity.this)
                    .load(R.mipmap.ic_image_default)
                    .into(newsPreviewImage);
        } else {
            Picasso.with(CreateScrapContentActivity.this)
                    .load(news_preview_image_str)
                    .into(newsPreviewImage);
        }

        newsPreviewTitle.setText(news_preview_title_str);
        newsPreviewWriteTime.setText(news_preview_write_time_str);
        newsPreviewContent.setText(news_preview_content_str);
        newsPreviewAuthor.setText(news_preview_author_str);

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
                if (editable.length() >= 1000) {
                    textInputLayout.setError("100글자 이하로 입력하세요");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });

        /** 태그 에디터 설정 **/
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setCounterMaxLength(1000);

        tagEnrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBeautyTagGroup.setVisibility(View.VISIBLE);

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
                        tag_layout_array.add(str_data_array[i].trim());
                    }

                    for (int i = 0; i < tag_layout_array.size(); i++) {
                        //해시태그 모양 만들기.//
                        Log.d("array: ", "#" + tag_layout_array.get(i).toString());
                    }

                    //만들어진 태그값을 전용 레이아웃에 구성//
                    mBeautyTagGroup.setTags(tag_layout_array);

                    //초기화.//
                    mTagsEditText.setText("");
                    mTagsEditText.setTags(null);
                } else {
                    Log.d("ERROR : ", "태그정보가 없습니다");
                }
            }
        });

        mTagsEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (is_touch_tag_input == false) {
                    Toast.makeText(CreateScrapContentActivity.this, "태그를 입력 후 엔터키를 이용하여 등록하세요. 등록을 모두 완료하였으면 " +
                            "등록버튼을 눌러 최종등록 하세요", Toast.LENGTH_LONG).show();
                }

                is_touch_tag_input = true;

                mBeautyTagGroup.setVisibility(View.GONE);

                return false; //false로 해야지 이벤트가 먹질 않는다.//
            }
        });
    }

    /**
     * 태그 입력 이벤트 리스너
     **/
    public void onTagsChanged(Collection<String> tags) {
        String input_tag = Arrays.toString(tags.toArray());

        //태그 중복 비교//
        Log.d(TAG, input_tag);
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

                item.setIcon(R.mipmap.ic_lock);

                is_private = true; //잠금상태.//
            } else if (is_private == true) {

                item.setIcon(R.mipmap.ic_lock_open);

                is_private = false; //해제 상태//
            }
        } else if (item_id == R.id.create_scrap)
        {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateScrapContentActivity.this);
            alertDialog.setMessage("스크랩 하시겠습니까?").setCancelable(false).setPositiveButton("생성",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //yes
                            //네트워크로 데이터를 보낸다.//
                            create_Scrap();

                            Toast.makeText(CreateScrapContentActivity.this, "스크랩 생성을 완료하였습니다", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //no

                }
            });

            AlertDialog alert = alertDialog.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void create_Scrap() {
        //저장에 필요한 변수들을 뽑는다//
        String scrap_title = scrapTitleEdittext.getText().toString();
        String scrap_content = appCompatEditText.getText().toString();
        String scrap_locked = "0";

        /** Networok 설정 **/
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        /** POST방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의(Http / Https)//
        builder.host(getResources().getString(R.string.real_server_domain)); //host정의.//
        builder.port(8080);
        builder.addPathSegment("scraps");

        //lock설정//
        if (is_private == true) {
            scrap_locked = "1";
        } else if (is_private == false) {
            scrap_locked = "0";
        }

        //여러개를 보낼려면 FormBody가 필요//
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("cid", folder_id)
                .add("ncid", news_id)
                .add("title", scrap_title)
                .add("content", scrap_content)
                .add("locked", scrap_locked);

        //태그처리//
        int array_size = tag_array.size();

        if (array_size > 0) {
            tag_data_str = new String[array_size];
            str_data_sample_tag_array = new String[array_size];

            int last_index = tag_array.size();

            if (last_index > 0) {
                String original_tag_str = tag_array.get(last_index - 1).toString().replace("[", ""); //양쪽 공백제거.//
                original_tag_str = original_tag_str.replace("]", "");

                Log.d("original str : ", original_tag_str);

                str_data_sample_tag_array = original_tag_str.split(",");
            }

            for (int i = 0; i < str_data_sample_tag_array.length; i++) {
                tag_data_str[i] = str_data_sample_tag_array[i].trim().toString();
            }

            for (int i = 0; i < str_data_sample_tag_array.length; i++) {
                Log.d("json tag", tag_data_str[i]);
                formBuilder.add("tags", tag_data_str[i]);
            }
        }

        /** RequestBody 설정(Multipart로 설정) **/
        RequestBody body = formBuilder.build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body) //POST방식 적용.//
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestcreatescrapcontentcallback);
    }
}