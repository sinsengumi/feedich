package net.sinsengumi.feedich.model;

import lombok.Data;

@Data
public class UnreadFeed {

    private int feedId;

    private int unreadCount;

    private Feed feed;
}
