package com.progra3.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.progra3.treeengine.model.Node;
import com.progra3.treeengine.service.CollectionsTreeStrategy;
import com.progra3.treeengine.service.CustomTreeStrategy;
import com.progra3.treeengine.service.TreeAlgorithmStrategy;

class DualEngineStrategyFunctionalTest {

    private final TreeAlgorithmStrategy customStrategy = new CustomTreeStrategy();
    private final TreeAlgorithmStrategy collectionsStrategy = new CollectionsTreeStrategy();

    @Test
    void shouldReturnSameResultsForCustomAndCollectionsStrategies() {
        Node root = buildTree();

        assertEquals(collectionsStrategy.hasCycle(root), customStrategy.hasCycle(root));
        assertEquals(collectionsStrategy.getHeight(root), customStrategy.getHeight(root));
        assertEquals(collectionsStrategy.getDepth(root, "5"), customStrategy.getDepth(root, "5"));
    }

    private Node buildTree() {
        Node root = new Node("1", "ROOT", "Raiz", "ROOT", "Nodo raiz");

        Node childA = new Node("2", "A", "Hijo A", "FOLDER", "Primer hijo");
        Node childB = new Node("3", "B", "Hijo B", "FOLDER", "Segundo hijo");

        Node grandChildA = new Node("4", "A1", "Nieto A1", "LEAF", "Nieto del hijo A");
        Node grandChildB = new Node("5", "A2", "Nieto A2", "LEAF", "Nieto del hijo A");

        childA.addChild(grandChildA);
        childA.addChild(grandChildB);

        root.addChild(childA);
        root.addChild(childB);

        return root;
    }
}
