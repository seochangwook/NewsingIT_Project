package com.example.apple.newsingit_project.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    ExpandableListView expandablelistview;
    FolderGroupAdapter mAdapter;

    Button scrap_delete_button;
    SwitchButton scrap_private_switch;
    TextView scrap_info_text;
    String group_name;
    //String group_name[];
    String child_name[];
    int scrap_folder_id[];
    String scrap_id;
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

        //스크랩 id를 얻어온다.//
        Intent intent = getIntent();

        scrap_id = intent.getStringExtra(SCRAP_ID);

        //expandablelistview의 group indicator를 cusotom해주기 위해 기본 indicator를 제거한다//
        expandablelistview.setGroupIndicator(null);

        //리스트뷰 이벤트 처리.//
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(ScrapContentEditDialog.this, "원하는 카테고리를 선택하세요", Toast.LENGTH_SHORT).show();

                scrap_delete_button.setVisibility(View.GONE);
                scrap_private_switch.setVisibility(View.GONE);
                scrap_info_text.setVisibility(View.GONE);
            }
        });

        //자식뷰가 선택되었을 경우.//
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(ScrapContentEditDialog.this, "선택된 카테고리 : " + child_name[childPosition], Toast.LENGTH_SHORT).show();

                int move_scrap_folder_id = scrap_folder_id[childPosition];

                if (childPosition == child_name.length - 1) //마지막 자식
                {
                    Intent intent = new Intent(ScrapContentEditDialog.this, CreateFolderActivity.class);

                    startActivity(intent);
                } else {
                    //카테고리 이동관련. 이동을 할려면 스크랩 id와 이동할 폴더의 id가 필요.//
                    Scrap_move(scrap_id, move_scrap_folder_id);
                }

                expandableListView.collapseGroup(0);

                //카테고리 선택.//
                return true;
            }
        });

        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(ScrapContentEditDialog.this, "카테고리 목록 닫기", Toast.LENGTH_SHORT).show();

                scrap_delete_button.setVisibility(View.VISIBLE);
                scrap_private_switch.setVisibility(View.VISIBLE);
                scrap_info_text.setVisibility(View.VISIBLE);
            }
        });


        scrap_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                String scrapId = intent.getStringExtra(SCRAP_ID);
                //  Log.d("scrap_id", scrapId);
                deleteScrapData(scrapId);

                Toast.makeText(ScrapContentEditDialog.this, "스크랩 삭제 완료", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        //set_ExpanList_Data();

        /** 네트워크로 부터 데이터를 불러온다. **/
        get_Category_Data();
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
        builder.addQueryParameter("category", "" + move_scrap_folder_id);

        RequestBody body = new FormBody.Builder()
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

                    //마지막 부분에 추가.//
                    int array_last_size = scrap_folder_list_size + 1;

                    group_name = "스크랩 폴더 이동";
                    //  group_name = new String[]{"스크랩 폴더 이동"};
                    child_name = new String[array_last_size]; //스크랩 사이즈의 개수로 배열을 할당.//
                    scrap_folder_id = new int[array_last_size]; //스크랩 사이즈의 개수로 정수배열을 할당.//

                    for (int i = 0; i < array_last_size; i++) {
                        if (i == array_last_size - 1) {
                            child_name[i] = "+ 폴더만들기";
                        } else {
                            //id, name을 각각 배열에 저장//
                            child_name[i] = scrapFolderListRequestResultsList.get(i).getName();
                            scrap_folder_id[i] = scrapFolderListRequestResultsList.get(i).getId();
                        }

                        Log.d("data", child_name[i]);
                    }

                    mAdapter.set_List_Data(group_name, child_name);

//                     //Child를 설정//
//                    for (int group_index = 0; group_index < group_name.length; group_index++) {
//                        for (int child_index = 0; child_index < child_name.length; child_index++) {
//                            String groupname = group_name[group_index];
//                            String childname = child_name[child_index];
//                            mAdapter.set_List_Data(groupname, childname);
//                        }
//                    }
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


//
//    public void set_ExpanList_Data() {
//        //데이터 초기화.(네트워크로 부터 데이터를 로드한다.)//
//        group_name = new String[]{"스크랩 폴더 이동"};
//        child_name = new String[]{"사회이슈", "인물", "정치", "+ 폴더만들기"};
//
//        //이중for문으로 그룹 당 자식을 생성.//
//        for (int group_index = 0; group_index < group_name.length; group_index++) {
//            for (int child_index = 0; child_index < child_name.length; child_index++) {
//                String groupname = group_name[group_index];
//                String childname = child_name[child_index];
//
//                mAdapter.set_List_Data(groupname, childname);
//            }
//        }
//    }

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
