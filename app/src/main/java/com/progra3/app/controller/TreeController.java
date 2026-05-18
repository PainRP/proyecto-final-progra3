package com.progra3.app.controller;

import com.progra3.app.controller.api.NodesApi;
import com.progra3.app.controller.api.TreeApi;
import com.progra3.app.controller.dto.DepthResponse;
import com.progra3.app.controller.dto.HeightResponse;
import com.progra3.app.controller.dto.Node;
import com.progra3.app.controller.dto.NodeRequest;
import com.progra3.app.controller.dto.TreeNode;
import com.progra3.app.controller.dto.ValidationResponse;
import com.progra3.app.service.TreeOrchestratorService;
import com.progra3.treeengine.service.TreeTraversalNode;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TreeController implements NodesApi, TreeApi {

    private final TreeOrchestratorService service;

    public TreeController(TreeOrchestratorService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Node> createRoot(NodeRequest nodeRequest) {
        com.progra3.treeengine.model.Node rootNode = mapToModel(nodeRequest);
        com.progra3.treeengine.model.Node created = service.createRoot(rootNode);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(created, null));
    }

    @Override
    public ResponseEntity<Node> addChild(String parentId, NodeRequest nodeRequest) {
        com.progra3.treeengine.model.Node childNode = mapToModel(nodeRequest);
        com.progra3.treeengine.model.Node created = service.addChild(parentId, childNode);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(created, parentId));
    }

    @Override
    public ResponseEntity<TreeNode> getFullTree() {
        com.progra3.treeengine.model.Node root = service.getFullTree();
        if (root == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToTreeDto(root, null));
    }

    @Override
    public ResponseEntity<TreeNode> getSubtree(String nodeId) {
        com.progra3.treeengine.model.Node node = service.getSubtree(nodeId);
        if (node == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToTreeDto(node, null));
    }

    @Override
    public ResponseEntity<List<Node>> getPath(String nodeId) {
        List<com.progra3.treeengine.model.Node> path = service.getPathFromRoot(nodeId);
        if (path.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapPathToDto(path));
    }

    @Override
    public ResponseEntity<List<Node>> getAncestors(String nodeId) {
        List<com.progra3.treeengine.model.Node> path = service.getPathFromRoot(nodeId);
        if (path.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<com.progra3.treeengine.model.Node> ancestors = path.subList(0, path.size() - 1);
        return ResponseEntity.ok(mapPathToDto(ancestors));
    }

    @Override
    public ResponseEntity<DepthResponse> getDepth(String nodeId) {
        List<com.progra3.treeengine.model.Node> path = service.getPathFromRoot(nodeId);
        int depth = path.isEmpty() ? 0 : path.size() - 1;
        return ResponseEntity.ok(new DepthResponse(depth));
    }

    @Override
    public ResponseEntity<List<Node>> getTraversal(String type) {
        List<Node> result = new ArrayList<>();
        for (TreeTraversalNode traversalNode : service.getTraversal(type)) {
            result.add(mapToDto(traversalNode.getNode(), traversalNode.getParentId()));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<HeightResponse> getHeight() {
        return ResponseEntity.ok(new HeightResponse(service.getHeight()));
    }

    @Override
    public ResponseEntity<ValidationResponse> validateNoCycles() {
        boolean hasCycle = service.hasCycle();
        ValidationResponse response = new ValidationResponse(!hasCycle,
                hasCycle ? "Se detectaron ciclos en el arbol." : "El arbol es valido.");
        return ResponseEntity.ok(response);
    }

    private com.progra3.treeengine.model.Node mapToModel(NodeRequest request) {
        String id = UUID.randomUUID().toString();
        return new com.progra3.treeengine.model.Node(
                id,
                request.getCode(),
                request.getName(),
                request.getType(),
                request.getDescription()
        );
    }

    private Node mapToDto(com.progra3.treeengine.model.Node model, String parentId) {
        Node dto = new Node();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setName(model.getName());
        dto.setType(model.getType());
        dto.setDescription(model.getDescription());
        if (parentId != null) {
            dto.setParentId(JsonNullable.of(parentId));
        }
        return dto;
    }

    private TreeNode mapToTreeDto(com.progra3.treeengine.model.Node model, String parentId) {
        TreeNode dto = new TreeNode();
        dto.setId(model.getId());
        dto.setCode(model.getCode());
        dto.setName(model.getName());
        dto.setType(model.getType());
        dto.setDescription(model.getDescription());
        if (parentId != null) {
            dto.setParentId(JsonNullable.of(parentId));
        }

        List<com.progra3.treeengine.model.Node> children = model.getChildren();
        if (children != null && !children.isEmpty()) {
            List<TreeNode> mappedChildren = new ArrayList<>();
            for (com.progra3.treeengine.model.Node child : children) {
                mappedChildren.add(mapToTreeDto(child, model.getId()));
            }
            dto.setChildren(mappedChildren);
        }

        return dto;
    }

    private List<Node> mapPathToDto(List<com.progra3.treeengine.model.Node> path) {
        List<Node> result = new ArrayList<>();
        String parentId = null;
        for (com.progra3.treeengine.model.Node node : path) {
            result.add(mapToDto(node, parentId));
            parentId = node.getId();
        }
        return result;
    }
}
