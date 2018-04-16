package net.sinsengumi.feedich.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.service.UserItemService;

@RestController
@RequestMapping("api/items")
@RequiredArgsConstructor
public class ItemController extends AbstractController {

    private final UserItemService userItemService;

    @PostMapping("read")
    public String read(@RequestParam int itemId) {
        UserItem userItem = userItemService.findByUserIdAndItemId(UserController.USER_ID, itemId);
        authorizeResource(userItem, UserController.USER_ID);
        userItemService.read(UserController.USER_ID, itemId);
        return "OK";
    }

    @PostMapping("unread")
    public String unread(@RequestParam int itemId) {
        UserItem userItem = userItemService.findByUserIdAndItemId(UserController.USER_ID, itemId);
        authorizeResource(userItem, UserController.USER_ID);
        userItemService.unread(UserController.USER_ID, itemId);
        return "OK";
    }
}
