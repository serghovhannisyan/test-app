package com.webbfontaine.testapp.service;

import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GithubRepoAnalyticsService {

    Flux<GithubRepoAnalytics> getAll();

    Mono<GithubRepoAnalytics> get(String id);

    Mono<GithubRepoAnalytics> save(GithubRepoAnalytics githubRepoAnalytics);

    Mono<GithubRepoAnalytics> update(String id, GithubRepoAnalytics githubRepoAnalytics);

    Mono<Void> delete(String id);

    Mono<Void> deleteAll();

}
