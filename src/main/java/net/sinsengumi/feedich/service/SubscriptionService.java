package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Subscription;
import net.sinsengumi.feedich.repository.SubscriptionRepository;

@Slf4j
@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final FeedService feedService;
    private final FeedDiscoverer feedDiscoverer;

    public List<Subscription> subscriptions(int userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription subscribe(int userId, String feedUrl) {
        // feeds に存在しなかったら登録しておく。
        Feed feed = feedService.findByFeedUrl(feedUrl);
        if (feed == null) {
            feed = feedDiscoverer.discover(feedUrl).get(0);
            feedService.create(feed);
        }

        Subscription subscription = subscriptionRepository.findByUserIdAndFeedId(userId, feed.getId());
        if (subscription == null) {
            subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setFeedId(feed.getId());
            subscriptionRepository.create(subscription);
        }

        return subscription;
    }
}
