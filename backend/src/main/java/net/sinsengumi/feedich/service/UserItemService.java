package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.repository.UserItemRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserItemService {

    private final UserItemRepository userItemRepository;

    public int create(List<UserItem> userItems) {
        return userItemRepository.create(userItems);
    }

    public int read(int userId, int itemId) {
        return userItemRepository.read(userId, itemId);
    }

    public int unread(int userId, int itemId) {
        return userItemRepository.unread(userId, itemId);
    }
}
