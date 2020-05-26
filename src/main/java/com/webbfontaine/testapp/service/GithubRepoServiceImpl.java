package com.webbfontaine.testapp.service;

import com.webbfontaine.testapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.invoke.MethodHandles;

@Service
public class GithubRepoServiceImpl implements GithubService {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SEARCH_PAGE_SIZE = 10;
    private final WebClient webClient;

    private final String searchPath;
    private final String contributorsPath;
    private final String commitsPath;

    public GithubRepoServiceImpl(WebClient.Builder webClientBuilder,
                                 @Value("${github.baseUrl}") String baseUrl,
                                 @Value("${github.repos.path.search}") String searchPath,
                                 @Value("${github.repos.path.contributors}") String contributorsPath,
                                 @Value("${github.repos.path.commits}") String commitsPath) {
        this.searchPath = searchPath;
        this.contributorsPath = contributorsPath;
        this.commitsPath = commitsPath;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Method for searching repositories inside github
     * @param query search keyword
     * @param page page number
     * @return single response if there is or empty
     */
    @Override
    public Mono<GithubRepoResponse> searchRepo(String query, Integer page) {
        return this.webClient.get().uri(searchPath, query, page, SEARCH_PAGE_SIZE)
                .retrieve()
                .bodyToMono(GithubRepoResponse.class)
                .flatMap(response -> {
                    if (response.getTotalCount() > 1000) {
                        // Limitation form github
                        // Only the first 1000 search results are available
                        response.setTotalCount(1000);
                    }

                    int totalPages;
                    if (response.getTotalCount() <= SEARCH_PAGE_SIZE) {
                        totalPages = 1;
                    } else {
                        int totalPagesMod = response.getTotalCount() % SEARCH_PAGE_SIZE;
                        totalPages = response.getTotalCount() / SEARCH_PAGE_SIZE;
                        if (totalPagesMod != 0) {
                            totalPages = totalPages + 1;
                        }
                    }

                    response.setTotalPages(totalPages);
                    response.setCurrentPage(page);

                    return Mono.just(response);
                })
                .doOnSuccess(body -> log.info("repositories fetched successfully: {}", body))
                .doOnError(error -> log.error("unable to fetch repositories: {}", error.getMessage()));
    }

    /**
     * Retrieves top 30 contributors of the given repo
     * @param owner owner of the repo
     * @param repo repository name
     * @return list of contributors
     */
    @Override
    public Flux<GithubRepoContributor> getContributors(String owner, String repo) {
        log.info("getContributors(), owner: {}, repo: {}", owner, repo);
        return this.webClient.get().uri(contributorsPath, owner, repo)
                .retrieve()
                .bodyToFlux(GithubRepoContributor.class)
                .doOnNext(contributor -> log.warn("contributor fetched: {}", contributor))
                .doOnError(error -> log.error("unable to fetch contributors: {}", error.getMessage()));
    }

    /**
     * Retrieves latest 100 commits of the given repo
     * @param owner owner of the repo
     * @param repo repository name
     * @return list of commits
     */
    @Override
    public Flux<GithubRepoCommit> getCommits(String owner, String repo) {
        log.info("getCommits(), owner: {}, repo: {}", owner, repo);
        return this.webClient.get().uri(commitsPath, owner, repo)
                .retrieve()
                .bodyToFlux(GithubRepoCommit.class)
                .doOnNext(commit -> log.warn("commit fetched: {}", commit))
                .doOnError(error -> log.error("unable to fetch commits: {}", error.getMessage()));
    }

    /**
     * Get various analytics based commits and contributions of the given repository
     * @param githubRepo repository object
     * @return detailed analytics
     */
    @Override
    public Mono<GithubRepoAnalytics> getRealTimeAnalytics(GithubRepo githubRepo) {
        return Mono.zip(getContributors(githubRepo.getOwner().getLogin(), githubRepo.getName())
                        .subscribeOn(Schedulers.parallel())
                        .collectList(),
                getCommits(githubRepo.getOwner().getLogin(), githubRepo.getName())
                        .subscribeOn(Schedulers.parallel())
                        .filter(githubRepoCommit -> githubRepoCommit.getAuthor() != null && githubRepoCommit.getAuthor().getLogin() != null)
                        .groupBy(githubRepoCommit -> githubRepoCommit.getAuthor().getLogin())
                        .flatMap(Flux::collectList)
                        .filter(githubRepoCommits -> !githubRepoCommits.isEmpty())
                        .collectMap(githubRepoCommits -> githubRepoCommits.get(0).getAuthor().getLogin()))
                .map(objects -> new GithubRepoAnalytics(objects.getT1(), objects.getT2(), githubRepo));
    }
}
