package com.webbfontaine.testapp.service;

import com.webbfontaine.testapp.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GithubService {

    Mono<GithubRepoResponse> searchRepo(String query, Integer page);

    Flux<GithubRepoContributor> getContributors(String owner, String repo);

    Flux<GithubRepoCommit> getCommits(String owner, String repo);

    Mono<GithubRepoAnalytics> getRealTimeAnalytics(GithubRepo githubRepo);
}
