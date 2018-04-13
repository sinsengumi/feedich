package net.sinsengumi.feedich.model.http;

import java.util.Date;

import lombok.Data;

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
    private Date createdAt;
    private Date updatedAt;
}
