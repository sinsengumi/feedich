package net.sinsengumi.feedich.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.service.FeedCrawlService;

@Controller
@RequiredArgsConstructor
public class CrawlController extends AbstractController {

    private final FeedCrawlService feedCrawlService;

    @GetMapping("crawl")
    public String crawl() {
        feedCrawlService.crawl();
        return redirect("/");
    }
}
