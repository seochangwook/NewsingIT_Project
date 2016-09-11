package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.view.view_fragment.SearchNewsFragment;
import com.example.apple.newsingit_project.view.view_fragment.SearchTagFragment;
import com.example.apple.newsingit_project.view.view_fragment.SearchUserFragment;

public class SearchTabActivity extends AppCompatActivity {
    private static final String SEARCH_QUERY = "SEARCH_QUERY";

    Bundle bundle;
    SearchView searchView;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView) findViewById(R.id.search_tab);

        bundle = new Bundle();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //뷰 페이저의 페이지가 전환 될 시점에 검색어를 초기화 해줌//
                searchView.setQuery("", true); //뒤에 boolean 값은 정확한 의미를 잘 모르겠음..//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //SearchVIew 검색 창//
        //돋보기 모양의 검색 아이콘 클릭 시//
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //검색 창의 배경을 빈 배경으로 바꾼다.//
                searchView.setBackgroundResource(R.mipmap.searchbar_on);
            }
        });

        //x 아이콘 클릭 시//
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() { //검색 창의 배경을 힌트가 있는 배경으로 바꾼다.//
                searchView.setBackgroundResource(R.mipmap.searchbar_off);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(SearchTabActivity.this, "" + query, Toast.LENGTH_SHORT).show();

                //서치뷰의 값을 가지고 유효검사//

                //검색어를 입력 후 검색 버튼을 눌렀을 때//
                //검색어 전달//
                bundle.putString(SEARCH_QUERY, "" + query);
                //검색 결과 리스트 갱신(갱신 시 프래그먼트들이 재적제 되니 bundle이 다시 대입된다.)//
                mSectionsPagerAdapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        setTitle(""); //타이틀 자리에 에디트 텍스트가 올 것이니 타이틀은 생략.//
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_cancel) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        //  private Bundle bundle ;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            //     this.bundle = bundle;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    SearchNewsFragment search_news_fragment = new SearchNewsFragment();
                    search_news_fragment.setArguments(bundle);

                    /*
                    /** Fragment로 값을 전달할 필요가 있을 경우 *
                    Bundle bundle = new Bundle(); //Fragment에게 값을 전달하기 위해서 Bundle사용.//
                    //Intent일 때는 액티비티간에 데이터 전달이였다. 마찬가지로 프래그먼트도 (key,value)로 구성 후 bundle을 이용한다.//
                    bundle.putString("SERVER_IP_ADDRESS_KEY", server_ip);
                    bundle.putInt("PORT_NUMBER_KEY", Integer.parseInt(server_port_number));
                    fragment_1.setArguments(bundle); //프래그먼트에게 인자들(아규먼트)을 전송할 준비를 한다.//
                    */

                    return search_news_fragment;
                }

                case 1: {
                    SearchUserFragment search_user_fragment = new SearchUserFragment();
                    search_user_fragment.setArguments(bundle);

                    return search_user_fragment;
                }

                case 2: {
                    SearchTagFragment search_tag_fragment = new SearchTagFragment();
                    search_tag_fragment.setArguments(bundle);

                    return search_tag_fragment;
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "뉴스";
                case 1:
                    return "사용자";
                case 2:
                    return "태그";
            }
            return null;
        }

        //리스트뷰 갱신을 위해 override//
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}