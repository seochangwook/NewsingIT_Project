package com.example.apple.newsingit_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.fontmanager.FontManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditMyInfoActivity extends AppCompatActivity {
    /**
     * 정보전달 변수
     **/
    private static final String KEY_MY_IMG = "KEY_USER_IMG";
    private static final String KEY_MY_NAME = "KEY_USER_NAME";
    private static final String KEY_MY_ABOUTME = "KEY_USER_ABOUTME";
    private static final String KEY_IMG_DEFAULT_FLAG = "KEY_IMG_DEFAULT_FLAG";

    /**
     * startActivityForResult 요청값
     **/
    private static final int RC_SINGLE_IMAGE = 2;
    /**
     * 필요한 위젯 정의
     **/
    ImageView profile_fix_imageview, get_image_button;
    EditText myNameEdit, myIntroduceEdit;
    TextView nameTextVIew, aboutMeTextView;
    FontManager fontManager;

    /**
     * 설정 정보
     **/
    String my_name, my_aboutme, my_imgUrl, key_default_img;
    /**
     * Network관련 변수
     **/
    NetworkManager networkManager;

    File uploadFile = null; //이미지도 하나의 파일이기에 파일로 만든다.//

    String path = null;

    private Callback requesteditmyinfocallback = new Callback() {
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

                        Toast.makeText(EditMyInfoActivity.this, "나의 정보 수정을 완료하였습니다", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_info_activity_layout);

        networkManager = NetworkManager.getInstance();
        fontManager = new FontManager(EditMyInfoActivity.this);

        profile_fix_imageview = (ImageView) findViewById(R.id.my_profile_imageview);
        myNameEdit = (EditText) findViewById(R.id.my_name_fix_edittext);
        myIntroduceEdit = (EditText) findViewById(R.id.my_introduce_fix_edit);
        get_image_button = (ImageButton) findViewById(R.id.select_gallery_picture);

        nameTextVIew = (TextView) findViewById(R.id.textView5);
        aboutMeTextView = (TextView) findViewById(R.id.textView6);

        nameTextVIew.setTypeface(fontManager.getTypefaceMediumInstance());
        aboutMeTextView.setTypeface(fontManager.getTypefaceMediumInstance());
        myNameEdit.setTypeface(fontManager.getTypefaceRegularInstance());
        myIntroduceEdit.setTypeface(fontManager.getTypefaceRegularInstance());

        //설정된 정보값으로 설정//
        Intent intent = getIntent();

        my_name = intent.getStringExtra(KEY_MY_NAME);
        my_aboutme = intent.getStringExtra(KEY_MY_ABOUTME);
        my_imgUrl = intent.getStringExtra(KEY_MY_IMG);
        key_default_img = intent.getStringExtra(KEY_IMG_DEFAULT_FLAG);

        myNameEdit.setText(my_name);
        myIntroduceEdit.setText(my_aboutme);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
        //파카소 라이브러리를 이용하여 이미지 로딩//
        if (key_default_img.equals("1")) {
            Picasso.with(this)
                    .load(my_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(profile_fix_imageview); //into로 보낼 위젯 선택.//
        } else if (key_default_img.equals("0")) {
            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(my_imgUrl)
                    .transform(new CropCircleTransformation())
                    .into(profile_fix_imageview);
        }

        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                //수정하지 않고 종료//
                finish();
            }
        });

        get_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RC_SINGLE_IMAGE);
            }
        });
    }

    public void edit_user() {
        /** 네트워크 작업을 한다 **/
        String edit_my_name = myNameEdit.getText().toString();
        String edit_my_introduce = myIntroduceEdit.getText().toString();

        //파일 전송을 위한 설정.//
        MediaType mediaType = MediaType.parse("image/jpeg");

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        /** POST방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의(Http / Https)
        builder.host(getResources().getString(R.string.real_server_domain)); //host정의.//
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me");
        builder.addQueryParameter("action", "no");

        /** 파일 전송이므로 MultipartBody 설정 **/
        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", edit_my_name)
                .addFormDataPart("aboutme", edit_my_introduce);

        //이미지 설정//
        if (uploadFile != null) {
            //이미지 처리//
            Log.d("image path", uploadFile.getName());

            multipart_builder.addFormDataPart("pf", uploadFile.getName(),
                    RequestBody.create(mediaType, uploadFile));
        } else {
            //이미지를 선택안할 시 아무 처리를 해주지 않는다.//
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
        client.newCall(request).enqueue(requesteditmyinfocallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SINGLE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                Cursor c = getContentResolver().query(fileUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (c.moveToNext()) {
                    path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.i("json control:", "path : " + path);

                    uploadFile = new File(path);

                    Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

                    picasso.load(uploadFile)
                            .transform(new CropCircleTransformation())
                            .into(profile_fix_imageview);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_my_info, menu); //xml로 작성된 메뉴를 팽창//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.menu_edit_my_info) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditMyInfoActivity.this);
            alertDialog.setTitle("Newsing Info")
                    .setMessage("나의 정보를 수정하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("수정",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //yes
                                    //네트워크로 데이터를 보낸다.//
                                    edit_user();
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
