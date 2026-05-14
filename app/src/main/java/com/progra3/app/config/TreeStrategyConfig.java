package com.progra3.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.progra3.treeengine.service.CollectionsTreeStrategy;
import com.progra3.treeengine.service.CustomTreeStrategy;
import com.progra3.treeengine.service.TreeAlgorithmStrategy;

@Configuration
public class TreeStrategyConfig {

    private static final Logger log = LoggerFactory.getLogger(TreeStrategyConfig.class);

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "custom", matchIfMissing = true)
    public TreeAlgorithmStrategy customTreeStrategy() {
        log.info("Cargando estrategia de arbol: custom");
        return new CustomTreeStrategy();
    }

    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "collections")
    public TreeAlgorithmStrategy collectionsTreeStrategy() {
        log.info("Cargando estrategia de arbol: collections");
        return new CollectionsTreeStrategy();
    }
}