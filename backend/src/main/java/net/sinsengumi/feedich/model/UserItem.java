package net.sinsengumi.feedich.model;

import lombok.Data;

@Data
public class UserItem {

    private int userId;

    private int itemId;

    private int feedId;

    private boolean unread;

    public static UserItem build(int userId, Item item) {
        UserItem userItem = new UserItem();
        userItem.setUserId(userId);
        userItem.setItemId(item.getId());
        userItem.setFeedId(item.getFeedId());
        userItem.setUnread(true);
        return userItem;
    }
}
