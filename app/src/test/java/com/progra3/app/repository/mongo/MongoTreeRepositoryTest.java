package com.progra3.app.repository.mongo;

import com.progra3.treeengine.model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "app.storage=mongo")
class MongoTreeRepositoryTest {

    @Autowired
    private MongoTreeRepository mongoTreeRepository;

    @Autowired
    private MongoNodeRepository mongoNodeRepository;

    @Autowired
    private MongoRootRepository mongoRootRepository;

    @BeforeEach
    void cleanDatabase() {
        mongoNodeRepository.deleteAll();
        mongoRootRepository.deleteAll();
    }

    @Test
    void shouldSaveRootAndChildrenWithCorrectParentIds() {
        Node root = buildTree();

        mongoTreeRepository.save(root);
        mongoTreeRepository.setRootId(root.getId());

        NodeDocument rootDocument = mongoNodeRepository.findById("1").orElse(null);
        NodeDocument childADocument = mongoNodeRepository.findById("2").orElse(null);
        NodeDocument childBDocument = mongoNodeRepository.findById("3").orElse(null);
        NodeDocument grandChildDocument = mongoNodeRepository.findById("4").orElse(null);

        assertNotNull(rootDocument);
        assertNotNull(childADocument);
        assertNotNull(childBDocument);
        assertNotNull(grandChildDocument);

        assertNull(rootDocument.getParentId());
        assertEquals("1", childADocument.getParentId());
        assertEquals("1", childBDocument.getParentId());
        assertEquals("2", grandChildDocument.getParentId());

        assertEquals("1", mongoTreeRepository.getRootId());
    }

    @Test
    void shouldRecoverHydratedTreeUsingFindById() {
        Node root = buildTree();

        mongoTreeRepository.save(root);

        Node recovered = mongoTreeRepository.findById("1");

        assertNotNull(recovered);
        assertEquals("1", recovered.getId());
        assertEquals("Raiz", recovered.getName());
        assertNotNull(recovered.getChildren());
        assertEquals(2, recovered.getChildren().size());

        Node childA = recovered.getChildren().get(0);
        assertEquals("2", childA.getId());
        assertEquals("Hijo A", childA.getName());
        assertEquals(1, childA.getChildren().size());
        assertEquals("4", childA.getChildren().get(0).getId());
    }

    @Test
    void shouldReturnFlatMapUsingFindAll() {
        Node root = buildTree();

        mongoTreeRepository.save(root);

        Map<String, Node> flatNodes = mongoTreeRepository.findAll();

        assertEquals(4, flatNodes.size());
        assertTrue(flatNodes.containsKey("1"));
        assertTrue(flatNodes.containsKey("2"));
        assertTrue(flatNodes.containsKey("3"));
        assertTrue(flatNodes.containsKey("4"));

        assertEquals(0, flatNodes.get("1").getChildren().size());
        assertEquals(0, flatNodes.get("2").getChildren().size());
    }

    private Node buildTree() {
        Node root = new Node("1", "ROOT", "Raiz", "ROOT", "Nodo raiz");

        Node childA = new Node("2", "A", "Hijo A", "FOLDER", "Primer hijo");
        Node childB = new Node("3", "B", "Hijo B", "FOLDER", "Segundo hijo");
        Node grandChildA = new Node("4", "A1", "Nieto A1", "LEAF", "Nieto del hijo A");

        childA.addChild(grandChildA);

        root.addChild(childA);
        root.addChild(childB);

        return root;
    }
}