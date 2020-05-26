package com.webbfontaine.testapp.controller;

import com.webbfontaine.testapp.model.GithubRepo;
import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import com.webbfontaine.testapp.model.GithubRepoResponse;
import com.webbfontaine.testapp.service.GithubService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class GithubControllerApi {

    private final GithubService githubService;

    public GithubControllerApi(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/search")
    public Mono<GithubRepoResponse> search(@RequestParam("query") String query,
                                           @RequestParam(name = "page", defaultValue = "1") Integer page) {
        return githubService.searchRepo(query, page);
    }

    @PostMapping("/real-time-analytics")
    public Mono<GithubRepoAnalytics> getAnalytics(@RequestBody GithubRepo githubRepo) {
        return this.githubService.getRealTimeAnalytics(githubRepo);
    }
}
