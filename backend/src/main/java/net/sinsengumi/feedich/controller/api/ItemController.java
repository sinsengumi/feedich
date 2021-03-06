package net.sinsengumi.feedich.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.service.UserItemService;

@RestController
@RequestMapping("api/items")
@RequiredArgsConstructor
public class ItemController extends AbstractController {

    private final UserItemService userItemService;

    @PostMapping("read")
    public ResponseEntity<Void> read(@RequestParam int itemId, @AuthenticationPrincipal FeedichOAuth2User user) {
        int userId = user.getId();
        UserItem userItem = userItemService.findByUserIdAndItemId(userId, itemId);
        authorizeResource(userItem, userId);
        userItemService.read(userId, itemId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("unread")
    public ResponseEntity<Void> unread(@RequestParam int itemId, @AuthenticationPrincipal FeedichOAuth2User user) {
        int userId = user.getId();
        UserItem userItem = userItemService.findByUserIdAndItemId(userId, itemId);
        authorizeResource(userItem, userId);
        userItemService.unread(userId, itemId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
