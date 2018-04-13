package net.sinsengumi.feedich.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.UnreadFeed;

@Repository
public interface FeedRepository {

    int create(Feed feed);

    Feed findById(int id);

    List<Feed> findAll();

    Feed findByFeedUrl(String feedUrl);

    List<UnreadFeed> getUnreadFeeds(int userId);
}
