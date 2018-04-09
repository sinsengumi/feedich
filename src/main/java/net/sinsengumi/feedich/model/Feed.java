package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;

@Data
public class Feed {

    private int id;

    private String title;

    private String description;

    private String url;

    private String feedUrl;

    private String feedType;

    private String icon;

    private Date createdAt;

    private Date updatedAt;
}
