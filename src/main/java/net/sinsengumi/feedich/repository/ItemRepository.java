package net.sinsengumi.feedich.repository;

import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Item;

@Repository
public interface ItemRepository {

    int create(Item item);

    Item findByUrl(String url);
}
