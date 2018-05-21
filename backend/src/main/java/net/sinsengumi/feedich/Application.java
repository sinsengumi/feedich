package net.sinsengumi.feedich;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import net.sinsengumi.feedich.infrastructure.Env;
import net.sinsengumi.feedich.infrastructure.FQCNBeanNameGenerator;

@SpringBootApplication
@ComponentScan(nameGenerator = FQCNBeanNameGenerator.class)
public class Application {

    @Autowired
    Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Env env() {
        String activeProfile = environment.getActiveProfiles()[0];
        return Env.valueOf(activeProfile);
    }
}
