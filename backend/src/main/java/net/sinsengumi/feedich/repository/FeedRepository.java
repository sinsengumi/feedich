package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Feed;
import net.sinsengumi.feedich.model.Feed.FeedStatus;

@Repository
public interface FeedRepository {

    int create(Feed feed);

    int update(Feed feed);

    int updateStatus(@Param("id") int id, @Param("status") FeedStatus status);

    List<Feed> findAll();

    Feed findByFeedUrl(String feedUrl);

    List<Feed> getRecommendFeeds(@Param("userId") int userId, @Param("size") int size);
}
