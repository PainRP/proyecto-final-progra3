package com.progra3.app.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.progra3.treeengine.model.Node;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "memory", matchIfMissing = true)
public class InMemoryTreeRepository implements TreeRepository {

    private final Map<String, Node> storage = new ConcurrentHashMap<>();
    private String rootId;

    public Node save(Node node) {
        storage.put(node.getId(), node);
        return node;
    }

    public Node findById(String id) {
        return storage.get(id);
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getRootId() {
        return rootId;
    }

    public Map<String, Node> findAll() {
        return storage;
    }
}