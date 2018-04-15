package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;

@Data
public class Subscription {

    private int id;
    private int userId;
    private int feedId;
    private Feed feed;
    private Date createdAt;
    private Date updatedAt;
    private int unreadCount;

    public SubscriptionResponse toResponse() {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(id);
        response.setFeed(feed.toResponse());
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setUnreadCount(unreadCount);
        return response;
    }
}
