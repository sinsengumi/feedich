package net.sinsengumi.feedich.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import io.sentry.Sentry;
import io.sentry.event.User;
import io.sentry.event.UserBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.infrastructure.Env;
import net.sinsengumi.feedich.model.FeedichOAuth2User;
import net.sinsengumi.feedich.util.AppUtil;
import net.sinsengumi.feedich.util.HttpUtil;

@Slf4j
@Service
@AllArgsConstructor
public class ErrorService {

    private final Env env;

    public void error(String errorCode, Throwable e, HttpServletRequest request, FeedichOAuth2User user) {
        if (errorCode == null) {
            errorCode = AppUtil.createErrorCode();
        }

        MDC.put("errorCode", errorCode);

        if (user != null) {
            User sentryUser = new UserBuilder()
                    .setId(Integer.toString(user.getId()))
                    .setEmail(user.getEmail())
                    .setIpAddress(HttpUtil.getRemoteAddr(request))
                    .build();
            Sentry.getContext().setUser(sentryUser);
        }

        Sentry.getContext().addTag("environment", env.toString());

        log.error("[{}] {}", errorCode, e.getMessage(), e);

        MDC.remove("errorCode");
    }

    public void error(String errorCode, Throwable e) {
        error(errorCode, e, null, null);
    }
}
