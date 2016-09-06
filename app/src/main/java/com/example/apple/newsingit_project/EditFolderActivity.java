package com.example.apple.newsingit_project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditFolderActivity extends AppCompatActivity {
    /**
     * Intent 관련 변수
     **/
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
    private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
    private static final String KEY_FOLDER_IMG = "KEY_FOLDER_IMG";
    private static final String KEY_FOLDER_LOCKED = "KEY_FOLDER_LOCKED";

    /** startActivityForResult요청 값 **/
    private static final int RC_SINGLE_IMAGE = 2;

    ImageButton image_select_button;
    Switch private_select_switch;
    TextView nameView;

    /**
     * PopupWindow 화면관련
     **/
    PopupWindow image_select_popup;
    View image_select_popup_view;

    Button select_gallery_button;
    Button camera_picture_button;

    TextView deleteView;

    String folder_id;
    String folder_name;
    boolean is_private;
    String folder_imageUrl;

    File uploadFile = null; //이미지도 하나의 파일이기에 파일로 만든다.//
    String path = null;
    ImageView select_image_thumbnail;
    ImageView folder_imageview;

    /**
     * Network관련 변수
     **/

    NetworkManager networkManager;
    private Callback requestdeletefolderCallback = new Callback() {
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

    private Callback requestEditFolderCallback = new Callback() {
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
        setContentView(R.layout.activity_edit_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        image_select_button = (ImageButton) findViewById(R.id.img_btn_edit_folder_select_img);
        private_select_switch = (Switch) findViewById(R.id.edit_folder_private_switch_button);
        deleteView = (TextView) findViewById(R.id.delete_folder);
        nameView = (TextView) findViewById(R.id.folder_edit_name_edittext);
        folder_imageview = (ImageView) findViewById(R.id.folder_edit_imageview);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        folder_id = intent.getStringExtra(KEY_FOLDER_ID);
        folder_name = intent.getStringExtra(KEY_FOLDER_NAME);
        folder_imageUrl = intent.getStringExtra(KEY_FOLDER_IMG);
        is_private = intent.getBooleanExtra(KEY_FOLDER_LOCKED, false);

        /** Popup 화면 설정 **/
        image_select_popup_view = getLayoutInflater().inflate(R.layout.image_select_popup, null);

        //팝업창 내부에 있는 위젯 정의.//
        select_gallery_button = (Button) image_select_popup_view.findViewById(R.id.select_gallery_button);
        camera_picture_button = (Button) image_select_popup_view.findViewById(R.id.camera_picture_button);

        //팝업창 설정.//
        image_select_popup = new PopupWindow(image_select_popup_view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        image_select_popup.setTouchable(true);
        image_select_popup.setOutsideTouchable(true);
        image_select_popup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        image_select_popup.setAnimationStyle(R.style.PopupAnimationBottom);
        image_select_popup.getContentView().setFocusableInTouchMode(true);
        image_select_popup.getContentView().setFocusable(true);

        /** TitleBar 설정 **/
        setTitle(getResources().getString(R.string.title_activity_folder_create));

        //사용자에게 기존 폴더 정보를 보여줌//
        nameView.setText(folder_name);

        networkManager = NetworkManager.getInstance();

        if (folder_imageUrl.equals("default")) {
            Picasso.with(this)
                    .load(R.mipmap.no_image)
                    .transform(new CropCircleTransformation())
                    .into(folder_imageview); //into로 보낼 위젯 선택.//
        } else {
            Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

            picasso.load(folder_imageUrl)
                    .into(folder_imageview);
        }

        private_select_switch.setChecked(is_private);

        /** HomeAsUpEnableButton 관련 **/
        //back 버튼 추가//
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //락 스위치에 대한 이벤트 처리//
        private_select_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_check) {
                if (is_check == true) {
                    is_private = true;
                } else if (is_check == false) {
                    is_private = false;
                }
            }
        });

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(EditFolderActivity.this, "폴더를 삭제합니다.", Toast.LENGTH_SHORT).show();

                String delete_folder_id = folder_id; //폴더를 삭제할려면 폴더의 id가 필요하다.//

                //삭제요청//
                delete_folder(delete_folder_id);

                finish();
            }
        });

        image_select_button.setOnClickListener(new View.OnClickListener() //폴더 이미지 선택 버튼//
        {
            @Override
            public void onClick(View view) {
                //팝업창을 띄운다.//
                Toast.makeText(EditFolderActivity.this, "이미지를 선택합니다.", Toast.LENGTH_SHORT).show();

                image_select_popup.showAtLocation(findViewById(R.id.img_btn_edit_folder_select_img), Gravity.CENTER, 0, 0);
            }
        });

        /** PopupMenu 이벤트 처리 **/
        select_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditFolderActivity.this, "갤러리에서 이미지 선택", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RC_SINGLE_IMAGE);
            }
        });

        camera_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditFolderActivity.this, "카메라 버튼", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void editFolderRequest(String folderId) {
        String name = nameView.getText().toString();
        boolean lock = is_private;

        //파일 전송을 위한 설정.//
        MediaType mediaType = MediaType.parse("image/jpeg");

        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("users")
                .addPathSegment("me")
                .addPathSegment("categories")
                .addPathSegment(folderId);

        /** 파일 전송이므로 MultipartBody 설정 **/
        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name);

        //비공개, 공개여부에 의해서 1/0으로 판단.//
        if (lock == true) //잠금모드 활성화//
        {
            multipart_builder.addFormDataPart("locked", "1"); //true//
        } else if (lock == false) //잠금모드 비활성화//
        {
            multipart_builder.addFormDataPart("locked", "0"); //false//
        }

        if (uploadFile != null) {
            //이미지 처리//
            multipart_builder.addFormDataPart("img", uploadFile.getName(),
                    RequestBody.create(mediaType, uploadFile));
        }

        //이미지가 없으면 이미지 값을 보내지 않는다.//

        RequestBody body = multipart_builder.build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .put(body)
                .build();

        client.newCall(request).enqueue(requestEditFolderCallback);
    }

    public void delete_folder(String folder_id) {
        /** 네트워크 설정을 한다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me");
        builder.addPathSegment("categories");
        builder.addPathSegment(folder_id);

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .delete(body)
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestdeletefolderCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_folder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.folder_menu_edit) {
            editFolderRequest(folder_id);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
                    Log.i("Single", "path : " + path);

                    uploadFile = new File(path);

                    Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

                    picasso.load(uploadFile)
                            .into(folder_imageview);
                }
            }
        }
    }
}
