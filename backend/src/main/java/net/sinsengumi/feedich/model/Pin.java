package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;

@Data
public class Pin {
    private int id;
    private int userId;
    private String title;
    private String url;
    private Date createdAt;
    private Date updatedAt;
}
