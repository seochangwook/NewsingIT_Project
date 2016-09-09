package com.example.apple.newsingit_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateFolderActivity extends AppCompatActivity {
    private static final int RC_SINGLE_IMAGE = 2;
    private static final int RC_CAMERA = 1;

    ImageButton image_select_button;
    SwitchButton private_select_switch;

    //설정관련 변수//
    ImageView select_image_thumbnail;
    EditText folder_name_edittext;
    boolean is_private;
    /**
     * PopupWindow 화면관련
     **/
    PopupWindow image_select_popup;
    View image_select_popup_view;

    Button select_gallery_button;
    Button camera_picture_button;

    /**
     * Network관련 변수
     **/
    NetworkManager networkManager;

    File uploadFile = null; //이미지도 하나의 파일이기에 파일로 만든다.//

    String path = null;

    private Callback requestfoldercreatecallback = new Callback() {
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
        setContentView(R.layout.create_folder_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        image_select_button = (ImageButton) findViewById(R.id.image_select_button);
        private_select_switch = (SwitchButton) findViewById(R.id.switch_private_folder);
        select_image_thumbnail = (ImageView) findViewById(R.id.folder_imageview);
        folder_name_edittext = (EditText) findViewById(R.id.folder_name_edittext);

        private_select_switch.setBackColorRes(R.color.switch_background_color);
        private_select_switch.setThumbColorRes(R.color.switch_thumb_color);

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
        setTitle(getResources().getString(R.string.title_activity_folder_add));

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

        image_select_button.setOnClickListener(new View.OnClickListener() //폴더 이미지 선택 버튼//
        {
            @Override
            public void onClick(View view) {
                //팝업창을 띄운다.//
                Toast.makeText(CreateFolderActivity.this, "이미지를 선택합니다.", Toast.LENGTH_SHORT).show();

                image_select_popup.showAtLocation(findViewById(R.id.image_select_button), Gravity.CENTER, 0, 0);
            }
        });

        /** PopupMenu 이벤트 처리 **/
        select_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateFolderActivity.this, "갤러리에서 이미지 선택", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RC_SINGLE_IMAGE);

                //팝업창 제거//
                image_select_popup.dismiss();
            }
        });

        camera_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateFolderActivity.this, "카메라 버튼", Toast.LENGTH_SHORT).show();

                boolean is_camera_usable = checkCameraHardware(CreateFolderActivity.this); //현재 사용자가 카메라를 사용할 수 있으므로//

                if (is_camera_usable == true) {
                    //카메라를 직접 앱에 특성에 맞게 SurfaceView를 이용해서 커스텀 할 수 있다.//
                    //해당 앱에서는 간단히 Intent로 한다.//
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, RC_CAMERA);
                } else if (is_camera_usable == false) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFolderActivity.this);
                    alertDialog
                            .setTitle("Newsing Picture")
                            .setMessage("현재 다른 앱에서 카메라가 사용 중 입니다!!!")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //yes
                                            //네트워크로 데이터를 보낸다.//
                                        }
                                    });

                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        });
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

                    networkManager = NetworkManager.getInstance();

                    Picasso picasso = networkManager.getPicasso(); //피카소의 자원을 불러온다.//

                    picasso.load(uploadFile)
                            .into(select_image_thumbnail);
                }
            }
        } else if (requestCode == RC_CAMERA) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateFolderActivity.this);
                alertDialog
                        .setTitle("Newsing Picture")
                        .setMessage("카메라로 찍은 사진을 갤러리에서 선택 후 폴더이미지에 적용하세요!!")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //yes
                                        //네트워크로 데이터를 보낸다.//
                                    }
                                });

                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        }
    }

    public void CreateFolder() {
        //우선 이동할 데이터를 뽑아온다//
        String folder_name = folder_name_edittext.getText().toString();
        boolean folder_locked = is_private;

        //Log.d("enroll data", "" + folder_name + "/" + folder_locked + "/" + uploadFile.getName());

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
        builder.addPathSegment("categories");

        /** 파일 전송이므로 MultipartBody 설정 **/
        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", folder_name);

        //비공개, 공개여부에 의해서 1/0으로 판단.//
        if (folder_locked == true) //잠금모드 활성화//
        {
            multipart_builder.addFormDataPart("locked", "1"); //true//
        } else if (folder_locked == false) //잠금모드 비활성화//
        {
            multipart_builder.addFormDataPart("locked", "0"); //false//
        }

        if (uploadFile != null) {
            //이미지 처리//
            multipart_builder.addFormDataPart("img", uploadFile.getName(),
                    RequestBody.create(mediaType, uploadFile));
        } else {
            //이미지 선택을 안할 시 아무 수행도 안함//
        }

        /** RequestBody 설정(Multipart로 설정) **/
        RequestBody body = multipart_builder.build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body) //POST방식 적용.//
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestfoldercreatecallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_folder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.folder_menu_create) {
            //폴더를 생성한다.//
            CreateFolder();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkCameraHardware(Context context) {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
