package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;

@Data
public class Subscription {

    private int id;

    private int userId;

    private int feedId;

    private Feed feed;

    private Date createdAt;

    private Date updatedAt;
}
