package net.sinsengumi.feedich.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.User;
import net.sinsengumi.feedich.model.User.ServiceProvider;
import net.sinsengumi.feedich.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final UserItemService userItemService;
    private final PinService pinService;

    public int create(User user) {
        return userRepository.create(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int updateAuthId(int id, ServiceProvider serviceProvider, String authId) {
        return userRepository.updateAuthId(id, serviceProvider, authId);
    }

    public int withdraw(int id) {
        log.info("Withdraw userId = {}", id);
        int result = userRepository.deleteById(id);

        // async delete
        subscriptionService.deleteByUserId(id);
        userItemService.deleteByUserId(id);
        pinService.clearAsync(id);

        return result;
    }
}
