package net.sinsengumi.feedich.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.exception.ApplicationException;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.model.UserItem;

@Slf4j
@Service
@AllArgsConstructor
public class FeedCrawlService {

    private final FeedService feedService;
    private final ItemService itemService;
    private final SubscriptionService subscriptionService;
    private final UserItemService userItemService;

    public void crawl() {
        // クラスタリングするときはここを分割する
        List<Feed> feeds = feedService.findByAll();
        log.info("feedSize = {}", feeds.size());
        feeds.forEach(feed -> {
            try {
                crawl(feed);
            } catch (Exception e) {
                log.error("Failed crawl feed. id = {}, url = {}", feed.getId(), feed.getFeedUrl(), e);
            }
        });
    }

    public void crawl(Feed feed) {
        int feedId = feed.getId();
        String feedUrl = feed.getFeedUrl();
        log.info("Crawl id = {}, feedUrl = {}", feedId, feedUrl);

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(new URL(feedUrl)));
            List<SyndEntry> entries = syndFeed.getEntries();
            log.info("item size = {}, feedUrl = {}", entries.size(), feedUrl);

            Date now = new Date();
            List<SyndEntry> rejectedEntries = rejectDuplicated(feed, entries);
            log.info("rejectedItem size = {}, feedUrl = {}", rejectedEntries.size(), feedUrl);

            if (!rejectedEntries.isEmpty()) {
                List<Item> items = rejectedEntries.stream()
                        .map(e -> Item.build(feedId, now, e))
                        .collect(Collectors.toList());
                itemService.create(items);

                List<Item> createdItems = itemService.findByFeedId(feedId, DateUtils.addSeconds(now, -1));
                log.info("createdItems size = {}, feedUrl = {}", createdItems.size(), feedUrl);

                List<Integer> subscribeUsers = subscriptionService.getSubscribeUsers(feedId);
                log.info("subscribeUsers size = {}, feedUrl = {}", subscribeUsers.size(), feedUrl);

                List<UserItem> userItems = subscribeUsers.stream().flatMap(userId -> {
                    return createdItems.stream().map(i -> UserItem.build(userId, i));
                }).collect(Collectors.toList());

                userItemService.create(userItems);

                // TODO: feed 情報を更新
                feedService.updateUpdatedAt(feedId, now);
            }
        } catch (IllegalArgumentException | FeedException | IOException e) {
            throw new ApplicationException(e);
        }
    }

    private List<SyndEntry> rejectDuplicated(Feed feed, List<SyndEntry> entries) {
        return entries.stream()
                .filter(e -> itemService.findByUrl(feed.getId(), e.getLink()) == null)
                .collect(Collectors.toList());
    }
}