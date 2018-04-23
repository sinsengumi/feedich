package net.sinsengumi.feedich.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.controller.UserController;
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
    public List<SubscriptionResponse> subscriptions() {
        return subscriptionService.findByUserId(UserController.USER_ID).stream()
                .map(Subscription::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("{subscriptionId}")
    public SubscriptionResponse subscription(@PathVariable int subscriptionId) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, UserController.USER_ID);
        return subscription.toResponse();
    }

    @GetMapping("{subscriptionId}/items")
    public List<UserItemResponse> items(@PathVariable int subscriptionId) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, UserController.USER_ID);

        return userItemService.findUnreadItems(UserController.USER_ID, subscription.getFeedId()).stream()
                .map(UserItem::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping
    public SubscriptionResponse subscribe(@RequestParam String feedUrl) {
        return subscriptionService.subscribe(UserController.USER_ID, feedUrl).toResponse();
    }

    @DeleteMapping("{subscriptionId}")
    public int unsubscribe(@PathVariable int subscriptionId) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, UserController.USER_ID);

        return subscriptionService.unsubscribe(subscriptionId, UserController.USER_ID, subscription.getFeedId());
    }
}
