package net.sinsengumi.feedich.controller.api;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;
import net.sinsengumi.feedich.model.http.UserItemResponse;
import net.sinsengumi.feedich.service.FeedOpmlService;
import net.sinsengumi.feedich.service.SubscriptionService;
import net.sinsengumi.feedich.service.UserItemService;

@RestController
@RequestMapping("api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController extends AbstractController {

    private final SubscriptionService subscriptionService;
    private final UserItemService userItemService;
    private final FeedOpmlService feedOpmlService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> subscriptions(@AuthenticationPrincipal FeedichOAuth2User user) {
        List<SubscriptionResponse> result = subscriptionService.findByUserId(user.getId()).stream()
                .map(Subscription::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("{subscriptionId}/items")
    public ResponseEntity<List<UserItemResponse>> items(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        List<UserItemResponse> result = userItemService.findUnreadItems(user.getId(), subscription.getFeedId()).stream()
                .map(UserItem::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestParam String feedUrl, @AuthenticationPrincipal FeedichOAuth2User user) {
        SubscriptionResponse result = subscriptionService.subscribe(user.getId(), feedUrl).toResponse();
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("{subscriptionId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable int subscriptionId, @AuthenticationPrincipal FeedichOAuth2User user) {
        Subscription subscription = subscriptionService.findById(subscriptionId);
        authorizeResource(subscription, user.getId());

        subscriptionService.unsubscribe(subscriptionId, user.getId(), subscription.getFeedId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("export")
    public ResponseEntity<byte[]> export(@AuthenticationPrincipal FeedichOAuth2User user) {
        List<Feed> feeds = subscriptionService.findByUserId(user.getId()).stream()
                .map(s -> s.getFeed())
                .collect(Collectors.toList());
        String opml = feedOpmlService.exportOpml(feeds);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml; charset=UTF-8");
        headers.setContentDispositionFormData("filename", "feedich_subscriptions.xml");
        return new ResponseEntity<>(opml.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }

}
