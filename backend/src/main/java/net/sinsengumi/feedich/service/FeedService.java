package net.sinsengumi.feedich.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.repository.FeedRepository;

@Service
@AllArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public int create(Feed feed) {
        return feedRepository.create(feed);
    }

    public int updateUpdatedAt(int id, Date updatedAt) {
        return feedRepository.updateUpdatedAt(id, updatedAt);
    }

    public List<Feed> findByAll() {
        return feedRepository.findAll();
    }

    public Feed findByFeedUrl(String feedUrl) {
        return feedRepository.findByFeedUrl(feedUrl);
    }
}
