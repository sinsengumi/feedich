package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Feed.FeedStatus;
import net.sinsengumi.feedich.repository.FeedRepository;

@Service
@AllArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public int create(Feed feed) {
        return feedRepository.create(feed);
    }

    public int update(Feed feed) {
        return feedRepository.update(feed);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateStatus(int id, FeedStatus status) {
        return feedRepository.updateStatus(id, status);
    }

    public List<Feed> findByAll() {
        return feedRepository.findAll();
    }

    public Feed findByFeedUrl(String feedUrl) {
        return feedRepository.findByFeedUrl(feedUrl);
    }

    public List<Feed> getRecommendFeeds(int userId, int size) {
        return feedRepository.getRecommendFeeds(userId, size);
    }
}
