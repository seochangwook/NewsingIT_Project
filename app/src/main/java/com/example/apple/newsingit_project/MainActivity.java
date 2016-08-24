package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.apple.newsingit_project.dialog.KeywordListActivity;
import com.example.apple.newsingit_project.view.view_fragment.MainNewsListFragment;
import com.example.apple.newsingit_project.view.view_fragment.MyInfo_Fragment;
import com.example.apple.newsingit_project.widget.menuwidget.BottomMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar; //툴바//

    BottomMenu bottommenu; //하단 네비게이션 메뉴 바//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        if (savedInstanceState == null) {
            //최초 초기화가 되고 생성 될 프래그먼트 정의.//
            getSupportFragmentManager().beginTransaction().add(
                    R.id.container, new MainNewsListFragment())
                    .commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottommenu = (BottomMenu) findViewById(R.id.bottom_menu_mainactivity);

        setSupportActionBar(toolbar); //툴바 생성.//
        setupBottomMenu(); //하단 메뉴 네비게이션 버튼 생성.//

        //우선적으로 fab은 보이지 않게 한다.//
    }

    @Override
    protected void onResume() {
        super.onResume();

        /** Bottom 네비게이션 메뉴의 리스너 처리는 onResume()상태에서 구현 **/
        setupBottomMenuClickListener();
    }

    /**
     * 하단 네비게이션 메뉴 이벤트 처리
     **/
    private void setupBottomMenuClickListener() {
        bottommenu.setBottomMenuClickListener(new BottomMenu.BottomMenuClickListener() {
            @Override
            public void onItemSelected(int position, int id, boolean triggeredOnRotation) {
                // Do something when item is selected
                if (position == 0) {
                    Toast.makeText(MainActivity.this, "메인 페이지 이동", Toast.LENGTH_SHORT).show();

                    //프래그먼트 변경.replace로 변경//
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainNewsListFragment())
                            .commit();

                    setTitle(getResources().getString(R.string.title_activity_main));
                }
            }

            @Override
            public void onItemReSelected(int position, int id) {
                // Do something when item is re-selected
                if (position == 1) {
                    Toast.makeText(MainActivity.this, "나의 정보 페이지 이동", Toast.LENGTH_SHORT).show();

                    //프래그먼트 변경.//
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyInfo_Fragment())
                            .commit();

                    //프래그먼트 변경 시 타이틀 변경.//
                    setTitle(getResources().getString(R.string.title_fragment_myinfo));
                }
            }
        });
    }

    /**
     * 하단 네비게이션 메뉴 구성
     **/
    public void setupBottomMenu() {
        //메뉴에 들어갈 아이템 리스트 정의.//
        ArrayList<BottomMenu.BottomMenuItem> items = new ArrayList<>();

        BottomMenu.BottomMenuItem mainnews_list_option = new BottomMenu.BottomMenuItem(R.id.bottom_menu_mainactivity, R.drawable.ic_subscriptions_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem my_info_option = new BottomMenu.BottomMenuItem(R.id.bottom_bar_my_info, R.mipmap.sample_menu_image, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);

        items.add(mainnews_list_option);
        items.add(my_info_option);

        bottommenu.addItems(items); //바에 적용.//
    }

    /**
     * Menu 구성
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); //xml로 작성된 메뉴를 팽창//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == R.id.keyword_menu_item) {
            Intent intent = new Intent(MainActivity.this, KeywordListActivity.class);

            startActivity(intent);
        } else if (item_id == R.id.search_menu_item) {
            Toast.makeText(MainActivity.this, "검색 화면으로 이동", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
