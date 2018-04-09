package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.repository.FeedRepository;

@Slf4j
@Service
@AllArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public int create(Feed feed) {
        return feedRepository.create(feed);
    }

    public List<Feed> findByAll() {
        return feedRepository.findAll();
    }

    public Feed findByFeedUrl(String feedUrl) {
        return feedRepository.findByFeedUrl(feedUrl);
    }

    public List<Feed> findByFeedIds(List<Integer> feedIds) {
        return feedRepository.findByFeedIds(feedIds);
    }
}
