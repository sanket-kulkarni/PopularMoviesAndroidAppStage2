package com.example.sanketk.popularmoives.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<ResultsItem> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setResults(List<ResultsItem> results) {
        this.results = results;
    }

    public List<ResultsItem> getResults() {
        return this.results;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return this.totalResults;
    }

    public String toString() {
        return "ApiResponse{page = '" + this.page + '\'' + ",total_pages = '" + this.totalPages + '\'' + ",results = '" + this.results + '\'' + ",total_results = '" + this.totalResults + '\'' + "}";
    }
}
