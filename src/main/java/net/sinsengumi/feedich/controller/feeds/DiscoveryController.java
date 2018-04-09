package net.sinsengumi.feedich.controller.feeds;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.service.FeedDiscoverer;

@RestController
@RequestMapping("feeds")
@RequiredArgsConstructor
public class DiscoveryController {

    private final FeedDiscoverer feedDiscoverer;

    @GetMapping("discovery")
    public List<Feed> discovery(@RequestParam String url) {
        return feedDiscoverer.discover(url);
    }
}
