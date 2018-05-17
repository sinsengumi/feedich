package net.sinsengumi.feedich.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.rometools.rome.feed.synd.SyndContent;
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
        item.setTitle(StringUtils.trim(entry.getTitle()));

        // description
        if (entry.getDescription() != null) {
            item.setDescription(StringUtils.trim(entry.getDescription().getValue()));
        }
        if (!entry.getContents().isEmpty()) {
            SyndContent syndContent = entry.getContents().get(0);
            item.setDescription(StringUtils.trim(syndContent.getValue()));
        }

        item.setUrl(StringUtils.trim(entry.getLink()));
        item.setAuthor(StringUtils.trim(entry.getAuthor()));
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
        return response;
    }
}
