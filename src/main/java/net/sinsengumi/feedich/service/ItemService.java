package net.sinsengumi.feedich.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.Item;
import net.sinsengumi.feedich.repository.ItemRepository;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public int create(Item item) {
        return itemRepository.create(item);
    }

    public Item findByUrl(String url) {
        return itemRepository.findByUrl(url);
    }
}
