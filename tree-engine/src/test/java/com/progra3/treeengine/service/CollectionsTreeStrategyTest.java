package com.progra3.treeengine.service;

import com.progra3.treeengine.model.Node;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsTreeStrategyTest {

    @Test
    void shouldRunDFSCorrectly() {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();
        Node root = buildTree();

        List<TreeTraversalNode> result = strategy.getTraversal(root, "DFS");

        org.junit.jupiter.api.Assertions.assertEquals(5, result.size());
        org.junit.jupiter.api.Assertions.assertEquals("1", result.get(0).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("2", result.get(1).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("4", result.get(2).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("5", result.get(3).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("3", result.get(4).getNode().getId());
    }

    @Test
    void shouldRunBFSCorrectly() {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();
        Node root = buildTree();

        List<TreeTraversalNode> result = strategy.getTraversal(root, "BFS");

        org.junit.jupiter.api.Assertions.assertEquals(5, result.size());
        org.junit.jupiter.api.Assertions.assertEquals("1", result.get(0).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("2", result.get(1).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("3", result.get(2).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("4", result.get(3).getNode().getId());
        org.junit.jupiter.api.Assertions.assertEquals("5", result.get(4).getNode().getId());
    }

    @Test
    void shouldBuildFullTreeWhenMapHasRootNode() {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();

        Node root = new Node("1", "ROOT", "Raiz", "ROOT", "Nodo raiz");

        Map<String, Node> flatNodes = new LinkedHashMap<>();
        flatNodes.put(root.getId(), root);

        Node result = strategy.buildFullTree(flatNodes);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals("1", result.getId());
        org.junit.jupiter.api.Assertions.assertEquals("Raiz", result.getName());
    }

    @Test
    void shouldReturnNullWhenBuildFullTreeReceivesEmptyMap() {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();

        Node result = strategy.buildFullTree(new LinkedHashMap<>());

        org.junit.jupiter.api.Assertions.assertNull(result);
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

