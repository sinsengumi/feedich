package net.sinsengumi.feedich.model.http;

import java.util.Date;

import lombok.Data;

@Data
public class PinResponse {
    private int id;
    private String title;
    private String url;
    private Date createdAt;
    private Date updatedAt;
}
