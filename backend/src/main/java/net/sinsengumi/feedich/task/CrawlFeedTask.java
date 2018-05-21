package net.sinsengumi.feedich.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.service.FeedCrawlService;
import net.sinsengumi.feedich.service.FeedService;
import net.sinsengumi.feedich.service.SlackService;
import net.sinsengumi.feedich.util.StopWatchUtil;

@Slf4j
@Component
@AllArgsConstructor
public class CrawlFeedTask {

    private final FeedService feedService;
    private final FeedCrawlService feedCrawlService;
    private final SlackService slackService;

    @Scheduled(cron = "0 */15 * ? * *")
    public void crawl() {
        StopWatchUtil stopWatch = new StopWatchUtil();
        List<Feed> feeds = feedService.findByAll();

        String message = String.format("Begin crawl. feedSize = %d", feeds.size());
        notify(message);

        List<Feed> failedFeeds = new ArrayList<>();

        for (Feed feed : feeds) {
            try {
                feedCrawlService.crawl(feed);
            } catch (Exception e) {
                failedFeeds.add(feed);
                log.warn("Failed crawl feed. id = {}, url = {}", feed.getId(), feed.getFeedUrl(), e);
            }
        }

        if (!failedFeeds.isEmpty()) {
            log.error("Crawl failed size = {}", failedFeeds.size());
        }

        message = String.format("End crawl. elapsed = %.3f (s)", stopWatch.getTotalTimeSeconds());
        notify(message);
    }

    private void notify(String message) {
        log.info(message);
        slackService.notify(message);
    }
}
