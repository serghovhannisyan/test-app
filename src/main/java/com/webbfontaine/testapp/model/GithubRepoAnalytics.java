package com.webbfontaine.testapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document
public class GithubRepoAnalytics {

    @Id
    private String id;
    @Indexed(unique = true)
    private GithubRepo githubRepo;
    private List<GithubRepoContributor> contributors;
    private Map<String, List<GithubRepoCommit>> commits;

    public GithubRepoAnalytics() {

    }

    public GithubRepoAnalytics(List<GithubRepoContributor> contributors,
                               Map<String, List<GithubRepoCommit>> commits,
                               GithubRepo githubRepo) {
        this.contributors = contributors;
        this.commits = commits;
        this.githubRepo = githubRepo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GithubRepo getGithubRepo() {
        return githubRepo;
    }

    public void setGithubRepo(GithubRepo githubRepo) {
        this.githubRepo = githubRepo;
    }

    public List<GithubRepoContributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<GithubRepoContributor> contributors) {
        this.contributors = contributors;
    }

    public Map<String, List<GithubRepoCommit>> getCommits() {
        return commits;
    }

    public void setCommits(Map<String, List<GithubRepoCommit>> commits) {
        this.commits = commits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GithubRepoAnalytics that = (GithubRepoAnalytics) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
