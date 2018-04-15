package net.sinsengumi.feedich.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;
import net.sinsengumi.feedich.service.SubscriptionService;

@RestController
@RequestMapping("api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionResponse> subscriptions() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return subscriptionService.findByUserId(UserController.USER_ID).stream()
                .map(Subscription::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Subscription subscribes(@RequestParam String feedUrl) {
        return subscriptionService.subscribe(UserController.USER_ID, feedUrl);
    }
}
