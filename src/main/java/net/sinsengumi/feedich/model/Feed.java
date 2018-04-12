package net.sinsengumi.feedich.model;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndFeed;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
    private Date createdAt;
    private Date updatedAt;

    public static Feed build(String feedUrl, SyndFeed syndFeed) {
        Feed feed = new Feed();
        feed.setTitle(syndFeed.getTitle());
        feed.setDescription(syndFeed.getDescription());
        feed.setUrl(syndFeed.getLink());
        feed.setFeedUrl(feedUrl);
        feed.setFeedType(syndFeed.getFeedType());
        if (syndFeed.getIcon() != null) {
            feed.setIcon(syndFeed.getIcon().getUrl());
        }
        if (syndFeed.getImage() != null) {
            feed.setImage(syndFeed.getImage().getUrl());
        }
        feed.setFavicon(HttpUtil.extractFavicon(feed.getUrl()));
        return feed;
    }
}
