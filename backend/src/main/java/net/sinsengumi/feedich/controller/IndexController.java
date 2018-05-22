package net.sinsengumi.feedich.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.service.FeedCrawlService;
import net.sinsengumi.feedich.service.FeedService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController extends AbstractController {

    private final FeedService feedService;
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
        List<Feed> feeds = feedService.findByAll();
        for (Feed feed : feeds) {
            try {
                feedCrawlService.crawl(feed);
            } catch (Exception e) {
                log.error("Failed crawl feed. id = {}, url = {}", feed.getId(), feed.getFeedUrl(), e);
            }
        }
        
        return redirect("/");
    }
}
