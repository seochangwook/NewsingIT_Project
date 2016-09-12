package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.SearchUserData;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.view.view_list.SearchUserViewHolder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Tacademy on 2016-08-25.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SearchUserData searchUserData;
    Context context;

    NetworkManager networkManager;
    private Callback requestSetFollowingCallback = new Callback() {
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

    public SearchUserAdapter(Context context) {
        this.context = context;
        searchUserData = new SearchUserData();
    }

    public void setSearchUserData(SearchUserData searchUserData) {
        if (this.searchUserData != searchUserData) {
            this.searchUserData = searchUserData;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    public void initSearchUserData(SearchUserData searchUserData) {
        if (this.searchUserData != searchUserData) {
            this.searchUserData = searchUserData;

            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    private void setFollowing(int userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows");

        RequestBody body = new FormBody.Builder()
                .add("ofid", "" + userId)
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .post(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);

    }

    private void deleteFollowing(int userId) {
        networkManager = NetworkManager.getInstance();

        OkHttpClient client = networkManager.getClient();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        builder.scheme("http")
                .host(context.getResources().getString(R.string.real_server_domain))
                .port(8080)
                .addPathSegment("follows")
                .addPathSegment("" + userId);

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(builder.build())
                .tag(context)
                .delete(body)
                .build();

        client.newCall(request).enqueue(requestSetFollowingCallback);
    }


    @Override
    public int getItemCount() {
        if (searchUserData == null) {
            return 0;
        }
        return searchUserData.searchUserDataArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_user, parent, false);
        SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(view);
        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (searchUserData.searchUserDataArrayList.size() > 0) {
            if (searchUserData.searchUserDataArrayList.size() > position) {
                SearchUserViewHolder searchUserViewHolder = (SearchUserViewHolder) holder;
                searchUserViewHolder.setSearchUserData(searchUserData.searchUserDataArrayList.get(position), context);

                return;
            }
            position -= searchUserData.searchUserDataArrayList.size();
        }
        throw new IllegalArgumentException("invalid position");
    }

}
