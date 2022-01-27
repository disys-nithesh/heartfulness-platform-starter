package org.heartfulness.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import({ BlogPostGRPCServiceImpl.class })
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class StarterApp {
    public static void main(String[] args) {
        SpringApplication.run(StarterApp.class, args);
    }
}
