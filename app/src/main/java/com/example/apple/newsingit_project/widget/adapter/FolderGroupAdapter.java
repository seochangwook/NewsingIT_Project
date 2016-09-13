package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FolderItem;
import com.example.apple.newsingit_project.view.view_list.FolderGroupItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 25..
 */
public class FolderGroupAdapter extends BaseExpandableListAdapter {
    Context context;
    //그룹에 대한 리스트 정의.//
    List<FolderGroupItem> items = new ArrayList<>(); //ExpandableListView는 ListView를 가지고 데이터를 셋팅//

    int last_childposition;

    public FolderGroupAdapter(Context context) {
        this.context = context;
    }

    public void set_List_Data(String groupName, String[] childName) {
        FolderGroupItem group = null;

        for (FolderGroupItem g : items) {
            if (g.foldergroup_name.equals(groupName)) {
                group = g;

                break;
            }
        }

        if (group == null) //만약 그룹이 할당이 안되었으면.//
        {
            group = new FolderGroupItem(); //그룹을 만들어준다.//
            group.foldergroup_name = groupName;

            items.add(group); //그룹을 추가.//
        }

        //그룹 당 자식의 객체를 생성.//
        for (int i = 0; i < childName.length; i++) {
            if (!TextUtils.isEmpty(childName.clone().toString())) {
                FolderItem child = new FolderItem(); //자식을 생성.//

                child.folder_name = childName[i];

                group.folder.add(child); //해당 그룹에 자식을 등록.//
            }
        }

        notifyDataSetChanged();
    }

    public void set_List_Init(String groupName, String[] childName) {
        items.clear(); //ExpandableList를 초기화//

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount()  //그룹의 개수를 반환.//
    {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) //그룹 당 자식의 개수를 반환.//
    {
        return items.get(groupPosition).folder.size();
    }

    @Override
    public Object getGroup(int groupPosition) //그룹 하나를 얻어온다.//
    {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) //그룹 당 자식을 얻어온다.//
    {
        return items.get(groupPosition).folder.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //그룹뷰에 대한 설정//
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupItem_view;

        TextView group_name_text;
        final ImageView indicator_image;

        if (convertView == null) {
            groupItem_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foldergroup_item, parent, false);

            indicator_image = (ImageView) groupItem_view.findViewById(R.id.image_indicate_arrow);
            group_name_text = (TextView) groupItem_view.findViewById(R.id.group_name_text);

        } else {
            groupItem_view = convertView;

            indicator_image = (ImageView) groupItem_view.findViewById(R.id.image_indicate_arrow);
            group_name_text = (TextView) groupItem_view.findViewById(R.id.group_name_text);
        }

        group_name_text.setText(items.get(groupPosition).foldergroup_name);

        //group indicator를 custom한다//
        if (isExpanded) { //group이 펼쳐졌을 때 indicator 이미지//
            indicator_image.setImageResource(R.mipmap.ic_arrow);
        } else {  //group이 닫혔을 때 indicator 이미지//
            indicator_image.setImageResource(R.mipmap.ic_arrow_dropdown);
        }

        return groupItem_view;
    }

    //자식뷰에 대한 설정.//
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View child_item_view;

        TextView child_item_name;

        if (convertView == null) {
            child_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);

            child_item_name = (TextView) child_item_view.findViewById(R.id.child_name_text);

        } else {
            child_item_view = convertView;

            child_item_name = (TextView) child_item_view.findViewById(R.id.child_name_text);
        }

        child_item_name.setText(items.get(groupPosition).folder.get(childPosition).folder_name);

        return child_item_view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
