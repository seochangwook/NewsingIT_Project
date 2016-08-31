package com.example.apple.newsingit_project.data.json_data.searchnewslist;

public class SearchNewsListRequest {
    private SearchNewsListRequestResults[] results;

    public SearchNewsListRequestResults[] getResults() {
        return this.results;
    }

    public void setResults(SearchNewsListRequestResults[] results) {
        this.results = results;
    }
}
