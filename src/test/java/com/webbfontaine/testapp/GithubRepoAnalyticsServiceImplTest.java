package com.webbfontaine.testapp;

import com.webbfontaine.testapp.model.GithubRepo;
import com.webbfontaine.testapp.model.GithubRepoAnalytics;
import com.webbfontaine.testapp.repository.GithubRepoAnalyticsRepository;
import com.webbfontaine.testapp.service.GithubRepoAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GithubRepoAnalyticsServiceImplTest {

    @MockBean
    private GithubRepoAnalyticsRepository repository;

    @Autowired
    private GithubRepoAnalyticsService service;

    private List<GithubRepoAnalytics> expectedList;

    @BeforeEach
    void beforeEach() {
        this.expectedList = Arrays.asList(
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(1L, "Repo1")),
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(2L, "Repo2")),
                new GithubRepoAnalytics(new ArrayList<>(), new HashMap<>(), new GithubRepo(3L, "Repo3")));
    }

    @Test
    void testGetAll() {
        Mockito.when(repository.findAll()).thenReturn(Flux.fromIterable(this.expectedList));

        StepVerifier.create(this.service.getAll())
                .expectNext(this.expectedList.get(0))
                .expectNext(this.expectedList.get(1))
                .expectNext(this.expectedList.get(2))
                .expectComplete()
                .verify();
    }

    @Test
    void testInvalidIdNotFound() {
        String id = "aaa";
        Mockito.when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(this.service.get(id))
                .verifyComplete();
    }

    @Test
    void testValidIdFound() {
        GithubRepoAnalytics expectedGithubRepoAnalytics = this.expectedList.get(0);
        Mockito.when(repository.findById(expectedGithubRepoAnalytics.getId())).thenReturn(Mono.just(expectedGithubRepoAnalytics));

        StepVerifier.create(this.service.get(expectedGithubRepoAnalytics.getId()))
                .expectNext(expectedGithubRepoAnalytics)
                .verifyComplete();
    }

}