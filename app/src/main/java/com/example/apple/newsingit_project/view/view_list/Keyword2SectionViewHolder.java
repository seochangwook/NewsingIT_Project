package com.example.apple.newsingit_project.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.Keyword2SectionData;

/**
 * Created by apple on 2016. 8. 26..
 */
public class Keyword2SectionViewHolder extends RecyclerView.ViewHolder {
    public TextView keywordsection_textview;
    Keyword2SectionData keywordSectionData;

    public Keyword2SectionViewHolder(View itemView) {
        super(itemView);

        keywordsection_textview = (TextView) itemView.findViewById(R.id.keyword_2_name_text);
    }

    public void set_KeywordSection(Keyword2SectionData keywordSectionData) {
        this.keywordSectionData = keywordSectionData;

        keywordsection_textview.setText(keywordSectionData.get_keyword_text());
    }
}
