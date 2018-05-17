package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.UserItem;

@Repository
public interface UserItemRepository {

    int create(@Param("userItems") List<UserItem> userItems);

    List<UserItem> findUnreadItems(@Param("userId") int userId, @Param("feedId") int feedId);

    UserItem findByUserIdAndItemId(@Param("userId") int userId, @Param("itemId") int itemId);

    int read(@Param("userId") int userId, @Param("itemId") int itemId);

    int unread(@Param("userId") int userId, @Param("itemId") int itemId);

    int deleteByFeedId(@Param("userId") int userId, @Param("feedId") int feedId);

    int deleteByUserId(int userId);
}
