package net.sinsengumi.feedich;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.sentry.spring.SentryServletContextInitializer;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SentryConfig {
    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new SentryServletContextInitializer();
    }
}
