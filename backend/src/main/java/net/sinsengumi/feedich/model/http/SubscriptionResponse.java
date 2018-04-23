package net.sinsengumi.feedich.model.http;

import java.util.Date;

import lombok.Data;

@Data
public class SubscriptionResponse {
    private int id;
    private FeedResponse feed;
    private Date createdAt;
    private Date updatedAt;
    private int unreadCount;
    private int unreadCountOriginal;
}
