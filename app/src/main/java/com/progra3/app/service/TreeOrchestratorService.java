package com.progra3.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import com.progra3.treeengine.service.CustomTreeStrategy;
import com.progra3.treeengine.service.TreeAlgorithmStrategy;

@Service
public class TreeOrchestratorService {

    private final TreeRepository repository;
    private final TreeAlgorithmStrategy strategy;

    public TreeOrchestratorService(TreeRepository repository) {
        this.repository = repository;
        this.strategy = new CustomTreeStrategy();
    }

    public Node createRoot(Node rootNode) {
        Node processedNode = strategy.createRoot(rootNode);
        repository.setRootId(processedNode.getId());
        return repository.save(processedNode);
    }

    public Node addChild(String parentId, Node childNode) {
        Node parent = repository.findById(parentId);
        if (parent == null) {
            throw new IllegalArgumentException("Padre no encontrado");
        }

        Node updatedChild = strategy.addChild(parent, childNode);

        repository.save(parent);
        return repository.save(updatedChild);
    }

    public List<Node> getChildren(String parentId) {
        Node parent = repository.findById(parentId);
        if (parent != null) {
            return parent.getChildren();
        }
        return new ArrayList<>();
    }

    public Node getFullTree() {
        String rootId = repository.getRootId();
        if (rootId == null) {
            return null;
        }
        return repository.findById(rootId);
    }
}