package net.sinsengumi.feedich.controller.api;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.http.DiscoverResponse;
import net.sinsengumi.feedich.service.FeedDiscoverer;
import net.sinsengumi.feedich.service.SubscriptionService;

@Slf4j
@RestController
@RequestMapping("api/feeds")
@RequiredArgsConstructor
public class FeedController extends AbstractController {

    private final FeedDiscoverer feedDiscoverer;
    private final SubscriptionService subscriptionService;

    @GetMapping("discover")
    public ResponseEntity<List<DiscoverResponse>> discover(@RequestParam String url,
            @AuthenticationPrincipal FeedichOAuth2User user) {
        try {
            List<DiscoverResponse> result = feedDiscoverer.discover(url).stream()
                    .map(s -> {
                        boolean subscribed = subscriptionService.subscribed(user.getId(), s.getUri());
                        return DiscoverResponse.buildSyndFeed(s, subscribed);
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("{}", e.getMessage(), e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}
