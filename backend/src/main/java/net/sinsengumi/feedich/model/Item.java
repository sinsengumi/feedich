package net.sinsengumi.feedich.model;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndEntry;

import lombok.Data;
import net.sinsengumi.feedich.model.http.ItemResponse;

@Data
public class Item {

    private int id;
    private int feedId;
    private String title;
    private String description;
    private String url;
    private String author;
    private Date publishedAt;
    private Date createdAt;
    private Date updatedAt;

    public static Item build(int feedId, Date createdAt, SyndEntry entry) {
        Item item = new Item();
        item.setFeedId(feedId);
        item.setTitle(entry.getTitle());
        if (entry.getDescription() != null) {
            item.setDescription(entry.getDescription().getValue());
        }
        item.setUrl(entry.getLink());
        item.setAuthor(entry.getAuthor());
        item.setPublishedAt(entry.getPublishedDate());
        item.setCreatedAt(createdAt);
        return item;
    }

    public Pin toPin(int userId) {
        Pin pin = new Pin();
        pin.setUserId(userId);
        pin.setTitle(title);
        pin.setUrl(url);
        return pin;
    }

    public ItemResponse toResponse() {
        ItemResponse response = new ItemResponse();
        response.setId(id);
        response.setFeedId(feedId);
        response.setTitle(title);
        response.setDescription(description);
        response.setUrl(url);
        response.setAuthor(author);
        response.setPublishedAt(publishedAt);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setUnread(true); // TODO
        return response;
    }
}