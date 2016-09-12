package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.NoticeGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2016-08-24.
 */
public class NoticeAdapter extends BaseExpandableListAdapter {

    List<NoticeGroup> items = new ArrayList<>();
    Context context;

    public NoticeAdapter(List<NoticeGroup> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setNoticeData(List<NoticeGroup> items){
        if (this.items != items) {
            this.items = items;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return items.get(i).childList.size();
    }

    @Override
    public Object getGroup(int i) {
        return items.get(i);
    }

    @Override
    public Object getChild(int i, int child) {
        return items.get(i).childList.get(child);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int childposition) {
        return childposition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean expanded, View convertView, ViewGroup parent) {
        View view;
        TextView titleView, dateView;
        final ImageView imageView;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_notice_group, parent, false);
            titleView = (TextView)view.findViewById(R.id.text_notice_title);
            dateView = (TextView)view.findViewById(R.id.text_notice_date);
            imageView= (ImageView)view.findViewById(R.id.img_notice_indicator);

        }else{
            view = convertView;
            titleView = (TextView)view.findViewById(R.id.text_notice_title);
            dateView = (TextView)view.findViewById(R.id.text_notice_date);
            imageView= (ImageView)view.findViewById(R.id.img_notice_indicator);
        }

        titleView.setText(items.get(groupPosition).title);
        dateView.setText(items.get(groupPosition).date);

        imageView.setVisibility(View.VISIBLE);

        //group indicator를 custom한다//
        if(expanded){ //group이 펼쳐졌을 때 indicator 이미지//
            imageView.setImageResource(R.mipmap.ic_arrow);
        }else {  //group이 닫혔을 때 indicator 이미지//
            imageView.setImageResource(R.mipmap.ic_arrow_dropdown);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean expanded, View convertView, ViewGroup parent) {
        View view;
        TextView childView, childTitleView;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_notice_child, parent, false);
            childView = (TextView)view.findViewById(R.id.text_notice_content);
            childTitleView = (TextView) view.findViewById(R.id.text_notice_content_title);
        }else{
            view = convertView;
            childView = (TextView)view.findViewById(R.id.text_notice_content);
            childTitleView = (TextView) view.findViewById(R.id.text_notice_content_title);
        }
        childView.setText(items.get(groupPosition).childList.get(childPosition).name);
        childTitleView.setText(items.get(groupPosition).title);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
