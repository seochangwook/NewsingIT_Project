package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.KeywordSection;
import com.example.apple.newsingit_project.data.view_data.NewsContent;
import com.example.apple.newsingit_project.view.view_list.Keyword10NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword10SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword1NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword1SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword2NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword2SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword3NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword3SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword4NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword4SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword5NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword5SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword6NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword6SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword7NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword7SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword8NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword8SectionViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword9NewsContentViewHolder;
import com.example.apple.newsingit_project.view.view_list.Keyword9SectionViewHolder;

/**
 * Created by apple on 2016. 8. 26..
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //뷰의 종류를 정의.//
    private static final int VIEW_TYPE_KEYWORDSECTION_1_CATEGORY = 100;
    private static final int VIEW_TYPE_KEYWORDSECTION_2_CATEGORY = 101;
    private static final int VIEW_TYPE_KEYWORDSECTION_3_CATEGORY = 102;
    private static final int VIEW_TYPE_KEYWORDSECTION_4_CATEGORY = 103;
    private static final int VIEW_TYPE_KEYWORDSECTION_5_CATEGORY = 104;
    private static final int VIEW_TYPE_KEYWORDSECTION_6_CATEGORY = 105;
    private static final int VIEW_TYPE_KEYWORDSECTION_7_CATEGORY = 106;
    private static final int VIEW_TYPE_KEYWORDSECTION_8_CATEGORY = 107;
    private static final int VIEW_TYPE_KEYWORDSECTION_9_CATEGORY = 108;
    private static final int VIEW_TYPE_KEYWORDSECTION_10_CATEGORY = 109;
    private static final int VIEW_TYPE_NEWSCONTENT_1_CATEGORY = 200;
    private static final int VIEW_TYPE_NEWSCONTENT_2_CATEGORY = 201;
    private static final int VIEW_TYPE_NEWSCONTENT_3_CATEGORY = 202;
    private static final int VIEW_TYPE_NEWSCONTENT_4_CATEGORY = 203;
    private static final int VIEW_TYPE_NEWSCONTENT_5_CATEGORY = 204;
    private static final int VIEW_TYPE_NEWSCONTENT_6_CATEGORY = 205;
    private static final int VIEW_TYPE_NEWSCONTENT_7_CATEGORY = 206;
    private static final int VIEW_TYPE_NEWSCONTENT_8_CATEGORY = 207;
    private static final int VIEW_TYPE_NEWSCONTENT_9_CATEGORY = 208;
    private static final int VIEW_TYPE_NEWSCONTENT_10_CATEGORY = 209;
    //키워드 관련 인덱스//
    private static int keyword_section_position = 0; //초기 인덱스는 0//
    //액티비티 자원//
    Context context;
    //관련 클래스 정의.//
    NewsContent newsContent;
    KeywordSection keywordSection;

    //액티비티 자원을 초기화 하기 위해서 생성자로 선언//
    public NewsAdapter(Context context) {
        this.context = context;
    }

    //데이터의 변경 유무를 확인하는 메소드. 데이터의 관리는 어댑터에서 한다.//
    public void setNewsData(KeywordSection keywordSection, NewsContent newsContent) {
        if (this.keywordSection != keywordSection && this.newsContent != newsContent) {
            //객체를 설정.//
            this.keywordSection = keywordSection;
            this.newsContent = newsContent;
        }

        notifyDataSetChanged(); //데이터 변경을 저장.//
    }

    //멀티뷰 리스트를 위한 메소드(position에 따라 어떤 뷰들이 출력되는지 설정)//
    @Override
    public int getItemViewType(int position) {
        /** 키워드 10개에 대한 뉴스데이터를 설정 **/

        //첫번째 키워드에 대한 리스트(0보다 많다는 것에 대한 비교는 키워드당 뉴스 데이터가 있는지 확인)//
        if (newsContent.keyword_1_news_content.size() > 0) {
            if (position == 0) //첫번째의 경우는 해당 키워드를 대표하는 섹션이 들어간다.//
            {
                return VIEW_TYPE_KEYWORDSECTION_1_CATEGORY;
            }

            position--;

            //섹션이 출력된 후 뉴스정보가 출력.//
            if (position < newsContent.keyword_1_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_1_CATEGORY;
            }


            //다시 position을 0으로 초기화 하기 위해서 해당 배열의 사이즈만큼 뺀다.//
            position -= newsContent.keyword_1_news_content.size();
        }

        //두번째 키워드에 대한 리스트//
        if (newsContent.keyword_2_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_2_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_2_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_2_CATEGORY;
            }

            position -= newsContent.keyword_2_news_content.size();
        }

        //세번째 키워드에 대한 리스트//
        if (newsContent.keyword_3_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_3_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_3_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_3_CATEGORY;
            }

            position -= newsContent.keyword_3_news_content.size();
        }

        //네번째 키워드에 대한 리스트//
        if (newsContent.keyword_4_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_4_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_4_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_4_CATEGORY;
            }

            position -= newsContent.keyword_4_news_content.size();
        }

        //다섯번째 키워드에 대한 리스트//
        if (newsContent.keyword_5_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_5_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_5_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_5_CATEGORY;
            }

            position -= newsContent.keyword_5_news_content.size();
        }

        //여섯번째 키워드에 대한 리스트//
        if (newsContent.keyword_6_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_6_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_6_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_6_CATEGORY;
            }

            position -= newsContent.keyword_6_news_content.size();
        }

        //일곱번쨰 키워드에 대한 리스트//
        if (newsContent.keyword_7_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_7_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_7_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_7_CATEGORY;
            }

            position -= newsContent.keyword_7_news_content.size();
        }

        //여덟번째 키워드에 대한 리스트//
        if (newsContent.keyword_8_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_8_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_8_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_8_CATEGORY;
            }

            position -= newsContent.keyword_8_news_content.size();
        }

        //아홉번째 키워드에 대한 리스트//
        if (newsContent.keyword_9_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_9_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_9_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_9_CATEGORY;
            }

            position -= newsContent.keyword_9_news_content.size();
        }

        //열번째 키워드에 대한 리스트//
        if (newsContent.keyword_10_news_content.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_KEYWORDSECTION_10_CATEGORY;
            }

            position--;

            if (position < newsContent.keyword_10_news_content.size()) {
                return VIEW_TYPE_NEWSCONTENT_10_CATEGORY;
            }

            position -= newsContent.keyword_10_news_content.size();
        }

        throw new IllegalArgumentException(("Invalid position"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewholder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_KEYWORDSECTION_1_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_1_section, parent, false);

                viewholder = new Keyword1SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_1_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_1_content, parent, false);

                viewholder = new Keyword1NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_2_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_2_section, parent, false);

                viewholder = new Keyword2SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_2_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_2_content, parent, false);

                viewholder = new Keyword2NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_3_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_3_section, parent, false);

                viewholder = new Keyword3SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_3_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_3_content, parent, false);

                viewholder = new Keyword3NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_4_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_4_section, parent, false);

                viewholder = new Keyword4SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_4_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_4_content, parent, false);

                viewholder = new Keyword4NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_5_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_5_section, parent, false);

                viewholder = new Keyword5SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_5_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_5_content, parent, false);

                viewholder = new Keyword5NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_6_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_6_section, parent, false);

                viewholder = new Keyword6SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_6_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_6_content, parent, false);

                viewholder = new Keyword6NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_7_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_7_section, parent, false);

                viewholder = new Keyword7SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_7_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_7_content, parent, false);

                viewholder = new Keyword7NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_8_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_8_section, parent, false);

                viewholder = new Keyword8SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_8_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_8_content, parent, false);

                viewholder = new Keyword8NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_9_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_9_section, parent, false);

                viewholder = new Keyword9SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_9_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_9_content, parent, false);

                viewholder = new Keyword9NewsContentViewHolder(view);

                break;
            }

            case VIEW_TYPE_KEYWORDSECTION_10_CATEGORY: {
                View view = inflater.inflate(R.layout.view_keyword_10_section, parent, false);

                viewholder = new Keyword10SectionViewHolder(view);

                break;
            }

            case VIEW_TYPE_NEWSCONTENT_10_CATEGORY: {
                View view = inflater.inflate(R.layout.view_news_10_content, parent, false);

                viewholder = new Keyword10NewsContentViewHolder(view);

                break;
            }
        }

        return viewholder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //각 리스트에 데이터를 채워준다.//

        //첫번째 키워트 경우//
        if (newsContent.keyword_1_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword1SectionViewHolder keyword1SectionViewHolder = (Keyword1SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword1SectionViewHolder.set_KeywordSection(keywordSection.keyword1SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_1_news_content.size()) {
                Keyword1NewsContentViewHolder keyword1NewsContentViewHolder = (Keyword1NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword1NewsContentViewHolder.set_NewsContent(newsContent.keyword_1_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_1_news_content.size();
        }

        //두번째 키워드 경우//
        if (newsContent.keyword_2_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Log.d("adapter position", "" + position);

                Keyword2SectionViewHolder keyword2SectionViewHolder = (Keyword2SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword2SectionViewHolder.set_KeywordSection(keywordSection.keyword2SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_2_news_content.size()) {
                Keyword2NewsContentViewHolder keyword2NewsContentViewHolder = (Keyword2NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword2NewsContentViewHolder.set_NewsContent(newsContent.keyword_2_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_2_news_content.size();
        }

        //세번째 키워드 경우//
        if (newsContent.keyword_3_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword3SectionViewHolder keyword3SectionViewHolder = (Keyword3SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword3SectionViewHolder.set_KeywordSection(keywordSection.keyword3SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_3_news_content.size()) {
                Keyword3NewsContentViewHolder keyword3NewsContentViewHolder = (Keyword3NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword3NewsContentViewHolder.set_NewsContent(newsContent.keyword_3_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_3_news_content.size();
        }

        //네번째 키워드 관련//
        if (newsContent.keyword_4_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword4SectionViewHolder keyword4SectionViewHolder = (Keyword4SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword4SectionViewHolder.set_KeywordSection(keywordSection.keyword4SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_4_news_content.size()) {
                Keyword4NewsContentViewHolder keyword4NewsContentViewHolder = (Keyword4NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword4NewsContentViewHolder.set_NewsContent(newsContent.keyword_4_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_4_news_content.size();
        }

        //다섯번째 키워드 관련//
        if (newsContent.keyword_5_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword5SectionViewHolder keyword5SectionViewHolder = (Keyword5SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword5SectionViewHolder.set_KeywordSection(keywordSection.keyword5SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_5_news_content.size()) {
                Keyword5NewsContentViewHolder keyword5NewsContentViewHolder = (Keyword5NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword5NewsContentViewHolder.set_NewsContent(newsContent.keyword_5_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_5_news_content.size();
        }

        //여섯번째 키워드 관련//
        if (newsContent.keyword_6_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword6SectionViewHolder keyword6SectionViewHolder = (Keyword6SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword6SectionViewHolder.set_KeywordSection(keywordSection.keyword6SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_6_news_content.size()) {
                Keyword6NewsContentViewHolder keyword6NewsContentViewHolder = (Keyword6NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword6NewsContentViewHolder.set_NewsContent(newsContent.keyword_6_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_6_news_content.size();
        }

        //일곱번째 키워드 관련//
        if (newsContent.keyword_7_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword7SectionViewHolder keyword7SectionViewHolder = (Keyword7SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword7SectionViewHolder.set_KeywordSection(keywordSection.keyword7SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_7_news_content.size()) {
                Keyword7NewsContentViewHolder keyword7NewsContentViewHolder = (Keyword7NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword7NewsContentViewHolder.set_NewsContent(newsContent.keyword_7_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_7_news_content.size();
        }

        //여덟번째 키워드 관련//
        if (newsContent.keyword_8_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword8SectionViewHolder keyword8SectionViewHolder = (Keyword8SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword8SectionViewHolder.set_KeywordSection(keywordSection.keyword8SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_8_news_content.size()) {
                Keyword8NewsContentViewHolder keyword8NewsContentViewHolder = (Keyword8NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword8NewsContentViewHolder.set_NewsContent(newsContent.keyword_8_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_8_news_content.size();
        }

        //아홉번째 키워드 관련//
        if (newsContent.keyword_9_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword9SectionViewHolder keyword9SectionViewHolder = (Keyword9SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword9SectionViewHolder.set_KeywordSection(keywordSection.keyword9SectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_9_news_content.size()) {
                Keyword9NewsContentViewHolder keyword9NewsContentViewHolder = (Keyword9NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword9NewsContentViewHolder.set_NewsContent(newsContent.keyword_9_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_9_news_content.size();
        }

        //열번째 키워드 관련//
        if (newsContent.keyword_10_news_content.size() > 0) {
            if (position == 0) //먼저 섹션부분의 데이터를 초기화.//
            {
                Keyword10SectionViewHolder keyword10SectionViewHolder = (Keyword10SectionViewHolder) holder;

                //키워드 배열에 있는 값을 이용.//
                keyword10SectionViewHolder.set_KeywordSection(keywordSection.keywor10dSectionDatas.get(position));

                return;
            }

            position--;

            if (position < newsContent.keyword_10_news_content.size()) {
                Keyword10NewsContentViewHolder keyword10NewsContentViewHolder = (Keyword10NewsContentViewHolder) holder;

                //뉴스데이터 배열에 있는 정보를 이용.//
                keyword10NewsContentViewHolder.set_NewsContent(newsContent.keyword_10_news_content.get(position), context);

                return;
            }

            //다시 0을 위해서 포지션값 초기화.//
            position -= newsContent.keyword_10_news_content.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (newsContent == null && keywordSection == null) {
            return 0;
        }

        int count = 0;

        if (newsContent.keyword_1_news_content.size() > 0) {
            count += (1 + newsContent.keyword_1_news_content.size());
        }

        if (newsContent.keyword_2_news_content.size() > 0) {
            count += (1 + newsContent.keyword_2_news_content.size());
        }

        if (newsContent.keyword_3_news_content.size() > 0) {
            count += (1 + newsContent.keyword_3_news_content.size());
        }

        if (newsContent.keyword_4_news_content.size() > 0) {
            count += (1 + newsContent.keyword_4_news_content.size());
        }

        if (newsContent.keyword_5_news_content.size() > 0) {
            count += (1 + newsContent.keyword_5_news_content.size());
        }

        if (newsContent.keyword_6_news_content.size() > 0) {
            count += (1 + newsContent.keyword_6_news_content.size());
        }

        if (newsContent.keyword_7_news_content.size() > 0) {
            count += (1 + newsContent.keyword_7_news_content.size());
        }

        if (newsContent.keyword_8_news_content.size() > 0) {
            count += (1 + newsContent.keyword_8_news_content.size());
        }

        if (newsContent.keyword_9_news_content.size() > 0) {
            count += (1 + newsContent.keyword_9_news_content.size());
        }

        if (newsContent.keyword_10_news_content.size() > 0) {
            count += (1 + newsContent.keyword_10_news_content.size());
        }

        return count;
    }
}
