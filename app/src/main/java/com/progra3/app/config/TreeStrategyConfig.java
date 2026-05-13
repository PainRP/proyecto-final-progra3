package com.progra3.app.config;

import com.progra3.treeengine.service.CollectionsTreeStrategy;
import com.progra3.treeengine.service.CustomTreeStrategy;
import com.progra3.treeengine.service.TreeAlgorithmStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TreeStrategyConfig {

    @Bean
    @Profile("memory")
    public TreeAlgorithmStrategy memoryTreeStrategy() {
        return new CustomTreeStrategy();
    }

    @Bean
    @Profile("mongo")
    public TreeAlgorithmStrategy mongoTreeStrategy() {
        return new CustomTreeStrategy();
    }

    @Bean
    @Profile("postgres")
    public TreeAlgorithmStrategy postgresTreeStrategy() {
        return new CollectionsTreeStrategy();
    }
}
