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
    private static final String SCRAP_ID = "SCRAP_ID";
    private static Boolean is_private = false; //기본적으로 비활성화 상태로 구성//

    TextInputLayout textInputLayout;
    AppCompatEditText appCompatEditText;
    FontManager fontManager;

    ImageButton tag_edit_button; //태그 등록버튼.//
    List<String> tag_array = new ArrayList<>(); //태그배열(원본 에디터에서 가져온 데이터)//
    List<String> tag_layout_array = new ArrayList<>(); //태그 레이아웃//
    /**
     * Preview관련 위젯
     **/
    ImageView newsPreviewImage;
    TextView newsPreviewTitle;
    TextView newsPreviewWriteTime;
    TextView newsPreviewContent;
    TextView newsPreviewAuthor;
    /**
     * 위젯 관련
     **/
    EditText scrapTitleTextview;
    /**
     * 전달받아서 쓰일 변수 관련
     **/
    String scarp_title_str;
    String scrap_content_str;
    String scrap_tags[];
    String scrap_id;
    boolean scrap_isprivate = false;
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

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //응답메시지를 보내는 시기는 네트워크 작업이 모두 완료된 후이다.//
                        setResult(RESULT_OK);

                        Toast.makeText(EditScrapContentActivity.this, "스크랩 정보를 수정하였습니다.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_scrap_content_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        fontManager = new FontManager(EditScrapContentActivity.this);

        tag_edit_button = (ImageButton) findViewById(R.id.btn_tag_create);
        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        mBeautyTagGroup = (TagGroup) findViewById(R.id.tag_group_beauty);
        appCompatEditText = (AppCompatEditText) findViewById(R.id.text_layout_edittext_edit_scrap);
        scrapTitleTextview = (EditText) findViewById(R.id.editText4);

        /** Preview **/
        newsPreviewImage = (ImageView) findViewById(R.id.news_preview_image);
        newsPreviewTitle = (TextView) findViewById(R.id.news_preview_title);
        newsPreviewWriteTime = (TextView) findViewById(R.id.news_preview_write_time);
        newsPreviewContent = (TextView) findViewById(R.id.news_preview_content);
        newsPreviewAuthor = (TextView) findViewById(R.id.news_preview_author);

        scrapTitleTextview.setTypeface(fontManager.getTypefaceMediumInstance());
        appCompatEditText.setTypeface(fontManager.getTypefaceRegularInstance());
        mTagsEditText.setTypeface(fontManager.getTypefaceRegularInstance());

        newsPreviewAuthor.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewWriteTime.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewTitle.setTypeface(fontManager.getTypefaceMediumInstance());
        newsPreviewContent.setTypeface(fontManager.getTypefaceMediumInstance());


        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.title_activity_edit_scrap_news));

        //정보를 전달받는다.//
        Intent intent = getIntent();

        scarp_title_str = intent.getStringExtra(KEY_SCRAP_TITLE);
        scrap_content_str = intent.getStringExtra(KEY_SCRAP_CONTENT);
        scrap_id = intent.getStringExtra(SCRAP_ID);
        scrap_tags = intent.getStringArrayExtra(KEY_SCRAP_TAGS);

        news_preview_title_str = intent.getStringExtra(NEWS_TITLE);
        news_preview_author_str = intent.getStringExtra(KEY_NEWS_AUTHOR);
        news_preview_content_str = intent.getStringExtra(KEY_NEWS_CONTENT);
        news_preview_writetime_str = intent.getStringExtra(KEY_NEWS_WRITE_TIME);
        news_previewimage_Url = intent.getStringExtra(KEY_NEWS_IMGURL);

        /** 이전정보를 설정 **/
        newsPreviewTitle.setText(news_preview_title_str);
        newsPreviewAuthor.setText(news_preview_author_str);
        newsPreviewContent.setText(news_preview_content_str);
        newsPreviewWriteTime.setText(news_preview_writetime_str);

        if (news_previewimage_Url.equals("")) {
            Picasso.with(EditScrapContentActivity.this)
                    .load(R.mipmap.ic_image_default)
                    .into(newsPreviewImage);
        } else {
            Picasso.with(EditScrapContentActivity.this)
                    .load(news_previewimage_Url)
                    .into(newsPreviewImage);
        }

        for (int i = 0; i < scrap_tags.length; i++) {
            tag_array.add(i, scrap_tags[i].toString());
            tag_layout_array.add(scrap_tags[i].toString());
        }

        scrapTitleTextview.setText(scarp_title_str);
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
        textInputLayout.setCounterMaxLength(1000);


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
                mBeautyTagGroup.setVisibility(View.GONE);

                return false; //false로 해야지 이벤트가 먹질 않는다.//
            }
        });
    }

    public int Search_Tag_position(String search_tag) {
        int position = 0;

        for (int i = 0; i < tag_array.size(); i++) {
            if (search_tag.equals(tag_array.get(i).toString())) {
                break;
            }

            position++;
        }

        return position;
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
        String edit_scrap_title = scrapTitleTextview.getText().toString();
        String edit_scrap_content = appCompatEditText.getText().toString();
        String edit_scrap_locked = "0"; //기본 비공개라 설정.//

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

        if (array_size > 0) {
            //새로 추가된 태그//
            for (int i = 0; i < str_data_sample_tag_array.length; i++) {
                tag_data_str[i] = str_data_sample_tag_array[i].trim().toString();
            }
        }

        /** 네트워크 정보 초기화 **/
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        /** PUT방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의(Http / Https)
        builder.host(getResources().getString(R.string.real_server_domain)); //host정의.//
        builder.port(8080);
        builder.addPathSegment("scraps");
        builder.addPathSegment(scrap_id); //스크랩 id를 변수로 넣어준다.//
        builder.addQueryParameter("action", "udscrap");

        if (scrap_isprivate == true) //true이면 1//
        {
            edit_scrap_locked = "1";
        } else if (scrap_isprivate == false) {
            edit_scrap_locked = "0";
        }

        //여러개를 보낼려면 FormBody가 필요//
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("title", edit_scrap_title)
                .add("content", edit_scrap_content)
                .add("locked", edit_scrap_locked);

        if (array_size > 0) {
            //태그//
            for (int i = 0; i < str_data_sample_tag_array.length; i++) {
                tag_data_str[i] = str_data_sample_tag_array[i].trim().toString();

                formBuilder.add("tags", tag_data_str[i].toString()); //true//
            }
        }

        if (scrap_tags.length > 0) {
            //새로 추가된 태그//
            for (int i = 0; i < scrap_tags.length; i++) {
                //기존에 있는지 비교.(기존에 있는 것을 사용자가 입력을 해도 반영되지 않는다.)//
                boolean is_duplicate_check = Tag_duplicate_check(scrap_tags[i].toString());

                if (is_duplicate_check == false) //같은것이 한개라도 없는 경우(태그 반영)//
                {
                    formBuilder.add("tags", scrap_tags[i].toString());
                } else if (is_duplicate_check == true) //같은 것이 한개라도 있는경우//
                {
                    Log.d("tags", "duplicate tag data");
                }
            }
        }

        /** RequestBody 설정(Multipart로 설정) **/
        RequestBody body = formBuilder.build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body) //PUT방식 적용.//
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requesteditscrapcallback);
    }

    public boolean Tag_duplicate_check(String check_tag_data) {
        boolean is_check = false; //한개라도 없다는 가정//

        for (int i = 0; i < str_data_sample_tag_array.length; i++) {
            if (tag_data_str[i].equals(check_tag_data)) //같은 것이 존재//
            {
                is_check = true;

                break;
            }
        }

        return is_check;
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
                item.setIcon(R.mipmap.ic_lock);

                is_private = true;
                scrap_isprivate = true;
            } else if (is_private == true) {
                item.setIcon(R.mipmap.ic_lock_open);

                is_private = false;
                scrap_isprivate = false;
            }
        } else if (item_id == R.id.create_scrap) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditScrapContentActivity.this);
            alertDialog.setTitle("Newsing Info")
                    .setMessage("스크랩을 수정하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("수정",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //yes
                            //네트워크로 데이터를 보낸다.//
                            EditScrap(scrap_id);
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
}
