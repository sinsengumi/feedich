package net.sinsengumi.feedich.model;

import static java.util.Comparator.*;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.rometools.rome.feed.synd.SyndEntry;
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
    private FeedStatus status = FeedStatus.NORMAL;
    private Date publishedAt;
    private Date createdAt;
    private Date updatedAt;

    public static Feed build(SyndFeed syndFeed) {
        Feed feed = new Feed();
        feed.setTitle(StringUtils.trim(syndFeed.getTitle()));
        feed.setDescription(StringUtils.trim(syndFeed.getDescription()));
        feed.setUrl(StringUtils.trim(syndFeed.getLink()));
        feed.setFeedUrl(StringUtils.trim(syndFeed.getUri()));
        feed.setFeedType(StringUtils.trim(syndFeed.getFeedType()));
        if (syndFeed.getIcon() != null) {
            feed.setIcon(StringUtils.trim(syndFeed.getIcon().getUrl()));
        }
        if (syndFeed.getImage() != null) {
            feed.setImage(StringUtils.trim(syndFeed.getImage().getUrl()));
        }
        feed.setFavicon(HttpUtil.extractFavicon(feed.getUrl()));
        feed.setStatus(FeedStatus.NORMAL);

        if (syndFeed.getPublishedDate() == null) {
            Comparator<SyndEntry> comparator = nullsLast(comparing(SyndEntry::getPublishedDate).reversed());
            Optional<Date> entryLatestPublishedDate = syndFeed.getEntries().stream().sorted(comparator).map(SyndEntry::getPublishedDate).findFirst();
            if (entryLatestPublishedDate.isPresent()) {
                feed.setPublishedAt(entryLatestPublishedDate.get());
            }
        } else {
            feed.setPublishedAt(syndFeed.getPublishedDate());
        }
        return feed;
    }

    public static Feed build(int id, SyndFeed syndFeed) {
        Feed feed = build(syndFeed);
        feed.setId(id);
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
        response.setStatus(status);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);
        response.setPublishedAt(publishedAt);
        return response;
    }

    public enum FeedStatus {
        NORMAL,
        BROKEN
    }
}
