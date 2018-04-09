package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Feed;

@Repository
public interface FeedRepository {

    int create(Feed feed);

    List<Feed> findAll();

    Feed findByFeedUrl(String feedUrl);

    List<Feed> findByFeedIds(@Param("feedIds") List<Integer> feedIds);
}
