package net.sinsengumi.feedich.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.controller.AbstractController;
import net.sinsengumi.feedich.controller.UserController;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.model.http.ItemResponse;
import net.sinsengumi.feedich.service.ItemService;
import net.sinsengumi.feedich.service.UserItemService;

@Slf4j
@RestController
@RequestMapping("api/items")
@RequiredArgsConstructor
public class ItemController extends AbstractController {

    private final ItemService itemService;
    private final UserItemService userItemService;

    @PostMapping("read")
    public ItemResponse read(@RequestParam int itemId) {
        Item item = itemService.findById(itemId);
        userItemService.read(UserController.USER_ID, itemId);
        return item.toResponse();
    }

    @PostMapping("unread")
    public ItemResponse unread(@RequestParam int itemId) {
        Item item = itemService.findById(itemId);
        userItemService.unread(UserController.USER_ID, itemId);
        return item.toResponse();
    }
}
