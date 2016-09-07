package com.example.apple.newsingit_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditScrapContentActivity extends AppCompatActivity implements TagsEditText.TagsEditListener {
    /**
     * 전달받는 값 설정
     **/
    private static final String TAG = "Tap_Sample_Activity";
    private static final String KEY_NEWS_AUTHOR = "KEY_NEWS_AUTHOR";
    private static final String NEWS_TITLE = "NEWS_TITLE";
    private static final String KEY_NEWS_WRITE_TIME = "KEY_NEWS_WRITE_TIME";
    private static final String KEY_NEWS_CONTENT = "KEY_NEWS_CONTENT";
    private static final String KEY_NEWS_IMGURL = "KEY_NEWS_IMGURL";
    private static final String KEY_SCRAP_TAGS = "KEY_TAGS";
    private static final String KEY_SCRAP_CONTENT = "KEY_SCRAP_CONTENT";
    private static final String KEY_SCRAP_TITLE = "KEY_SCRAP_TITLE";
    private static final String SCRAP_LOCK = "SCRAP_LOCK";
    private static final String SCRAP_ID = "SCRAP_ID";
    private static Boolean is_private = false; //기본적으로 비활성화 상태로 구성//

    TextInputLayout textInputLayout;
    AppCompatEditText appCompatEditText;

    ImageButton tag_edit_button; //태그 등록버튼.//
    List<String> tag_array = new ArrayList<>(); //태그배열(원본 에디터에서 가져온 데이터)//
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    /**
     * Preview관련 위젯
     **/
    ImageView news_preview_image;
    TextView news_preview_title;
    TextView news_preview_write_time;
    TextView news_preview_content;
    TextView news_preview_author;
    /**
     * 위젯 관련
     **/
    EditText scrap_title_textview;
    /**
     * 전달받아서 쓰일 변수 관련
     **/
    String scarp_title_str;
    String scrap_content_str;
    String scrap_tags[];
    String scrap_id;
    boolean scrap_isprivate;
    String news_previewimage_Url;
    String news_preview_title_str;
    String news_preview_writetime_str;
    String news_preview_author_str;
    String news_preview_content_str;
    /**
     * Network관련 변수
     **/
    String tag_data_str[];
    String str_data_sample_tag_array[];
    NetworkManager networkManager;
    private TagsEditText mTagsEditText; //태그를 지정할 수 있는 에디트 텍스트//
    private TagGroup mBeautyTagGroup; //태그를 나타낼 스타일 뷰//
    private Callback requesteditscrapcallback = new Callback() {
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_scrap_content_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tag_edit_button = (ImageButton) findViewById(R.id.btn_tag_create);
        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);
        appCompatEditText = (AppCompatEditText) findViewById(R.id.text_layout_edittext_edit_scrap);
        scrap_title_textview = (EditText) findViewById(R.id.editText4);

        /** Preview **/
        news_preview_image = (ImageView) findViewById(R.id.news_preview_image);
        news_preview_title = (TextView) findViewById(R.id.news_preview_title);
        news_preview_write_time = (TextView) findViewById(R.id.news_preview_write_time);
        news_preview_content = (TextView) findViewById(R.id.news_preview_content);
        news_preview_author = (TextView) findViewById(R.id.news_preview_author);

        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.title_activity_edit_scrap_news));

        //정보를 전달받는다.//
        Intent intent = getIntent();

        scarp_title_str = intent.getStringExtra(KEY_SCRAP_TITLE);
        scrap_content_str = intent.getStringExtra(KEY_SCRAP_CONTENT);
        scrap_id = intent.getStringExtra(SCRAP_ID);
        scrap_isprivate = intent.getBooleanExtra(SCRAP_LOCK, false);
        scrap_tags = intent.getStringArrayExtra(KEY_SCRAP_TAGS);

        news_preview_title_str = intent.getStringExtra(NEWS_TITLE);
        news_preview_author_str = intent.getStringExtra(KEY_NEWS_AUTHOR);
        news_preview_content_str = intent.getStringExtra(KEY_NEWS_CONTENT);
        news_preview_writetime_str = intent.getStringExtra(KEY_NEWS_WRITE_TIME);
        news_previewimage_Url = intent.getStringExtra(KEY_NEWS_IMGURL);

        /** 이전정보를 설정 **/
        news_preview_title.setText(news_preview_title_str);
        news_preview_author.setText(news_preview_author_str);
        news_preview_content.setText(news_preview_content_str);
        news_preview_write_time.setText(news_preview_writetime_str);

        Picasso.with(EditScrapContentActivity.this)
                .load(news_previewimage_Url)
                .into(news_preview_image);

        for (int i = 0; i < scrap_tags.length; i++) {
            tag_array.add(i, scrap_tags[i].toString());
            tag_layout_array.add("#" + scrap_tags[i].toString());
        }

        scrap_title_textview.setText(scarp_title_str);
        appCompatEditText.setText(scrap_content_str);
        is_private = scrap_isprivate;

        mBeautyTagGroup.setTags(tag_layout_array);

        /** 해시테그 설정 **/
        mTagsEditText.setHint("태그를 추가해보세요.");
        mTagsEditText.setTagsWithSpacesEnabled(true);
        mTagsEditText.setTagsListener(EditScrapContentActivity.this);
        mTagsEditText.setThreshold(1);

        //글자수 입력 제한,
        textInputLayout = (TextInputLayout) findViewById(R.id.text_layout_edit_scrap);
        appCompatEditText = (AppCompatEditText) findViewById(R.id.text_layout_edittext_edit_scrap);
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
                    textInputLayout.setError("1000글자 이하로 입력하세요");
                } else {
                    textInputLayout.setError(null);
                }
            }
        });

        textInputLayout.setCounterEnabled(true);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setCounterMaxLength(1000);

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** 태그 에디터 설정 **/
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setCounterMaxLength(100);

        tag_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBeautyTagGroup.setVisibility(View.VISIBLE);

                Toast.makeText(EditScrapContentActivity.this, "해시 태그 등록", Toast.LENGTH_SHORT).show();

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
                mBeautyTagGroup.setVisibility(View.GONE);

                return false; //false로 해야지 이벤트가 먹질 않는다.//
            }
        });
    }

    /**
     * 태그 입력 이벤트 리스너
     **/
    public void onTagsChanged(Collection<String> tags)
    {
        Log.d(TAG, "Tags changed: ");
        Log.d(TAG, Arrays.toString(tags.toArray()));

        //Collection된 태그의 정보를 배열로 이동.//
        tag_array.addAll(Arrays.asList(String.valueOf(tags)));
    }

    //스크랩 수정//
    public void EditScrap(String scrap_id) {
        //저장할 정보들을 모두 불러온다.//
        String edit_scrap_title = scrap_title_textview.getText().toString();
        String edit_scrap_content = appCompatEditText.getText().toString();

        //저장될 정보 출력//
        /*Log.d("scrap title", edit_scrap_title);
        Log.d("scrap content", edit_scrap_content);
        Log.d("scrap id", scrap_id);
        Log.d("scrap lock", "" + scrap_isprivate);*/

        //새로 입력된 해시태그 처리//
        int array_size = tag_array.size();

        if (array_size > 0) {
            tag_data_str = new String[array_size];
            str_data_sample_tag_array = new String[array_size];

            int last_index = tag_array.size();

            if (last_index > 0) {
                String original_tag_str = tag_array.get(last_index - 1).toString().replace("[", ""); //양쪽 공백제거.//
                original_tag_str = original_tag_str.replace("]", "");

                str_data_sample_tag_array = original_tag_str.split(",");
            }
        }

        //새로 추가된 태그//
        for (int i = 0; i < str_data_sample_tag_array.length; i++) {
            tag_data_str[i] = str_data_sample_tag_array[i].trim().toString();

            //Log.d("scrap tags", tag_data_str[i].toString());
        }

        //기존 입력된 태그//
        /*for (int i = 0; i < scrap_tags.length; i++) {
            Log.d("scrap tags", scrap_tags[i]);
        }

        /** 네트워크 정보 초기화 **/
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        /** PUT방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의(Http / Https)
        builder.host(getResources().getString(R.string.server_domain)); //host정의.//
        builder.addPathSegment("scraps");
        builder.addPathSegment(scrap_id); //스크랩 id를 변수로 넣어준다.//

        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", edit_scrap_title)
                .addFormDataPart("content", edit_scrap_content);

        if (scrap_isprivate == true) //true이면 1//
        {
            multipart_builder.addFormDataPart("locked", "1"); //true//
        } else if (scrap_isprivate == false) {
            multipart_builder.addFormDataPart("locked", "0"); //true//
        }

        //태그//
        for (int i = 0; i < str_data_sample_tag_array.length; i++) {
            tag_data_str[i] = str_data_sample_tag_array[i].trim().toString();

            //Log.d("scrap tags", tag_data_str[i].toString());
            multipart_builder.addFormDataPart("tags", tag_data_str[i].toString()); //true//
        }

        for (int i = 0; i < scrap_tags.length; i++) {
            //Log.d("scrap tags", scrap_tags[i]);
            multipart_builder.addFormDataPart("tags", scrap_tags[i].toString());
        }

        /** RequestBody 설정(Multipart로 설정) **/
        RequestBody body = multipart_builder.build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body) //PUT방식 적용.//
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requesteditscrapcallback);
    }

    /**
     * 태그 입력 이벤트 리스너
     **/
    public void onEditingFinished() {
        Log.d(TAG, "OnEditing finished");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_scrap, menu); //xml로 작성된 메뉴를 팽창//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == R.id.lock_scrap) {
            if (is_private == false) {
                Toast.makeText(EditScrapContentActivity.this, "스크랩 잠금", Toast.LENGTH_SHORT).show();

                item.setIcon(R.mipmap.ic_lock);

                is_private = true;
                scrap_isprivate = true;
            } else if (is_private == true) {
                Toast.makeText(EditScrapContentActivity.this, "스크랩 해제", Toast.LENGTH_SHORT).show();

                item.setIcon(R.mipmap.ic_lock_open);

                is_private = false;
                scrap_isprivate = false;
            }
        } else if (item_id == R.id.create_scrap) {
            Toast.makeText(EditScrapContentActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditScrapContentActivity.this);
            alertDialog.setMessage("수정하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //yes
                            //네트워크로 데이터를 보낸다.//
                            EditScrap(scrap_id);

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

            //  finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
