package net.sinsengumi.feedich.model;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndFeed;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.http.FeedResponse;
import net.sinsengumi.feedich.util.HttpUtil;

@Slf4j
@Data
public class Feed {

    private int id;
    private String title;
    private String description;
    private String url;
    private String feedUrl;
    private String feedType;
    private String icon;
    private String image;
    private String favicon;
    private Date publishedAt;
    private Date createdAt;
    private Date updatedAt;

    public static String getReadableFeedType(String feedType) {
        String[] split = feedType.split("_");
        if (split.length == 2) {
            String type = split[0];
            String version = split[1];
            if (type.equalsIgnoreCase("atom")) {
                return "Atom " + version;
            } else {
                return type.toUpperCase() + " " + version;
            }
        } else {
            return feedType;
        }
    }

    public static Feed build(String feedUrl, SyndFeed syndFeed) {
        Feed feed = new Feed();
        feed.setTitle(syndFeed.getTitle());
        feed.setDescription(syndFeed.getDescription());
        feed.setUrl(syndFeed.getLink());
        feed.setFeedUrl(feedUrl);
        feed.setFeedType(getReadableFeedType(syndFeed.getFeedType()));
        if (syndFeed.getIcon() != null) {
            feed.setIcon(syndFeed.getIcon().getUrl());
        }
        if (syndFeed.getImage() != null) {
            feed.setImage(syndFeed.getImage().getUrl());
        }
        feed.setFavicon(HttpUtil.extractFavicon(feed.getUrl()));
        feed.setPublishedAt(syndFeed.getPublishedDate());
        return feed;
    }

    public FeedResponse toResponse() {
        FeedResponse response = new FeedResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDescription(description);
        response.setUrl(url);
        response.setFeedUrl(feedUrl);
        response.setFeedType(feedType);
        response.setIcon(icon);
        response.setImage(image);
        response.setFavicon(favicon);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setPublishedAt(publishedAt);
        return response;
    }
}
