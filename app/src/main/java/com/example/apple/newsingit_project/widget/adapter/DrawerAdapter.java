package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.view.view_list.DrawerGroup;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class DrawerAdapter extends BaseExpandableListAdapter {
    DrawerGroup[] items;
    Switch mSwitch;
    Context context;

    public DrawerAdapter(DrawerGroup[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return items.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return items[i].childViewList.size();
    }

    @Override
    public Object getGroup(int i) {
        return items[i];
    }

    @Override
    public Object getChild(int i, int child) {
        return items[i].childViewList.get(child);
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
        TextView groupView;
        final ImageView imageView;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_group, parent, false);
            groupView = (TextView)view.findViewById(R.id.text_group);
            imageView= (ImageView)view.findViewById(R.id.img_indicator);

            if(items[groupPosition].name.equals("알림 설정"))
            {
                imageView.setImageResource(android.R.drawable.arrow_down_float);
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

        }else{
            view = convertView;
            groupView = (TextView)view.findViewById(R.id.text_group);
            imageView= (ImageView)view.findViewById(R.id.img_indicator);
            if(items[groupPosition].name.equals("알림 설정")){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

        }
        groupView.setText(items[groupPosition].name);

        /*groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                imageView.setImageResource(android.R.drawable.arrow_up_float);
            }
        });*/
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean expanded, View convertView, ViewGroup parent) {
        View view;
        TextView childView;
        String groupname = items[groupPosition].name;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_child, parent, false);
            mSwitch = (Switch)view.findViewById(R.id.switch_alarm);
            childView = (TextView)view.findViewById(R.id.text_child);

            //알림 설정 그룹일 경우//
            if(groupname.equals("알림 설정")){
                mSwitch.setVisibility(View.VISIBLE);//스위치 보이도록 설정//
                mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if(checked){
                            Toast.makeText(context , "알림 on" ,Toast.LENGTH_SHORT ).show();
                        }else{
                            Toast.makeText(context , "알림 off" ,Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }else{
                mSwitch.setVisibility(View.GONE);
            }

        }else{
            view = convertView;
            mSwitch = (Switch)view.findViewById(R.id.switch_alarm);
            childView = (TextView)view.findViewById(R.id.text_child);

            if(groupname.equals("알림 설정")) {
                mSwitch.setVisibility(View.VISIBLE);
            }

            else
            {
                mSwitch.setVisibility(View.GONE);
            }
        }


        childView.setText(items[groupPosition].childViewList.get(childPosition).name);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
