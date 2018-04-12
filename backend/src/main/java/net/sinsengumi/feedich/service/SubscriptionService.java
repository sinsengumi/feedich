package net.sinsengumi.feedich.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.repository.SubscriptionRepository;

@Slf4j
@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final FeedService feedService;
    private final FeedDiscoverer feedDiscoverer;
    private final ItemService itemService;
    private final UserItemService userItemService;

    public List<Subscription> findByUserId(int userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription subscribe(int userId, String feedUrl) {
        // feed に存在しなかったら登録しておく。
        Feed feed = feedService.findByFeedUrl(feedUrl);
        if (feed == null) {
            SyndFeed syndFeed = feedDiscoverer.parseFeed(feedUrl);
            feed = Feed.build(feedUrl, syndFeed);
            feedService.create(feed);
        }

        Subscription subscription = subscriptionRepository.findByUserIdAndFeedId(userId, feed.getId());
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setFeedId(feed.getId());
            subscriptionRepository.create(subscription);

            // 初回クロール
            firstCrawl(userId, feed);
        }

        return subscription;
    }

    private void firstCrawl(int userId, Feed feed) {
        int feedId = feed.getId();
        String feedUrl = feed.getFeedUrl();

        SyndFeed syndFeed = feedDiscoverer.parseFeed(feedUrl);
        List<SyndEntry> entries = syndFeed.getEntries();

        if (!entries.isEmpty()) {
            for (SyndEntry e : entries) {
                final Item item = itemService.findByUrl(feedId, e.getLink());
                if (item == null) {
                    // log.info("New Item");
                    // item が登録されていない場合
                    // item を登録して、他の購読者の未読に追加する
                    Item newItem = Item.build(feedId, new Date(), e);
                    itemService.create(newItem);

                    List<Integer> subscribeUsers = getSubscribeUsers(feedId);
                    List<UserItem> userItems = subscribeUsers.stream()
                            .map(uid -> UserItem.build(uid, newItem))
                            .collect(Collectors.toList());
                    userItemService.create(userItems);
                } else {
                    log.info("Already registered Item");
                    // すでに item が登録されている場合
                    userItemService.create(Arrays.asList(UserItem.build(userId, item)));
                }
            }
        }
    }

    public List<Integer> getSubscribeUsers(int feedId) {
        return subscriptionRepository.getSubscribeUsers(feedId);
    }
}
