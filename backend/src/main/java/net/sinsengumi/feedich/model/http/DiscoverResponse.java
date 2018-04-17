package net.sinsengumi.feedich.model.http;

import com.rometools.rome.feed.synd.SyndFeed;

import lombok.Data;
import net.sinsengumi.feedich.util.HttpUtil;

@Data
public class DiscoverResponse {
    private String title;
    private String description;
    private String url;
    private String feedUrl;
    private String feedType;
    private String icon;
    private String image;
    private String favicon;
    private boolean subscribed;

    public static DiscoverResponse buildSyndFeed(SyndFeed syndFeed, boolean subscribed) {
        DiscoverResponse response = new DiscoverResponse();
        response.setTitle(syndFeed.getTitle());
        response.setDescription(syndFeed.getDescription());
        response.setUrl(syndFeed.getLink());
        response.setFeedUrl(syndFeed.getUri());
        response.setFeedType(syndFeed.getFeedType());
        if (syndFeed.getIcon() != null) {
            response.setIcon(syndFeed.getIcon().getUrl());
        }
        if (syndFeed.getImage() != null) {
            response.setImage(syndFeed.getImage().getUrl());
        }
        response.setFavicon(HttpUtil.extractFavicon(response.getUrl()));
        response.setSubscribed(subscribed);
        return response;
    }
}
