package com.progra3.app.repository.memory;

import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "memory", matchIfMissing = true)
public class InMemoryTreeRepository implements TreeRepository {

    private final Map<String, Node> storage = new ConcurrentHashMap<>();
    private String rootId;

    public Node save(Node node) {
        storage.put(node.getId(), node);
        return node;
    }

    @Override
    public Node saveChild(String parentId, Node childNode) {
        childNode.setParentId(parentId);
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
        Map<String, Node> snapshot = new HashMap<>();

        for (Map.Entry<String, Node> entry : storage.entrySet()) {
            snapshot.put(entry.getKey(), copyNode(entry.getValue()));
        }

        return snapshot;
    }

    private Node copyNode(Node source) {
        if (source == null) {
            return null;
        }

        Node copy = new Node(
                source.getId(),
                source.getCode(),
                source.getName(),
                source.getType(),
                source.getDescription()
        );
        copy.setParentId(source.getParentId());

        // Copia profunda de hijos (aunque se espera lista vacia en nodos planos).
        List<Node> copiedChildren = new ArrayList<>();
        if (source.getChildren() != null) {
            for (Node child : source.getChildren()) {
                copiedChildren.add(copyNode(child));
            }
        }
        copy.setChildren(copiedChildren);

        return copy;
    }
}
