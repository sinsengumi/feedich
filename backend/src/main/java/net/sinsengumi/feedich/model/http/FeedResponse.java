package net.sinsengumi.feedich.model.http;

import java.util.Date;

import lombok.Data;
import net.sinsengumi.feedich.model.Feed.FeedStatus;

@Data
public class FeedResponse {
    private int id;
    private String title;
    private String description;
    private String url;
    private String feedUrl;
    private String feedType;
    private String icon;
    private String image;
    private String favicon;
    private FeedStatus status;
    private Date publishedAt;
    private Date createdAt;
    private Date updatedAt;
}
