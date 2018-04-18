package net.sinsengumi.feedich.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.repository.ItemRepository;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public int create(Item item) {
        return itemRepository.createOne(item);
    }

    public int create(List<Item> items) {
        return itemRepository.create(items);
    }

    public Item findByUrl(int feedId, String url) {
        return itemRepository.findByUrl(feedId, url);
    }

    public List<Item> findByFeedId(int feedId, Date createdAt) {
        return itemRepository.findByFeedId(feedId, createdAt);
    }
}
