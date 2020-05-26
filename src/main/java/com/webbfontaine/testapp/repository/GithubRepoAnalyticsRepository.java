package com.webbfontaine.testapp.repository;

import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GithubRepoAnalyticsRepository extends ReactiveMongoRepository<GithubRepoAnalytics, String> {

    Mono<GithubRepoAnalytics> findFirstByGithubRepoId(Long id);
}
