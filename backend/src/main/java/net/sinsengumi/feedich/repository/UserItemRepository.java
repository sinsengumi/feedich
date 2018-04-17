package net.sinsengumi.feedich.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.UserItem;

@Repository
public interface UserItemRepository {

    int create(@Param("userItems") List<UserItem> userItems);

    UserItem findByUserIdAndItemId(@Param("userId") int userId, @Param("itemId") int itemId);

    int read(@Param("userId") int userId, @Param("itemId") int itemId);

    int unread(@Param("userId") int userId, @Param("itemId") int itemId);

    int deleteByfeedId(@Param("userId") int userId, @Param("feedId") int feedId);
}