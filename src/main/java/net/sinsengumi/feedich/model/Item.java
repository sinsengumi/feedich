package net.sinsengumi.feedich.model;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndEntry;

import lombok.Data;

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

    public static Item build(int feedId, SyndEntry entry) {
        Item item = new Item();
        item.setFeedId(feedId);
        item.setTitle(entry.getTitle());
        item.setDescription(entry.getDescription().getValue());
        item.setUrl(entry.getLink());
        item.setAuthor(entry.getAuthor());
        item.setPublishedAt(entry.getPublishedDate());
        return item;
    }
}
