package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;
import net.sinsengumi.feedich.model.http.SubscriptionResponse;

@Data
public class Subscription implements Authorizable {

    private int id;
    private int userId;
    private int feedId;
    private Date createdAt;
    private Date updatedAt;
    private int unreadCount;

    private Feed feed;

    public SubscriptionResponse toResponse() {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(id);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setUnreadCount(unreadCount);
        response.setFeed(feed.toResponse());
        response.setUnreadCount(unreadCount);
        return response;
    }

    @Override
    public int getOwner() {
        return userId;
    }
}
