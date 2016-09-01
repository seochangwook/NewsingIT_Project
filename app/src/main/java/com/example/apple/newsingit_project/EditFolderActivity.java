package com.example.apple.newsingit_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditFolderActivity extends AppCompatActivity {
    private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";

    ImageButton image_select_button;
    Switch private_select_switch;

    /**
     * PopupWindow 화면관련
     **/
    PopupWindow image_select_popup;
    View image_select_popup_view;

    Button select_gallery_button;
    Button camera_picture_button;

    TextView deleteView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        image_select_button = (ImageButton) findViewById(R.id.img_btn_create_folder_select_img);
        private_select_switch = (Switch) findViewById(R.id.create_folder_private_switch_button);
        deleteView = (TextView) findViewById(R.id.delete_folder);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final String folder_id = intent.getStringExtra(KEY_FOLDER_ID);

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

                image_select_popup.showAtLocation(findViewById(R.id.img_btn_create_folder_select_img), Gravity.CENTER, 0, 0);
            }
        });

        /** PopupMenu 이벤트 처리 **/
        select_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditFolderActivity.this, "갤러리에서 이미지 선택", Toast.LENGTH_SHORT).show();
            }
        });

        camera_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditFolderActivity.this, "카메라 버튼", Toast.LENGTH_SHORT).show();
            }
        });

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
        builder.host("ec2-52-78-89-94.ap-northeast-2.compute.amazonaws.com");
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
