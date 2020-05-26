package com.webbfontaine.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GithubRepoResponse {

    private Integer totalCount;
    private List<GithubRepo> items;
    private int currentPage;
    private int totalPages;

    public Integer getTotalCount() {
        return totalCount;
    }

    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<GithubRepo> getItems() {
        return items;
    }

    public void setItems(List<GithubRepo> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "GithubRepoResponse{" +
                "totalCount=" + totalCount +
                ", items=" + items +
                '}';
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
