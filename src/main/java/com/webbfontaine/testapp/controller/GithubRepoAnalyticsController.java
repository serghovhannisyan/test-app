package com.webbfontaine.testapp.controller;

import com.webbfontaine.testapp.service.GithubRepoAnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analytics")
public class GithubRepoAnalyticsController {

    private final GithubRepoAnalyticsService githubRepoAnalyticsService;

    public GithubRepoAnalyticsController(GithubRepoAnalyticsService githubRepoAnalyticsService) {
        this.githubRepoAnalyticsService = githubRepoAnalyticsService;
    }

    @GetMapping
    public String getAllAnalytics(final Model model) {
        model.addAttribute("analytics", this.githubRepoAnalyticsService.getAll());

        return "analytics";
    }

    @GetMapping("/{id}")
    public String getAnalytics(@PathVariable("id") String id, Model model) {

        model.addAttribute("analytics", this.githubRepoAnalyticsService.get(id));
        return "analytics_details";
    }
}
