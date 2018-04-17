package net.sinsengumi.feedich;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import net.sinsengumi.feedich.repository.FeedRepository;

@Configuration
@MapperScan(basePackageClasses = FeedRepository.class)
@ImportResource("classpath:transaction-context.xml")
public class DatabaseConfig {
}
