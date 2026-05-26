package com.progra3.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

import com.progra3.app.service.TreeOrchestratorService;
import com.progra3.treeengine.model.Node;

@Deprecated
public class TreeFrontendController {

    private final TreeOrchestratorService treeService;

    public TreeFrontendController(TreeOrchestratorService treeService) {
        this.treeService = treeService;
    }

    @GetMapping("/api/tree")
    public Map<String, Object> getTree() {
        Node root = treeService.getFullTree();

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Arbol cargado correctamente desde el backend");
        response.put("storage", "mongo");
        response.put("treeStrategy", "custom");
        response.put("root", root);

        return response;
    }
}