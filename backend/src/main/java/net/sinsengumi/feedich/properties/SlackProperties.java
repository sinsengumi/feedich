package net.sinsengumi.feedich.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Data
@Component
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    private Api api = new Api();
    private Channel channel = new Channel();

    @Data
    @ToString(exclude = { "token" })
    public class Api {
        private String token;
    }

    @Data
    public class Channel {
        private String notify;
        private String alert;
    }
}
