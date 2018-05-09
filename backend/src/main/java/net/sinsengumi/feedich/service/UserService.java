package net.sinsengumi.feedich.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.model.User;
import net.sinsengumi.feedich.model.User.ServiceProvider;
import net.sinsengumi.feedich.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int create(User user) {
        return userRepository.create(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int updateAuthId(int id, ServiceProvider serviceProvider, String authId) {
        return userRepository.updateAuthId(id, serviceProvider, authId);
    }
}
