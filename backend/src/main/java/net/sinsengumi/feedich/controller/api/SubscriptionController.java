package net.sinsengumi.feedich.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;
import net.sinsengumi.feedich.model.http.UserItemResponse;
import net.sinsengumi.feedich.service.SubscriptionService;
import net.sinsengumi.feedich.service.UserItemService;

@RestController
@RequestMapping("api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController extends AbstractController {

    private final SubscriptionService subscriptionService;
    private final UserItemService userItemService;

    @GetMapping
    public List<SubscriptionResponse> subscriptions(@AuthenticationPrincipal FeedichOAuth2User user) {
        return subscriptionService.findByUserId(user.getId()).stream()
                .map(Subscription::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("{subscriptionId}")
    public SubscriptionResponse subscription(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());
        return subscription.toResponse();
    }

    @GetMapping("{subscriptionId}/items")
    public List<UserItemResponse> items(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        return userItemService.findUnreadItems(user.getId(), subscription.getFeedId()).stream()
                .map(UserItem::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping
    public SubscriptionResponse subscribe(@RequestParam String feedUrl, @AuthenticationPrincipal FeedichOAuth2User user) {
        return subscriptionService.subscribe(user.getId(), feedUrl).toResponse();
    }

    @DeleteMapping("{subscriptionId}")
    public int unsubscribe(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        return subscriptionService.unsubscribe(subscriptionId, user.getId(), subscription.getFeedId());
    }
}
