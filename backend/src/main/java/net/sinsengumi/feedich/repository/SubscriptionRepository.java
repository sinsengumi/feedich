package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Subscription;

@Repository
public interface SubscriptionRepository {

    int create(Subscription subscription);

    int delete(int id);

    Subscription findById(int id);

    List<Subscription> findByUserId(int userId);

    Subscription findByUserIdAndFeedId(@Param("userId") int userId, @Param("feedId") int feedId);

    List<Integer> getSubscribeUsers(int feedId);

    boolean subscribed(@Param("userId") int userId, @Param("feedUrl") String feedUrl);

    int deleteByUserId(int userId);
}
