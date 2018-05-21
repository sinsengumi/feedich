package net.sinsengumi.feedich;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.exception.ApplicationException;
import net.sinsengumi.feedich.properties.SlackProperties;

@Configuration
@AllArgsConstructor
public class SlackConfig {

    private final SlackProperties slackProperties;

    @Bean(destroyMethod = "disconnect")
    public SlackSession slackSession() {
        try {
            SlackSession session = SlackSessionFactory.createWebSocketSlackSession(slackProperties.getApi().getToken());
            session.connect();
            return session;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
