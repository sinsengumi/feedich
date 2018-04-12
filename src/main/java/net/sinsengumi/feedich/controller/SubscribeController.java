package net.sinsengumi.feedich.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.service.SubscriptionService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SubscribeController extends AbstractController {

    private final SubscriptionService subscriptionService;

    @PostMapping("subscribe")
    public String subscribe(@RequestParam String feedUrl) {
        log.info("feedUrl = {}", feedUrl);
        subscriptionService.subscribe(UserController.USER_ID, feedUrl);
        return redirect("/");
    }
}
