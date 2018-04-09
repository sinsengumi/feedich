package net.sinsengumi.feedich.controller.feeds;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.service.SubscriptionService;

@RestController
@RequestMapping("feeds")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscriptionService subscriptionService;

    @PostMapping("subscribe")
    public Subscription subscribe(@RequestParam String feedUrl) {
        return subscriptionService.subscribe(2, feedUrl);
    }

    @GetMapping("subscriptions")
    public List<Subscription> subscriptions() {
        return subscriptionService.subscriptions(1);
    }
}
