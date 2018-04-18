package net.sinsengumi.feedich.model;

import java.util.Date;

import lombok.Data;
import net.sinsengumi.feedich.model.http.PinResponse;

@Data
public class Pin implements Authorizable {
    private int id;
    private int userId;
    private String title;
    private String url;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public int getOwner() {
        return userId;
    }

    public PinResponse toResponse() {
        PinResponse response = new PinResponse();
        response.setId(id);
        response.setTitle(title);
        response.setUrl(url);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        return response;
    }
}
