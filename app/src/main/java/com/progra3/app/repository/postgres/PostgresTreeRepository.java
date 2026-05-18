package com.progra3.app.repository.postgres;

import com.progra3.app.entity.NodeEntity;
import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("postgres")
@Transactional
public class PostgresTreeRepository implements TreeRepository {

    private final JpaNodeRepository jpaNodeRepository;

    public PostgresTreeRepository(JpaNodeRepository jpaNodeRepository) {
        this.jpaNodeRepository = jpaNodeRepository;
    }

    @Override
    public Node save(Node node) {
        NodeEntity entity = toEntity(node);
        NodeEntity savedEntity = jpaNodeRepository.save(entity);
        return toNode(savedEntity);
    }

    @Override
    public Node saveChild(String parentId, Node childNode) {
        NodeEntity parent = jpaNodeRepository.findById(Long.valueOf(parentId))
                .orElseThrow(() -> new IllegalArgumentException("Padre no encontrado"));

        NodeEntity child = toEntity(childNode);
        child.setParent(parent);

        NodeEntity savedChild = jpaNodeRepository.save(child);
        return toNode(savedChild);
    }

    @Override
    @Transactional(readOnly = true)
    public Node findById(String id) {
        if (id == null) {
            return null;
        }

        try {
            Optional<NodeEntity> entity = jpaNodeRepository.findById(Long.valueOf(id));
            return entity.map(this::toNode).orElse(null);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    @Override
    public void setRootId(String rootId) {
    }

    @Override
    @Transactional(readOnly = true)
    public String getRootId() {
        return jpaNodeRepository.findByParentIsNull()
                .map(entity -> String.valueOf(entity.getId()))
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Node> findAll() {
        Map<String, Node> nodes = new HashMap<>();

        for (NodeEntity entity : jpaNodeRepository.findAll()) {
            Node node = toNode(entity);
            nodes.put(node.getId(), node);
        }

        return nodes;
    }

    private NodeEntity toEntity(Node node) {
        NodeEntity entity = new NodeEntity();

        if (node.getId() != null && !node.getId().isBlank()) {
            try {
                entity.setId(Long.valueOf(node.getId()));
            } catch (NumberFormatException exception) {
                entity.setId(null);
            }
        }

        entity.setCode(node.getCode());
        entity.setName(node.getName());
        entity.setType(node.getType());
        entity.setDescription(node.getDescription());

        return entity;
    }

    private Node toNode(NodeEntity entity) {
        Node node = new Node(
                String.valueOf(entity.getId()),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getDescription()
        );

        for (NodeEntity child : entity.getChildren()) {
            node.addChild(toNode(child));
        }

        return node;
    }
}