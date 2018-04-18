package net.sinsengumi.feedich.model.http;

import lombok.Data;

@Data
public class UserItemResponse {
    private int userId;
    private int itemId;
    private int feedId;
    private boolean unread;
    private ItemResponse item;
    private PinResponse pin;
}
