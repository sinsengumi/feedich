package net.sinsengumi.feedich.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.service.FeedDiscoverer;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class DiscoverController {

    private final FeedDiscoverer feedDiscoverer;

    @GetMapping("discover")
    public List<Feed> discovery(@RequestParam String url) {
        return feedDiscoverer.discover(url).stream().map(s -> Feed.build("dummy", s)).collect(Collectors.toList());
    }
}
