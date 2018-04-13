package net.sinsengumi.feedich.model.http;

import java.util.Date;

import lombok.Data;

@Data
public class ItemResponse {
    private int id;
    private int feedId;
    private String title;
    private String description;
    private String url;
    private String author;
    private Date publishedAt;
    private Date createdAt;
    private Date updatedAt;
}
