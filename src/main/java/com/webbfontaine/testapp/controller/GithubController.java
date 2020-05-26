package com.webbfontaine.testapp.controller;

import com.webbfontaine.testapp.model.GithubRepo;
import com.webbfontaine.testapp.service.GithubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         final Model model) {
        if (page > 100) {
            model.addAttribute("errorMessage", "Only the first 1000 search results are available");
            return "index";
        }

        model.addAttribute("repos", this.githubService.searchRepo(query, page));
        model.addAttribute("query", query);
        return "index";
    }

    @PostMapping("/real-time-analytics")
    public String getAnalytics(@ModelAttribute GithubRepo githubRepo, final Model model) {
        model.addAttribute("analytics", this.githubService.getRealTimeAnalytics(githubRepo));

        return "analytics_details";
    }
}
