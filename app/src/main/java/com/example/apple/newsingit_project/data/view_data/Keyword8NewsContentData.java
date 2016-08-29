package com.example.apple.newsingit_project.data.view_data;

/**
 * Created by apple on 2016. 8. 26..
 */
public class Keyword8NewsContentData {
    public int news_content_id;
    public String news_title;
    public String news_write_date;
    public String news_content;
    public String news_author;
    public String news_thumbnail_Url;

    public int get_news_content_id() {
        return this.news_content_id;
    }

    public void set_news_content_id(int news_content_id) {
        this.news_content_id = news_content_id;
    }

    public void set_news_thumbnail_Url(String news_thumbnail_Url) {
        this.news_thumbnail_Url = news_thumbnail_Url;
    }

    public String get_news_title() {
        return this.news_title;
    }

    public void set_news_title(String news_title) {
        this.news_title = news_title;
    }

    public String get_news_write_date() {
        return this.news_write_date;
    }

    public void set_news_write_date(String news_write_date) {
        this.news_write_date = news_write_date;
    }

    public String get_news_content() {
        return this.news_content;
    }

    public void set_news_content(String news_content) {
        this.news_content = news_content;
    }

    public String get_news_author() {
        return this.news_author;
    }

    public void set_news_author(String news_author) {
        this.news_author = news_author;
    }

    public String get_news_thumbnailUrl() {
        return this.news_thumbnail_Url;
    }
}
