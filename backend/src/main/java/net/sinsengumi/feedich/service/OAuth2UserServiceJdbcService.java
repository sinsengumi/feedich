package net.sinsengumi.feedich.service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
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
        ServiceProvider serviceProvider = ServiceProvider.of(userRequest.getClientRegistration().getRegistrationId());

        String authId = serviceProvider.getId(attributes);
        String email = getEmail(serviceProvider, oauth2User, userRequest.getAccessToken().getTokenValue());

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

    private String getEmail(ServiceProvider serviceProvider, OAuth2User oauth2User, String accessToken) {
        String email = serviceProvider.getEmail(oauth2User.getAttributes());
        if (StringUtils.isNotEmpty(email)) {
            return email;
        }

        if (serviceProvider == ServiceProvider.Github) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));

            GitHubEmail[] emails = restTemplate.getForObject("https://api.github.com/user/emails", GitHubEmail[].class);
            return Stream.of(emails).filter(e -> e.primary).map(e -> e.email).findFirst().orElseThrow(() -> {
                OAuth2Error oauth2Error = new OAuth2Error("missing_user_email", "Missing required email", null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            });
        }
        return null;
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, body);
            }
        };
    }

    @Value
    @ToString
    private static class GitHubEmail {
        private String email;
        private boolean verified;
        private boolean primary;
        private String visibility;
    }
}
