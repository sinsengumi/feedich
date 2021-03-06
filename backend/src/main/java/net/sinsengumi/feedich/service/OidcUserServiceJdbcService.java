package net.sinsengumi.feedich.service;

import java.util.Map;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.model.User;
import net.sinsengumi.feedich.model.User.ServiceProvider;

@Slf4j
@RequiredArgsConstructor
@Service
public class OidcUserServiceJdbcService extends OidcUserService {

    private final UserService userService;
    private final SlackService slackService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info("oidcUser = {}", oidcUser);

        Map<String, Object> attributes = oidcUser.getAttributes();
        ServiceProvider serviceProvider = ServiceProvider.of(userRequest.getClientRegistration().getRegistrationId());

        String authId = serviceProvider.getId(attributes);
        String email = serviceProvider.getEmail(attributes);

        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(serviceProvider.getName(attributes));
            user.setAuthId(serviceProvider, authId);
            userService.create(user);
            slackService.notify("Registered new user. id = " + user.getId());
        } else {
            userService.updateAuthId(user.getId(), serviceProvider, authId);
        }

        return new FeedichOAuth2User(oidcUser.getName(), oidcUser.getAuthorities(), attributes, oidcUser.getIdToken(),
                user.getId(), user.getEmail(), user.getName());
    }
}
