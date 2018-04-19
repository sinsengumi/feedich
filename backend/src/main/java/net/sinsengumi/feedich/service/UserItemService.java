package net.sinsengumi.feedich.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.UserItem;
import net.sinsengumi.feedich.repository.UserItemRepository;

@Service
@AllArgsConstructor
public class UserItemService {

    private final UserItemRepository userItemRepository;

    public int create(List<UserItem> userItems) {
        return userItemRepository.create(userItems);
    }

    public List<UserItem> findUnreadItems(int userId, int feedId) {
        return userItemRepository.findUnreadItems(userId, feedId);
    }

    public UserItem findByUserIdAndItemId(int userId, int itemId) {
        return userItemRepository.findByUserIdAndItemId(userId, itemId);
    }

    public int read(int userId, int itemId) {
        return userItemRepository.read(userId, itemId);
    }

    public int unread(int userId, int itemId) {
        return userItemRepository.unread(userId, itemId);
    }

    public int deleteByfeedId(int userId, int feedId) {
        return userItemRepository.deleteByfeedId(userId, feedId);
    }
}
