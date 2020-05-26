package com.webbfontaine.testapp;

import com.webbfontaine.testapp.controller.GithubRepoAnalyticsControllerApi;
import com.webbfontaine.testapp.model.GithubRepo;
import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import com.webbfontaine.testapp.service.GithubRepoAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GithubRepoAnalyticsControllerApiTest {

    @Autowired
    private GithubRepoAnalyticsService service;

    private WebTestClient client;

    @BeforeEach
    void beforeEach() {
        this.client =
                WebTestClient
                        .bindToController(new GithubRepoAnalyticsControllerApi(service))
                        .configureClient()
                        .baseUrl("/api/v1/analytics")
                        .build();
    }

    @Test
    void testGetAll() {
        List<GithubRepoAnalytics> list = new ArrayList<>(Arrays.asList(
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(1L, "Repo1")),
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(2L, "Repo2")),
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(3L, "Repo3"))));

        Flux.fromIterable(list)
                .flatMap(service::save).then().block();

        client
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(GithubRepoAnalytics.class)
                .hasSize(3);
    }

    @Test
    void testInvalidIdNotFound() {
        client
                .get()
                .uri("/wrongId")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testValidIdFound() {
        GithubRepoAnalytics repo = new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(4L, "Repo4"));
        service.save(repo).block();

        client
                .get()
                .uri("/" + repo.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }
}
