package net.sinsengumi.feedich.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Item;

@Repository
public interface ItemRepository {

    int createOne(Item item);

    int create(@Param("items") List<Item> items);

    Item findById(int id);

    Item findByUrl(@Param("feedId") int feedId, @Param("url") String url);

    List<Item> findByFeedId(@Param("feedId") int feedId, @Param("createdAt") Date createdAt);

    List<Item> getUnreadItem(@Param("userId") int userId, @Param("feedId") int feedId);
}
