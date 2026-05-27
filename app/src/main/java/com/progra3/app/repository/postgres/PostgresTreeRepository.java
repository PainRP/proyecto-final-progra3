package com.progra3.app.repository.postgres;

import com.progra3.app.entity.NodeEntity;
import com.progra3.app.repository.TreeRepository;
import com.progra3.treeengine.model.Node;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "postgres")
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
        // El repositorio devuelve nodos planos, sin hijos anidados.
        return toFlatNode(savedEntity);
    }

    @Override
    public Node saveChild(String parentId, Node childNode) {
        Long parentLongId = parseId(parentId);

        if (parentLongId == null) {
            throw new IllegalArgumentException("Id de padre invalido");
        }

        NodeEntity parent = jpaNodeRepository.findById(parentLongId)
                .orElseThrow(() -> new IllegalArgumentException("Padre no encontrado"));

        NodeEntity child = toEntity(childNode);
        child.setParent(parent);

        NodeEntity savedChild = jpaNodeRepository.save(child);
        // El repositorio devuelve nodos planos, sin hijos anidados.
        return toFlatNode(savedChild);
    }

    @Override
    @Transactional(readOnly = true)
    public Node findById(String id) {
        Long longId = parseId(id);

        if (longId == null) {
            return null;
        }

        Optional<NodeEntity> entity = jpaNodeRepository.findById(longId);
        return entity.map(this::toFlatNode).orElse(null);
    }

    @Override
    public void setRootId(String rootId) {
    }

    @Override
    @Transactional(readOnly = true)
    public String getRootId() {
        List<NodeEntity> roots = jpaNodeRepository.findByParentIsNull();

        if (roots.isEmpty()) {
            return null;
        }

        return String.valueOf(roots.get(0).getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Node> findAll() {
        Map<String, Node> flatNodes = new HashMap<>();

        List<NodeEntity> entities = jpaNodeRepository.findAll();

        for (NodeEntity entity : entities) {
            Node node = toFlatNode(entity);
            flatNodes.put(node.getId(), node);
        }

        return flatNodes;
    }

    private NodeEntity toEntity(Node node) {
        NodeEntity entity = new NodeEntity();

        Long longId = parseId(node.getId());

        if (longId != null) {
            entity.setId(longId);
        }

        entity.setCode(node.getCode());
        entity.setName(node.getName());
        entity.setType(node.getType());
        entity.setDescription(node.getDescription());

        return entity;
    }


    private Node toFlatNode(NodeEntity entity) {
        Node node = new Node(
                String.valueOf(entity.getId()),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getDescription()
        );

        if (entity.getParent() != null && entity.getParent().getId() != null) {
            node.setParentId(String.valueOf(entity.getParent().getId()));
        }

        return node;
    }

    private Long parseId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        try {
            return Long.valueOf(id);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}