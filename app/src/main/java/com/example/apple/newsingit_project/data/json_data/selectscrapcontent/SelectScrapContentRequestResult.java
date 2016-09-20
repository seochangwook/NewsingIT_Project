package com.example.apple.newsingit_project.data.json_data.selectscrapcontent;

public class SelectScrapContentRequestResult {
    private String nc_title;
    private String nc_content;
    private String nc_img_url;
    private String img_url;
    private String title;
    private boolean favorite;
    private String nc_link;
    private String content;
    private int favorite_cnt;
    //private boolean favorite;
    private String[] tags;
    private String nc_ntime;
    private String nc_author;
    private String dtime;

    public String getNc_title() {
        return this.nc_title;
    }

    public void setNc_title(String nc_title) {
        this.nc_title = nc_title;
    }

    public String getNc_contents() {
        return this.nc_content;
    }

    public void setNc_contents(String nc_contents) {
        this.nc_content = nc_contents;
    }

    public String getNc_img_url() {
        return this.nc_img_url;
    }

    public void setNc_img_url(String nc_img_url) {
        this.nc_img_url = nc_img_url;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getNc_link() {
        return this.nc_link;
    }

    public void setNc_link(String nc_link) {
        this.nc_link = nc_link;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavorite_cnt() {
        return this.favorite_cnt;
    }

    public void setFavorite_cnt(int favorite_cnt) {
        this.favorite_cnt = favorite_cnt;
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getNc_ntime() {
        return this.nc_ntime;
    }

    public void setNc_ntime(String nc_time) {
        this.nc_ntime = nc_time;
    }

    public String getNc_author() {
        return this.nc_author;
    }

    public void setNc_author(String nc_author) {
        this.nc_author = nc_author;
    }

    public String getDtime() {
        return this.dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    /*public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }*/
}
