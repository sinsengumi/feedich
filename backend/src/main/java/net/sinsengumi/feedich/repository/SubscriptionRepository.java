package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Subscription;

@Repository
public interface SubscriptionRepository {

    int create(Subscription subscription);

    List<Subscription> findByUserId(int userId);

    Subscription findByUserIdAndFeedId(@Param("userId") int userId, @Param("feedId") int feedId);

    List<Integer> getSubscribeUsers(int feedId);
}
