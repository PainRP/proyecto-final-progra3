package com.progra3.app.repository;

import com.progra3.treeengine.model.Node;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryTreeRepository implements TreeRepository {
    
    private final Map<String, Node> storage = new ConcurrentHashMap<>();
    private String rootId;

    public Node save(Node node) {
        storage.put(node.getId(), node);
        return node;
    }

    public Node saveChild(String parentId, Node childNode) {
        storage.put(childNode.getId(), childNode);
        return childNode;
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
