package net.sinsengumi.feedich.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Feed;

@Repository
public interface FeedRepository {

    int create(Feed feed);

    int updateUpdatedAt(@Param("id") int id, @Param("updatedAt") Date updatedAt);

    List<Feed> findAll();

    Feed findByFeedUrl(String feedUrl);
}
