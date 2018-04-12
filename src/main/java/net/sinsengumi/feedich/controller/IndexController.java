package net.sinsengumi.feedich.controller;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.service.FeedService;
import net.sinsengumi.feedich.service.ItemService;
import net.sinsengumi.feedich.service.PinService;
import net.sinsengumi.feedich.service.SubscriptionService;
import net.sinsengumi.feedich.service.UserItemService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController extends AbstractController {

    private final SubscriptionService subscriptionService;
    private final FeedService feedService;
    private final ItemService itemService;
    private final UserItemService userItemService;
    private final PinService pinService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("subscriptions", subscriptionService.findByUserId(UserController.USER_ID).stream().filter(s -> s.getFeed().getFavicon() == null).collect(Collectors.toList()));
        model.addAttribute("unreadFeeds", feedService.getUnreadFeeds(UserController.USER_ID));
        model.addAttribute("userId", UserController.USER_ID);
        model.addAttribute("pins", pinService.findByUserId(UserController.USER_ID));
        return "index";
    }

    @GetMapping("feeds/{feedId}/items")
    public String items(@PathVariable int feedId, Model model) {
        model.addAttribute("unreadItems", itemService.getUnreadItem(UserController.USER_ID, feedId));
        return "item";
    }

    @GetMapping("read")
    public String read(@RequestParam int itemId) {
        userItemService.read(UserController.USER_ID, itemId);
        return redirect("/");
    }

    @GetMapping("pin")
    public String pin(@RequestParam int itemId) {
        Item item = itemService.findById(itemId);
        pinService.create(item.toPin(UserController.USER_ID));
        return redirect("/");
    }

    @GetMapping("deletePin")
    public String deletePin(@RequestParam int pinId) {
        pinService.delete(pinId);
        return redirect("/");
    }

    @GetMapping("clearPin")
    public String clearPin() {
        pinService.clear(UserController.USER_ID);
        return redirect("/");
    }
}
