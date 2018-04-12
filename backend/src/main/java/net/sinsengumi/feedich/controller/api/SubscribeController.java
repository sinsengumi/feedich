package net.sinsengumi.feedich.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.service.SubscriptionService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscriptionService subscriptionService;

    @PostMapping("subscribe")
    public Subscription subscribe(@RequestParam String feedUrl) {
        return subscriptionService.subscribe(UserController.USER_ID, feedUrl);
    }
}
