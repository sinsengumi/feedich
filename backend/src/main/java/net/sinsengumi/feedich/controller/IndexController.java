package net.sinsengumi.feedich.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.service.FeedCrawlService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController extends AbstractController {

    private final FeedCrawlService feedCrawlService;

    @GetMapping("")
    public String index(@AuthenticationPrincipal FeedichOAuth2User oauth2User, Model model) {
        model.addAttribute("attributes", oauth2User.getAttributes());
        model.addAttribute("userId", oauth2User.getId());
        model.addAttribute("userName", oauth2User.getName());
        log.info("oauth2User = {}", oauth2User);
        return "index";
    }

    @GetMapping("crawl")
    public String crawl() {
        feedCrawlService.crawl();
        return redirect("/");
    }
}
