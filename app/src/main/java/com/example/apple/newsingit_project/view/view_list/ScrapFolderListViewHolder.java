package com.example.apple.newsingit_project.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.ScrapFolderListData;

/**
 * Created by apple on 2016. 8. 26..
 */
public class ScrapFolderListViewHolder extends RecyclerView.ViewHolder {
    public TextView scrapfolderlist_text;
    ScrapFolderListData scrapFolderListData;

    public ScrapFolderListViewHolder(View itemView) {
        super(itemView);

        scrapfolderlist_text = (TextView) itemView.findViewById(R.id.scrap_list_text);
    }

    public void set_ScrapFolderList(ScrapFolderListData scrapFolderListData) {
        this.scrapFolderListData = scrapFolderListData;

        scrapfolderlist_text.setText(scrapFolderListData.get_scrap_folder_list_data());
    }
}
