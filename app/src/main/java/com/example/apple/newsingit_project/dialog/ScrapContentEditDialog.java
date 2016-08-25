package com.example.apple.newsingit_project.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.widget.adapter.FolderGroupAdapter;

public class ScrapContentEditDialog extends Activity {
    ExpandableListView expandablelistview;
    FolderGroupAdapter mAdapter;

    Button scrap_delete_button;
    Switch scrap_private_switch;
    TextView scrap_info_text;
    Button scrap_fix_enroll_button;

    String group_name[];
    String child_name[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_content_edit_dialog);

        expandablelistview = (ExpandableListView) findViewById(R.id.folder_expa_list);
        scrap_delete_button = (Button) findViewById(R.id.delete_scrap_content);
        scrap_private_switch = (Switch) findViewById(R.id.private_set_switch);
        scrap_info_text = (TextView) findViewById(R.id.info_text_private);
        scrap_fix_enroll_button = (Button) findViewById(R.id.scrap_fix_enroll_button);

        mAdapter = new FolderGroupAdapter(this);
        expandablelistview.setAdapter(mAdapter);

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
                scrap_fix_enroll_button.setVisibility(View.GONE);
            }
        });

        //자식뷰가 선택되었을 경우.//
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(ScrapContentEditDialog.this, "선택된 카테고리 : " + child_name[childPosition], Toast.LENGTH_SHORT).show();

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
                scrap_fix_enroll_button.setVisibility(View.VISIBLE);
            }
        });

        scrap_fix_enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScrapContentEditDialog.this, "스크랩 설정 완료", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        scrap_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScrapContentEditDialog.this, "스크랩 삭제 완료", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        set_ExpanList_Data();
    }

    public void set_ExpanList_Data() {
        //데이터 초기화.(네트워크로 부터 데이터를 로드한다.)//
        group_name = new String[]{"Category"};
        child_name = new String[]{"사회이슈", "인물", "정치"};

        //이중for문으로 그룹 당 자식을 생성.//
        for (int group_index = 0; group_index < group_name.length; group_index++) {
            for (int child_index = 0; child_index < child_name.length; child_index++) {
                String groupname = group_name[group_index];
                String childname = child_name[child_index];

                mAdapter.set_List_Data(groupname, childname);
            }
        }
    }
}
