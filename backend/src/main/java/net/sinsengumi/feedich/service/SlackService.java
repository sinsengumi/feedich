package net.sinsengumi.feedich.service;

import org.springframework.stereotype.Service;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sinsengumi.feedich.properties.SlackProperties;

@Slf4j
@Service
@AllArgsConstructor
public class SlackService {

    private final SlackProperties properties;
    private final SlackSession session;

    public void notify(String message) {
        try {
            SlackChannel channel = session.findChannelByName(properties.getChannel().getNotify());
            SlackAttachment attachment = new SlackAttachment();
            attachment.setColor("#28a745");
            attachment.setText(message);
            session.sendMessage(channel, null, attachment);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
