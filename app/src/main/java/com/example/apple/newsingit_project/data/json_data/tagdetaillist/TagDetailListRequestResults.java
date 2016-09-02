package com.example.apple.newsingit_project.data.json_data.tagdetaillist;

public class TagDetailListRequestResults {
    private String nc_title;
    private String nc_img_url;
    private String nc_ntime;
    private int id;
    private String title;
    private String nc_author;
    private boolean favorite;
    private int favorite_cnt;

    public String getNc_title() {
        return this.nc_title;
    }

    public void setNc_title(String nc_title) {
        this.nc_title = nc_title;
    }

    public String getNc_img_url() {
        return this.nc_img_url;
    }

    public void setNc_img_url(String nc_img_url) {
        this.nc_img_url = nc_img_url;
    }

    public String getNc_ntime() {
        return this.nc_ntime;
    }

    public void setNc_ntime(String nc_ntime) {
        this.nc_ntime = nc_ntime;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNc_author() {
        return this.nc_author;
    }

    public void setNc_author(String nc_author) {
        this.nc_author = nc_author;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getFavorite_cnt() {
        return this.favorite_cnt;
    }

    public void setFavorite_cnt(int favorite_cnt) {
        this.favorite_cnt = favorite_cnt;
    }
}
