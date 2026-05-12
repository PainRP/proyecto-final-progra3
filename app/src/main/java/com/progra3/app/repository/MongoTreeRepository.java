package com.progra3.app.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.progra3.treeengine.model.Node;

@Repository
@Primary
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
        NodeDocument document = new NodeDocument(node);
        NodeDocument saved = mongoNodeRepository.save(document);
        return saved.toNode();
    }

    @Override
    public Node findById(String id) {
        return mongoNodeRepository.findById(id)
                .map(NodeDocument::toNode)
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
            nodes.put(node.getId(), node);
        }

        return nodes;
    }
}