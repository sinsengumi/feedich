package net.sinsengumi.feedich.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.model.http.DiscoverResponse;
import net.sinsengumi.feedich.model.http.FeedResponse;
import net.sinsengumi.feedich.model.http.ItemResponse;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;
import net.sinsengumi.feedich.service.FeedDiscoverer;
import net.sinsengumi.feedich.service.FeedService;
import net.sinsengumi.feedich.service.ItemService;
import net.sinsengumi.feedich.service.SubscriptionService;

@Slf4j
@RestController
@RequestMapping("api/feeds")
@RequiredArgsConstructor
public class FeedController extends AbstractController {

    private final FeedDiscoverer feedDiscoverer;
    private final SubscriptionService subscriptionService;
    private final FeedService feedService;
    private final ItemService itemService;

    @GetMapping("discover")
    public List<DiscoverResponse> discover(@RequestParam String url) {
        return feedDiscoverer.discover(url).stream()
                .map(s -> DiscoverResponse.buildSyndFeed(s))
                .collect(Collectors.toList());
    }

    @PostMapping("subscribe")
    public SubscriptionResponse subscribe(@RequestParam String feedUrl) {
        log.info("feedUrl = {}", feedUrl);
        return subscriptionService.subscribe(UserController.USER_ID, feedUrl).toResponse();
    }

    @GetMapping("{feedId}")
    public FeedResponse feed(@PathVariable int feedId) {
        return feedService.findById(feedId).toResponse();
    }

    @GetMapping("{feedId}/items")
    public List<ItemResponse> items(@PathVariable int feedId) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return itemService.getUnreadItem(UserController.USER_ID, feedId).stream()
                .map(Item::toResponse)
                .collect(Collectors.toList());
    }
}
