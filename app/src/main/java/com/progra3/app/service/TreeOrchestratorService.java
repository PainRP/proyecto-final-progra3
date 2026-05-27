package com.progra3.app.service;

import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import com.progra3.treeengine.service.TreeAlgorithmStrategy;
import com.progra3.treeengine.service.TreeTraversalNode;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TreeOrchestratorService {

    private final TreeRepository repository;
    private final TreeAlgorithmStrategy strategy;

    public TreeOrchestratorService(TreeRepository repository, TreeAlgorithmStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    public Node createRoot(Node rootNode) {
        Node processedNode = strategy.createRoot(rootNode);
        Node savedNode = repository.save(processedNode);
        repository.setRootId(savedNode.getId());
        return savedNode;
    }

    public Node addChild(String parentId, Node childNode) {
        Node parent = repository.findById(parentId);

        if (parent == null) {
            throw new IllegalArgumentException("Padre no encontrado");
        }

        Node processedChild = strategy.addChild(parent, childNode);
        return repository.saveChild(parentId, processedChild);
    }

    public List<Node> getChildren(String parentId) {
        Map<String, Node> flatNodes = repository.findAll();

        if (flatNodes.isEmpty()) {
            return new ArrayList<>();
        }

        // El arbol se construye en la estrategia, no en el repositorio.
        Node root = strategy.buildFullTree(flatNodes);
        Node parent = strategy.getSubtree(root, parentId);

        if (parent == null || parent.getChildren() == null) {
            return new ArrayList<>();
        }

        return parent.getChildren();
    }


    public Node getFullTree() {
        Map<String, Node> flatNodes = repository.findAll();

        if (flatNodes.isEmpty()) {
            return null;
        }

        return strategy.buildFullTree(flatNodes);
    }

    public Node getSubtree(String nodeId) {
        Node root = getFullTree();

        if (root == null) {
            return null;
        }

        return strategy.getSubtree(root, nodeId);
    }

    public List<Node> getPathFromRoot(String nodeId) {
        Node root = getFullTree();

        if (root == null) {
            return new ArrayList<>();
        }

        return strategy.getPathFromRoot(root, nodeId);
    }

    public List<TreeTraversalNode> getTraversal(String type) {
        Node root = getFullTree();

        if (root == null) {
            return new ArrayList<>();
        }

        return strategy.getTraversal(root, type);
    }

    public int getHeight() {
        Node root = getFullTree();

        if (root == null) {
            return 0;
        }

        return strategy.getHeight(root);
    }

    public boolean hasCycle() {
        Node root = getFullTree();

        if (root == null) {
            return false;
        }

        return strategy.hasCycle(root);
    }

    public int getDepth(String nodeId) {
        Node root = getFullTree();

        if (root == null) {
            return 0;
        }

        return strategy.getDepth(root, nodeId);
    }

    public List<Node> getAncestors(String nodeId) {
        Node root = getFullTree();

        if (root == null) {
            return new ArrayList<>();
        }

        return strategy.getAncestors(root, nodeId);
    }
}