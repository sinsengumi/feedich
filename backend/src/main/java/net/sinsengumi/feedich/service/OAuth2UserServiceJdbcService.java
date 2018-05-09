package net.sinsengumi.feedich.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.User;
import net.sinsengumi.feedich.model.User.ServiceProvider;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceJdbcService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        log.info("user = {}", oauth2User);
        ServiceProvider serviceProvider = ServiceProvider.of(userRequest.getClientRegistration().getRegistrationId());

        String email = serviceProvider.getEmail(attributes);
        String authId = serviceProvider.getId(attributes);

        User user = userService.findByEmail(email);
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(serviceProvider.getName(attributes));
            newUser.setAuthId(serviceProvider, authId);
            userService.create(newUser);
        } else {
            userService.updateAuthId(user.getId(), serviceProvider, authId);
        }
        return new FeedichOAuth2User(oauth2User.getAuthorities(), oauth2User.getAttributes(), oauth2User.getName(),
                user.getId(), user.getEmail(), user.getName());
    }
}
