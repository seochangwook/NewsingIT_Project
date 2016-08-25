package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.ScrapFolderListData;
import com.example.apple.newsingit_project.view.view_list.ScrapFolderListViewHolder;

/**
 * Created by apple on 2016. 8. 26..
 */
public class ScrapFolderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ScrapFolderListData scrapFolderListData;
    Context context;

    //생성자로 데이터 클래스 할당과 프래그먼트 자원 초기화.//
    public ScrapFolderListAdapter(Context context) {
        this.context = context;

        scrapFolderListData = new ScrapFolderListData();
    }

    //어댑터랑 연동되는 객체를 설정.//
    public void set_ScrapFolderList(ScrapFolderListData scrapFolderListData) {
        if (this.scrapFolderListData != scrapFolderListData) {
            this.scrapFolderListData = scrapFolderListData;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_folder_listview, parent, false);

        //뷰 홀더 객체 설정.//
        ScrapFolderListViewHolder holder = new ScrapFolderListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < scrapFolderListData.scrapfolderlist.size()) {
            final ScrapFolderListViewHolder scrapFolderListViewHolder = (ScrapFolderListViewHolder) holder;

            scrapFolderListViewHolder.set_ScrapFolderList(scrapFolderListData.scrapfolderlist.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return scrapFolderListData.scrapfolderlist.size();
    }
}
