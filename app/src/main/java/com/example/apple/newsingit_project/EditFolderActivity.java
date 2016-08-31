package com.example.apple.newsingit_project;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class EditFolderActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        image_select_button = (ImageButton) findViewById(R.id.img_btn_create_folder_select_img);
        private_select_switch = (Switch) findViewById(R.id.create_folder_private_switch_button);
        deleteView = (TextView) findViewById(R.id.delete_folder);

        setSupportActionBar(toolbar);


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
