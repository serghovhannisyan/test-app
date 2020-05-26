package com.webbfontaine.testapp.controller;

import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import com.webbfontaine.testapp.service.GithubRepoAnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/analytics")
public class GithubRepoAnalyticsControllerApi {

    private final GithubRepoAnalyticsService githubRepoAnalyticsService;

    public GithubRepoAnalyticsControllerApi(GithubRepoAnalyticsService githubRepoAnalyticsService) {
        this.githubRepoAnalyticsService = githubRepoAnalyticsService;
    }

    @GetMapping
    public Flux<GithubRepoAnalytics> getAll() {
        return this.githubRepoAnalyticsService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<GithubRepoAnalytics>> get(@PathVariable("id") String id) {
        return this.githubRepoAnalyticsService.get(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GithubRepoAnalytics> save(@RequestBody GithubRepoAnalytics githubRepoAnalytics) {
        return this.githubRepoAnalyticsService.save(githubRepoAnalytics);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<GithubRepoAnalytics>> update(@PathVariable("id") String id,
                                                            @RequestBody GithubRepoAnalytics githubRepoAnalytics) {
        return this.githubRepoAnalyticsService.update(id, githubRepoAnalytics)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return this.githubRepoAnalyticsService.delete(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAll() {
        return this.githubRepoAnalyticsService.deleteAll();
    }
}
