package com.webbfontaine.testapp.service;

import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import com.webbfontaine.testapp.repository.GithubRepoAnalyticsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubRepoAnalyticsServiceImpl implements GithubRepoAnalyticsService {

    private final GithubRepoAnalyticsRepository githubRepoAnalyticsRepository;

    public GithubRepoAnalyticsServiceImpl(GithubRepoAnalyticsRepository githubRepoAnalyticsRepository) {
        this.githubRepoAnalyticsRepository = githubRepoAnalyticsRepository;
    }

    @Override
    public Flux<GithubRepoAnalytics> getAll() {
        return this.githubRepoAnalyticsRepository.findAll();
    }

    @Override
    public Mono<GithubRepoAnalytics> get(String id) {
        return this.githubRepoAnalyticsRepository.findById(id);
    }

    @Override
    public Mono<GithubRepoAnalytics> save(GithubRepoAnalytics githubRepoAnalytics) {
        return this.githubRepoAnalyticsRepository.findFirstByGithubRepoId(githubRepoAnalytics.getGithubRepo().getId())
                .flatMap(exist -> Mono.error(RuntimeException::new))
                .then(this.githubRepoAnalyticsRepository.save(githubRepoAnalytics));
    }

    @Override
    public Mono<GithubRepoAnalytics> update(String id, GithubRepoAnalytics githubRepoAnalytics) {
        return this.githubRepoAnalyticsRepository.findById(id)
                .flatMap(existingGithubRepoAnalytics -> {
                    existingGithubRepoAnalytics.setGithubRepo(githubRepoAnalytics.getGithubRepo());
                    existingGithubRepoAnalytics.setContributors(githubRepoAnalytics.getContributors());
                    existingGithubRepoAnalytics.setCommits(githubRepoAnalytics.getCommits());
                    return this.githubRepoAnalyticsRepository.save(existingGithubRepoAnalytics);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.githubRepoAnalyticsRepository.findById(id)
                .flatMap(this.githubRepoAnalyticsRepository::delete);
    }

    @Override
    public Mono<Void> deleteAll() {
        return this.githubRepoAnalyticsRepository.deleteAll();
    }
}
