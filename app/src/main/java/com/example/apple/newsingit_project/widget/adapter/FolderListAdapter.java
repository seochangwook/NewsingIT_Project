package com.example.apple.newsingit_project.widget.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FolderData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.view_list.FolderViewHolder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by apple on 2016. 8. 24..
 */
public class FolderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //폴더 데이터 클래스 선언.//
    FolderData folderData;
    Context context;

    /**
     * Network 관련 변수
     **/
    NetworkManager networkManager;
    private Callback requestfolderunlockedCallback = new Callback() {
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
                ((Activity) context).runOnUiThread(new Runnable() //Adapter에서 runOnUiThread작업//
                {
                    @Override
                    public void run() {
                        //응답메시지를 보내는 시기는 네트워크 작업이 모두 완료된 후이다.//
                        Toast.makeText(context, "폴더 잠금 해제를 완료하였습니다", Toast.LENGTH_SHORT).show();

                        notifyDataSetChanged(); //UI변경작업을 하는 스레드//
                    }
                });
            }
        }
    };

    //생성자로 데이터 클래스 할당과 프래그먼트 자원 초기화.//
    public FolderListAdapter(Context context) {
        this.context = context;

        folderData = new FolderData();
    }

    //어댑터랑 연동되는 객체를 설정.//
    public void set_FolderDate(FolderData folderDate) {
        if (this.folderData != folderDate) {
            this.folderData = folderDate;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    public void init_folder(FolderData folderData) {
        if (this.folderData != folderData) {
            this.folderData = folderData;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    //리사이클뷰에 들어갈 뷰 초기화.//
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_folder_list, parent, false);

        //뷰 홀더 객체 설정.//
        FolderViewHolder holder = new FolderViewHolder(view);

        return holder;
    }

    //리사이클뷰에 들어갈 뷰의 아이템값 초기화.//
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position < folderData.folder_list.size()) {
            final FolderViewHolder folderViewHolder = (FolderViewHolder) holder;

            folderViewHolder.set_Folder(folderData.folder_list.get(position), context);

            final boolean folder_private = folderData.folder_list.get(position).get_folder_private();

            if (folder_private == true) { //잠금 해제//
                folderViewHolder.folder_private_button.setVisibility(View.VISIBLE);
            } else { //잠금 설정//
                folderViewHolder.folder_private_button.setVisibility(View.GONE);
            }

            folderViewHolder.folder_private_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (folder_private == true) { //잠금 해제//
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Newsing Info")
                                .setMessage("폴더 잠금을 해제하시겠습니까?")
                                .setCancelable(false)
                                .setPositiveButton("해제",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //yes
                                                //네트워크로 데이터를 보낸다.//
                                                Log.d("position ", "" + position);

                                                folderData.folder_list.get(position).setFolder_private(false);

                                                //폴더변경에 필요한 정보(폴더id, 폴더 lock정보)를 얻는다.//
                                                int folder_id = folderData.folder_list.get(position).get_folderid();
                                                boolean folder_locked = folderData.folder_list.get(position).get_folder_private();

                                                //인증해제 작업을 해준다.//
                                                set_folder_unlocked(folder_id, folder_locked);
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
                }
            });
        }
    }

    //리사이클뷰에 나타낼 뷰의 개수를 정의.//
    @Override
    public int getItemCount() {
        return folderData.folder_list.size(); //해당 폴더 목록만큼 반환.//
    }

    public void set_folder_unlocked(int folder_id, boolean folder_locked) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("users")
                .addPathSegment("me")
                .addPathSegment("categories")
                .addPathSegment("" + folder_id);

        /** 전송해야할 body가 여러개이므로 MultipartBody 설정 **/
        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        //비공개, 공개여부에 의해서 1/0으로 판단.//
        if (folder_locked == false) //잠금모드 비활성화//
        {
            multipart_builder.addFormDataPart("locked", "0"); //false//
        }

        RequestBody body = multipart_builder.build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .put(body)
                .build();

        client.newCall(request).enqueue(requestfolderunlockedCallback);
    }
}
