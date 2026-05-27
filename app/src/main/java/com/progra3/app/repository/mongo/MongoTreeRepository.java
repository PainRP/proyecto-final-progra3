package com.progra3.app.repository.mongo;

import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Node saveChild(String parentId, Node childNode) {
        saveFlatNode(childNode, parentId);
        return findById(childNode.getId());
    }

    private void saveFlatNode(Node node, String parentId) {
        String finalParentId = parentId;

        if (finalParentId == null) {
            finalParentId = mongoNodeRepository.findById(node.getId())
                    .map(NodeDocument::getParentId)
                    .orElse(null);
        }

        // El repositorio solo persiste nodos planos.
        mongoNodeRepository.save(new NodeDocument(node, finalParentId));

        // Si llegan hijos, se persisten como nodos planos con su parentId.
        if (node.getChildren() != null) {
            for (Node child : node.getChildren()) {
                saveFlatNode(child, node.getId());
            }
        }
    }

    @Override
    public Node findById(String id) {
        return mongoNodeRepository.findById(id)
                .map(document -> {
                    Node node = document.toNode();
                    node.setParentId(document.getParentId());
                    // Devolvemos un nodo plano: hijos vacios para evitar anidado en el repo.
                    node.setChildren(new ArrayList<>());


                    return node;
                })
                .orElse(null);
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

        for (NodeDocument document : mongoNodeRepository.findAll()) {
            Node node = document.toNode();
            node.setParentId(document.getParentId());
            node.setChildren(new ArrayList<>());
            nodes.put(node.getId(), node);
        }

        return nodes;
    }
}