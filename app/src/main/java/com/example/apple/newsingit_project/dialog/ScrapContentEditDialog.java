package com.example.apple.newsingit_project.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.CreateFolderActivity;
import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.json_data.scrapfolderlist.ScrapFolderListRequest;
import com.example.apple.newsingit_project.data.json_data.scrapfolderlist.ScrapFolderListRequestResults;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.widget.adapter.FolderGroupAdapter;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScrapContentEditDialog extends Activity {
    private static final String SCRAP_ID = "SCRAP_ID";
    /**
     * 응답코드
     **/
    private static final int RC_CREATEFOLDER = 100;
    ExpandableListView expandablelistview;
    FolderGroupAdapter mAdapter;
    Button create_folder_button;
    View footerview;
    Button scrap_delete_button;
    SwitchButton scrap_private_switch;
    TextView scrap_info_text;

    String group_name;
    String child_name[];
    int scrap_folder_id[];
    String scrap_id;
    String locked;
    /**
     * Network 관련 변수
     **/
    NetworkManager manager;
    private ProgressDialog pDialog;
    private Callback requestscrapcontentlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            //메인 UI에서 작업하기 위해서 runOnUiThread설정//
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            Gson gson = new Gson();

            ScrapFolderListRequest scrapFolderListRequest = gson.fromJson(response_data, ScrapFolderListRequest.class);

            set_scrap_folder_data(scrapFolderListRequest.getResults(), scrapFolderListRequest.getResults().length);
        }
    };

    private Callback requestchangelockedcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //응답메시지를 보내는 시기는 네트워크 작업이 모두 완료된 후이다.//
                        Toast.makeText(ScrapContentEditDialog.this, "스크랩 정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

    private Callback requestsmovecategorycallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //응답메시지를 보내는 시기는 네트워크 작업이 모두 완료된 후이다.//
                        Toast.makeText(ScrapContentEditDialog.this, "폴더 이동을 완료했습니다.", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        finish();
                    }
                });
            }
        }
    };

    private Callback requestDeleteScrapContentCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("json data", response_data);

            if (this != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //응답메시지를 보내는 시기는 네트워크 작업이 모두 완료된 후이다.//
                        Toast.makeText(ScrapContentEditDialog.this, "스크랩 삭제를 완료했습니다.", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_content_edit_dialog);

        expandablelistview = (ExpandableListView) findViewById(R.id.folder_expa_list);
        scrap_delete_button = (Button) findViewById(R.id.delete_scrap_content);
        scrap_private_switch = (SwitchButton) findViewById(R.id.switch_private_scrap);
        scrap_info_text = (TextView) findViewById(R.id.info_text_private);

        scrap_private_switch.setBackColorRes(R.color.switch_background_color);
        scrap_private_switch.setThumbColorRes(R.color.switch_thumb_color);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mAdapter = new FolderGroupAdapter(this);
        expandablelistview.setAdapter(mAdapter);

        /** FooterView **/
        footerview = getLayoutInflater().inflate(R.layout.expandable_footerview, null);

        create_folder_button = (Button) footerview.findViewById(R.id.create_folder_button_dialog);

        //스크랩 id를 얻어온다.//
        Intent intent = getIntent();

        scrap_id = intent.getStringExtra(SCRAP_ID);

        create_folder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrapContentEditDialog.this, CreateFolderActivity.class);

                expandablelistview.removeFooterView(footerview);

                startActivityForResult(intent, RC_CREATEFOLDER);

                expandablelistview.collapseGroup(0);
            }
        });

        //expandablelistview의 group indicator를 cusotom해주기 위해 기본 indicator를 제거한다//
        expandablelistview.setGroupIndicator(null);

        //리스트뷰 이벤트 처리.//
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                scrap_delete_button.setVisibility(View.GONE);
                scrap_private_switch.setVisibility(View.GONE);
                scrap_info_text.setVisibility(View.GONE);

                expandablelistview.addFooterView(footerview);
            }
        });

        //자식뷰가 선택되었을 경우.//
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {

                final int move_scrap_folder_id = scrap_folder_id[childPosition];

                Scrap_move(scrap_id, move_scrap_folder_id);

                expandableListView.collapseGroup(0);

                return true;
            }
        });

        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                scrap_delete_button.setVisibility(View.VISIBLE);
                scrap_private_switch.setVisibility(View.VISIBLE);
                scrap_info_text.setVisibility(View.VISIBLE);

                expandablelistview.removeFooterView(footerview);
            }
        });


        scrap_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final String scrapId = intent.getStringExtra(SCRAP_ID);

                deleteScrapData(scrapId);
            }
        });

        //스크랩 비공개/공개 설정//
        scrap_private_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean is_check) {
                //스위치가 눌리지 않았을 때 (스크랩 공개 -> 비공개로 변환)//
                if (is_check == true) {
                    //비공개("0") 전환작업//
                    locked = "0";
                    changed_scrap_locked(locked);
                } else if (is_check == false) //(비공개 -> 공개)//
                {
                    //공개("1") 전환 작업//
                    locked = "1";
                    changed_scrap_locked(locked);
                }
            }
        });

        /** 네트워크로 부터 데이터를 불러온다. **/
        get_Category_Data();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CREATEFOLDER) {
                Toast.makeText(ScrapContentEditDialog.this, "스크랩 이동을 완료하였습니다", Toast.LENGTH_SHORT).show();

                //초기화//
                init_scraplist();

                get_Category_Data(); //데이터 새롭게 셋팅//

                scrap_delete_button.setVisibility(View.VISIBLE);
                scrap_private_switch.setVisibility(View.VISIBLE);
                scrap_info_text.setVisibility(View.VISIBLE);
            }
        }
    }

    public void init_scraplist() {
        child_name = new String[0];
        scrap_folder_id = new int[0];

        group_name = null;

        mAdapter.set_List_Init(group_name, child_name);
    }

    public void changed_scrap_locked(String is_locked) {
        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의//
        builder.host(getResources().getString(R.string.real_server_domain)); //호스트를 설정.//
        builder.port(8080);
        builder.addPathSegment("scraps");
        builder.addPathSegment(scrap_id);
        builder.addQueryParameter("action", "udscrap");

        RequestBody body = new FormBody.Builder()
                .add("locked", is_locked)
                .build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(ScrapContentEditDialog.this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestchangelockedcallback);
    }

    public void Scrap_move(String scrap_id, int move_scrap_folder_id) {
        Log.d("data", scrap_id + "/" + move_scrap_folder_id);

        //폴더 이동작업//
        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의//
        builder.host(getResources().getString(R.string.real_server_domain)); //호스트를 설정.//
        builder.port(8080);
        builder.addPathSegment("scraps");
        builder.addPathSegment(scrap_id);
        builder.addQueryParameter("action", "mvcategory");

        RequestBody body = new FormBody.Builder()
                .add("cid", "" + move_scrap_folder_id)
                .build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(ScrapContentEditDialog.this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestsmovecategorycallback);
    }

    public void get_Category_Data() {

        //progress bar
        showpDialog();

        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http"); //스킴정의//
        builder.host(getResources().getString(R.string.real_server_domain)); //호스트를 설정.//
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //나의 정보이기에 "me"로 설정//
        builder.addPathSegment("categories");

        //값을 설정.//
        builder.addQueryParameter("usage", "scrap");
        builder.addQueryParameter("page", "1");
        builder.addQueryParameter("count", "20");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(ScrapContentEditDialog.this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestscrapcontentlistcallback);

        hidepDialog();
    }

    public void set_scrap_folder_data(final ScrapFolderListRequestResults scrapFolderListRequestResults[], final int scrap_folder_list_size) {
        if (this != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<ScrapFolderListRequestResults> scrapFolderListRequestResultsList = new ArrayList<>();

                    scrapFolderListRequestResultsList.addAll(Arrays.asList(scrapFolderListRequestResults));

                    group_name = "스크랩 폴더 이동";
                    //  group_name = new String[]{"스크랩 폴더 이동"};
                    child_name = new String[scrap_folder_list_size]; //스크랩 사이즈의 개수로 배열을 할당.//
                    scrap_folder_id = new int[scrap_folder_list_size]; //스크랩 사이즈의 개수로 정수배열을 할당.//

                    for (int i = 0; i < scrap_folder_list_size; i++) {
                        //id, name을 각각 배열에 저장//
                        child_name[i] = scrapFolderListRequestResultsList.get(i).getName();
                        scrap_folder_id[i] = scrapFolderListRequestResultsList.get(i).getId();

                        Log.d("data", child_name[i]);
                    }

                    mAdapter.set_List_Data(group_name, child_name);
                }
            });
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void deleteScrapData(String scrapId) {
        manager = NetworkManager.getInstance();

        OkHttpClient client = manager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("scraps")
                .addPathSegment(scrapId);

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(ScrapContentEditDialog.this)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestDeleteScrapContentCallback);

    }

}
