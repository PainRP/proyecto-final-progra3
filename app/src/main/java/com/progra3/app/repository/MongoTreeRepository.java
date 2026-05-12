package com.progra3.app.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.progra3.treeengine.model.Node;

@Repository
@Primary
public class MongoTreeRepository implements TreeRepository {

    private final MongoNodeRepository mongoNodeRepository;
    private String rootId;

    public MongoTreeRepository(MongoNodeRepository mongoNodeRepository) {
        this.mongoNodeRepository = mongoNodeRepository;
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
        this.rootId = rootId;
    }

    @Override
    public String getRootId() {
        return rootId;
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
}sss