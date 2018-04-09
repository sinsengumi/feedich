package net.sinsengumi.feedich.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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

@Slf4j
@Service
@AllArgsConstructor
public class FeedCrawlService {

    private final FeedService feedService;
    private final ItemService itemService;

    public void crawl() {
        List<Feed> feeds = feedService.findByAll();
        feeds.forEach(feed -> crawl(feed));
    }

    public void crawl(Feed feed) {
        log.info("Crawl id = {}, feedUrl = {}", feed.getId(), feed.getUrl());

        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed syndFeed = input.build(new XmlReader(new URL(feed.getUrl())));
            List<SyndEntry> entries = syndFeed.getEntries();
            log.info("Feed size = {}, url = {}", entries.size(), feed.getUrl());

            List<SyndEntry> rejectedEntries = rejectDuplicated(feed, entries);
            rejectedEntries.stream()
                .map(e -> Item.build(feed.getId(), e))
                .forEach(item -> itemService.create(item));
        } catch (IllegalArgumentException | FeedException | IOException e) {
            throw new ApplicationException(e);
        }
    }

    private List<SyndEntry> rejectDuplicated(Feed feed, List<SyndEntry> entries) {
        return entries.stream()
                .filter(e -> itemService.findByUrl(e.getLink()) != null)
                .collect(Collectors.toList());
    }

}
