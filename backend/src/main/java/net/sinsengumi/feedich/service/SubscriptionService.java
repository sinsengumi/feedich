package net.sinsengumi.feedich.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public Subscription findById(int id) {
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> findByUserId(int userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription findByUserIdAndFeedId(int userId, int feedId) {
        return subscriptionRepository.findByUserIdAndFeedId(userId, feedId);
    }

    public Subscription subscribe(int userId, String feedUrl) {
        // feed に存在しなかったら登録しておく。
        Feed feed = feedService.findByFeedUrl(feedUrl);
        SyndFeed syndFeed = null;
        if (feed == null) {
            syndFeed = feedDiscoverer.parseFeed(feedUrl);
            feed = Feed.build(syndFeed);
            feedService.create(feed);
        } else {
            syndFeed = feedDiscoverer.parseFeed(feedUrl);
        }

        Subscription subscription = findByUserIdAndFeedId(userId, feed.getId());
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setFeedId(feed.getId());
            subscriptionRepository.create(subscription);

            // 初回クロール（他の人の item も更新するので、速度優先で FeedCrawlService.crawl は使わない）
            firstCrawl(userId, feed, syndFeed);
        }

        return findById(subscription.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription subscribeOtherTransaction(int userId, String feedUrl) {
        return subscribe(userId, feedUrl);
    }

    public int unsubscribe(int id, int userId, int feedId) {
        int result = subscriptionRepository.delete(id);
        userItemService.deleteByFeedId(userId, feedId);
        return result;
    }

    private void firstCrawl(int userId, Feed feed, SyndFeed syndFeed) {
        int feedId = feed.getId();
        List<SyndEntry> entries = syndFeed.getEntries();

        if (!entries.isEmpty()) {
            Date now = new Date();

            for (SyndEntry e : entries) {
                final Item item = itemService.findByUrl(feedId, e.getLink());
                if (item == null) {
                    // item が登録されていない場合
                    // item を登録して、他の購読者の未読に追加する
                    Item newItem = Item.build(feedId, now, e);
                    itemService.create(newItem);

                    List<Integer> subscribeUsers = getSubscribeUsers(feedId).stream()
                            .filter(id -> id != userId)
                            .collect(Collectors.toList());
                    if (!subscribeUsers.isEmpty()) {
                        List<UserItem> userItems = subscribeUsers.stream()
                                .map(uid -> UserItem.build(uid, newItem))
                                .collect(Collectors.toList());
                        userItemService.createAsync(userItems);
                    }

                    // 自分のも登録する（同期）
                    userItemService.create(UserItem.build(userId, newItem));
                } else {
                    // すでに item が登録されている場合
                    userItemService.create(UserItem.build(userId, item));
                }
            }

            feedService.update(Feed.build(syndFeed));
        }
    }

    public List<Integer> getSubscribeUsers(int feedId) {
        return subscriptionRepository.getSubscribeUsers(feedId);
    }

    public boolean subscribed(int userId, String feedUrl) {
        return subscriptionRepository.subscribed(userId, feedUrl);
    }

    @Async
    public void deleteByUserId(int userId) {
        subscriptionRepository.deleteByUserId(userId);
    }
}
