package com.progra3.app.controller;

import com.progra3.app.controller.dto.AppInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"", "/api"})
public class AppInfoController {

    private final String storage;
    private final String strategy;

    public AppInfoController(
            @Value("${app.storage:memory}") String storage,
            @Value("${app.tree-strategy:custom}") String strategy
    ) {
        this.storage = storage;
        this.strategy = strategy;
    }

    @GetMapping("/info")
    public AppInfo getInfo() {
        AppInfo info = new AppInfo();
        info.setEngine(storage);
        info.setStrategy(strategy);
        return info;
    }
}

