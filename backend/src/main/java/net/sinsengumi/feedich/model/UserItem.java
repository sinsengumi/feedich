package net.sinsengumi.feedich.model;

import lombok.Data;
import net.sinsengumi.feedich.model.http.UserItemResponse;

@Data
public class UserItem implements Authorizable {

    private int userId;
    private int itemId;
    private int feedId;
    private boolean unread;

    private Item item;
    private Pin pin;

    public static UserItem build(int userId, Item item) {
        UserItem userItem = new UserItem();
        userItem.setUserId(userId);
        userItem.setItemId(item.getId());
        userItem.setFeedId(item.getFeedId());
        userItem.setUnread(true);
        return userItem;
    }

    @Override
    public int getOwner() {
        return userId;
    }

    public UserItemResponse toResponse() {
        UserItemResponse response = new UserItemResponse();
        response.setUserId(userId);
        response.setItemId(itemId);
        response.setFeedId(feedId);
        response.setUnread(unread);
        if (item != null) {
            response.setItem(item.toResponse());
        }
        if (pin != null) {
            response.setPin(pin.toResponse());
        }
        return response;
    }
}
