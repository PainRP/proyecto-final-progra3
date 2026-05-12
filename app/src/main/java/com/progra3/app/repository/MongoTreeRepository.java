package com.progra3.app.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.progra3.treeengine.model.Node;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoTreeRepository implements TreeRepository {

    private static final String ROOT_CONFIG_ID = "ROOT";

    private final MongoNodeRepository mongoNodeRepository;
    private final MongoRootRepository mongoRootRepository;

    public MongoTreeRepository(MongoNodeRepository mongoNodeRepository, MongoRootRepository mongoRootRepository) {
        this.mongoNodeRepository = mongoNodeRepository;
        this.mongoRootRepository = mongoRootRepository;
    }

    @Override
    public Node save(Node node) {
        saveFlatNode(node, null);
        return findById(node.getId());
    }

    private void saveFlatNode(Node node, String parentId) {
        String finalParentId = parentId;

        if (finalParentId == null) {
            finalParentId = mongoNodeRepository.findById(node.getId())
                    .map(NodeDocument::getParentId)
                    .orElse(null);
        }

        mongoNodeRepository.save(new NodeDocument(node, finalParentId));

        if (node.getChildren() != null) {
            for (Node child : node.getChildren()) {
                saveFlatNode(child, node.getId());
            }
        }
    }

    @Override
    public Node findById(String id) {
        return findAll().get(id);
    }

    @Override
    public void setRootId(String rootId) {
        RootDocument config = mongoRootRepository.findById(ROOT_CONFIG_ID)
                .orElse(new RootDocument(ROOT_CONFIG_ID, rootId));

        config.setRootId(rootId);
        mongoRootRepository.save(config);
    }

    @Override
    public String getRootId() {
        return mongoRootRepository.findById(ROOT_CONFIG_ID)
                .map(RootDocument::getRootId)
                .orElse(null);
    }

    @Override
    public Map<String, Node> findAll() {
        Map<String, Node> nodes = new HashMap<>();
        Map<String, String> parentByNode = new HashMap<>();

        for (NodeDocument document : mongoNodeRepository.findAll()) {
            Node node = document.toNode();
            node.setChildren(new ArrayList<>());

            nodes.put(node.getId(), node);
            parentByNode.put(node.getId(), document.getParentId());
        }

        for (Map.Entry<String, String> entry : parentByNode.entrySet()) {
            String nodeId = entry.getKey();
            String parentId = entry.getValue();

            if (parentId != null && nodes.containsKey(parentId)) {
                Node parent = nodes.get(parentId);
                Node child = nodes.get(nodeId);

                List<Node> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }

                children.add(child);
            }
        }

        return nodes;
    }
}