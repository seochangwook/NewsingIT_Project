package com.example.apple.newsingit_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.view.view_fragment.MainNewsListFragment;
import com.example.apple.newsingit_project.view.view_fragment.MyInfoFragment;
import com.example.apple.newsingit_project.widget.menuwidget.BottomMenu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar; //툴바//
    BottomMenu bottommenu; //하단 네비게이션 메뉴 바//
    /**
     * Drawable Menu 관련 위젯
     **/
    DrawerLayout drawerLayout; //드로워블 레이아웃.//
    ImageButton alarm_imagebutton;
    ImageView profile_imageview;
    TextView profile_name_textview;

    /**
     * Shraed 저장소 관련 변수
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//

    String profile_name;
    String profile_imgUrl;

    private BackPressCloseHandler backPressCloseHandler; //뒤로가기 처리//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);

        setTitle(getResources().getString(R.string.title_activity_main));

        if (savedInstanceState == null) {
            //최초 초기화가 되고 생성 될 프래그먼트 정의.//
            getSupportFragmentManager().beginTransaction().add(
                    R.id.container, new MainNewsListFragment())
                    .commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottommenu = (BottomMenu) findViewById(R.id.bottom_menu_mainactivity);
        alarm_imagebutton = (ImageButton) findViewById(R.id.btn_alarm);
        profile_imageview = (ImageView) findViewById(R.id.prrofile_image);
        profile_name_textview = (TextView) findViewById(R.id.textView4);

        setSupportActionBar(toolbar); //툴바 생성.//
        setupBottomMenu(); //하단 메뉴 네비게이션 버튼 생성.//

        /** DrawableLayout 설정 **/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.sample_drawable_image_2);

        profile_name = PropertyManager.getInstance().get_name();
        profile_imgUrl = PropertyManager.getInstance().get_pf_Url();

        /** 사용자 프로필 이미지 설정 **/
        //사용자 프로필 이미지 설정.(후엔 이 부분의 Url값을 전달받아 처리)//
        //파카소 라이브러리를 이용하여 이미지 로딩//
        Picasso.with(this)
                .load(profile_imgUrl)
                .transform(new CropCircleTransformation())
                .into(profile_imageview);

        profile_name_textview.setText(profile_name);

        /** Alarm화면으로 이동하는 이벤트. **/
        alarm_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "알림목록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, AlarmListActivity.class);

                startActivityForResult(intent, 100);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
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
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyInfoFragment())
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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

        BottomMenu.BottomMenuItem mainnews_list_option = new BottomMenu.BottomMenuItem(R.id.bottom_menu_mainactivity, R.mipmap.ic_top_news_on, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem my_info_option = new BottomMenu.BottomMenuItem(R.id.bottom_bar_my_info, R.mipmap.sample_menu_image, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);

        items.add(mainnews_list_option);
        items.add(my_info_option);

        bottommenu.addItems(items); //바에 적용.//
    }

    /** Drawalbe Menu 구성 **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        int item_id = item.getItemId();

        if (item_id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        drawerLayout.closeDrawer(GravityCompat.START); //우선적으로 어떠한 아이템이 클릭되었을 시 네비게이션바를 닫아준다.//

        Log.d("life cycle message", "MainActivity onPause()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) //응답코드(알람화면 중 '내 게시물 좋아요'가 전달되었을 경우)//
        {
            if (resultCode == RESULT_OK) //정상응답//
            {
                Log.d("respone message", "result");

                //프래그먼트 변경.(메인 프래그먼트로 이동)//
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainNewsListFragment())
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                //프래그먼트 변경 시 타이틀 변경.//
                setTitle(getResources().getString(R.string.title_fragment_myinfo));

                //bottommenu.
            }
        }
    }

    /**
     * 뒤로가기 처리 클래스(두번 터치 후 종료)
     **/
    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();

                return;
            }

            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        private void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);

            toast.show();
        }
    }
}
