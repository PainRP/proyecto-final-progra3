package com.progra3.app.controller;

import com.progra3.app.service.TreeOrchestratorService;
import com.progra3.treeengine.model.Node;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class TreeController {

    private final TreeOrchestratorService service;

    public TreeController(TreeOrchestratorService service) {
        this.service = service;
    }

    @PostMapping("/nodes/root")
    public Node createRoot(@RequestBody Node rootNode) {
        return service.createRoot(rootNode);
    }

    @PostMapping("/nodes/{parentId}/children")
    public Node addChild(@PathVariable String parentId, @RequestBody Node childNode) {
        return service.addChild(parentId, childNode);
    }

    @GetMapping("/tree/{nodeId}")
    public List<Node> getChildren(@PathVariable String nodeId) {
        return service.getChildren(nodeId);
    }
    
    @GetMapping("/tree")
    public Node getFullTree() {
        return service.getFullTree();
    }
}
