package com.example.apple.newsingit_project.data.json_data.mainnewslist;

public class MainNewsListRequestResults {
    public MainNewsListRequestResultsNewscontens[] newscontens;
    private String keyword;

    public MainNewsListRequestResultsNewscontens[] getNewscontens() {
        return this.newscontens;
    }

    public void setNewscontens(MainNewsListRequestResultsNewscontens[] newscontens) {
        this.newscontens = newscontens;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
