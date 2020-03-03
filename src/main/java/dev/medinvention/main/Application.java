package dev.medinvention.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "dev.medinvention.job", "dev.medinvention.controller", "dev.medinvention.listener", "dev.medinvention.tax" })
@EnableJpaRepositories("dev.medinvention.dao")
@EntityScan("dev.medinvention.entity")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
