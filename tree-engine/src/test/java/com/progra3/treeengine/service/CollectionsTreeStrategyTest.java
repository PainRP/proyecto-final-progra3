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

    @Test
    void shouldBuildTreeWithSystemRootZeroCorrectly() {
        CollectionsTreeStrategy strategy = new CollectionsTreeStrategy();

        Node root = new Node("100", "0", "Plan de Cuentas General", "SISTEMA", "Raiz unica");
        Node childA = new Node("1", "1", "Activo", "GRUPO", "Activo");
        Node childB = new Node("2", "2", "Pasivo", "GRUPO", "Pasivo");
        Node grandChildA = new Node("3", "1.1", "Activo Corriente", "GRUPO", "Activo Corriente");

        Map<String, Node> flatNodes = new LinkedHashMap<>();
        flatNodes.put(root.getId(), root);
        flatNodes.put(childA.getId(), childA);
        flatNodes.put(childB.getId(), childB);
        flatNodes.put(grandChildA.getId(), grandChildA);

        Node result = strategy.buildFullTree(flatNodes);

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals("100", result.getId());
        org.junit.jupiter.api.Assertions.assertEquals("0", result.getCode());
        org.junit.jupiter.api.Assertions.assertEquals(2, result.getChildren().size());
        
        Node resultChildA = result.getChildren().stream()
                .filter(n -> "1".equals(n.getId()))
                .findFirst()
                .orElse(null);
        org.junit.jupiter.api.Assertions.assertNotNull(resultChildA);
        org.junit.jupiter.api.Assertions.assertEquals(1, resultChildA.getChildren().size());
        org.junit.jupiter.api.Assertions.assertEquals("3", resultChildA.getChildren().get(0).getId());
    }

    @Test
    void shouldHaveIdenticalTraversalBetweenBothStrategies() {
        CollectionsTreeStrategy collectionsStrategy = new CollectionsTreeStrategy();
        CustomTreeStrategy customStrategy = new CustomTreeStrategy();

        // Create nodes out of order in HashMap
        Node root = new Node("100", "0", "Plan de Cuentas General", "SISTEMA", "Raiz unica");
        Node childA = new Node("1", "1", "Activo", "GRUPO", "Activo");
        Node childB = new Node("2", "2", "Pasivo", "GRUPO", "Pasivo");
        Node grandChildA = new Node("3", "1.1", "Activo Corriente", "GRUPO", "Activo Corriente");
        Node grandChildB = new Node("4", "1.2", "Activo No Corriente", "GRUPO", "Activo No Corriente");
        Node leafA = new Node("5", "1.1.1", "Caja", "CUENTA", "Caja");
        Node leafB = new Node("6", "1.1.2", "Bancos", "CUENTA", "Bancos");

        // Set up parent-child links for custom strategy (database style)
        childA.addChild(grandChildA);
        childA.addChild(grandChildB);
        grandChildA.addChild(leafA);
        grandChildA.addChild(leafB);
        root.addChild(childA);
        root.addChild(childB);

        // Put nodes in a HashMap in an arbitrary (non-sorted) order to test order recovery
        Map<String, Node> flatNodes = new java.util.HashMap<>();
        flatNodes.put(leafB.getId(), leafB);
        flatNodes.put(leafA.getId(), leafA);
        flatNodes.put(grandChildB.getId(), grandChildB);
        flatNodes.put(grandChildA.getId(), grandChildA);
        flatNodes.put(childB.getId(), childB);
        flatNodes.put(childA.getId(), childA);
        flatNodes.put(root.getId(), root);

        Node collectionsRoot = collectionsStrategy.buildFullTree(flatNodes);

        // Setup separate copies of nodes for Custom Strategy, or reuse but let's make sure
        // we test their buildFullTree. CustomTreeStrategy takes a map where parent-child links are set.
        Node rootC = new Node("100", "0", "Plan de Cuentas General", "SISTEMA", "Raiz unica");
        Node childAC = new Node("1", "1", "Activo", "GRUPO", "Activo");
        Node childBC = new Node("2", "2", "Pasivo", "GRUPO", "Pasivo");
        Node grandChildAC = new Node("3", "1.1", "Activo Corriente", "GRUPO", "Activo Corriente");
        Node grandChildBC = new Node("4", "1.2", "Activo No Corriente", "GRUPO", "Activo No Corriente");
        Node leafAC = new Node("5", "1.1.1", "Caja", "CUENTA", "Caja");
        Node leafBC = new Node("6", "1.1.2", "Bancos", "CUENTA", "Bancos");

        // Link in arbitrary order
        grandChildAC.addChild(leafBC);
        grandChildAC.addChild(leafAC);
        childAC.addChild(grandChildBC);
        childAC.addChild(grandChildAC);
        rootC.addChild(childBC);
        rootC.addChild(childAC);

        Map<String, Node> flatNodesC = new java.util.HashMap<>();
        flatNodesC.put(leafBC.getId(), leafBC);
        flatNodesC.put(leafAC.getId(), leafAC);
        flatNodesC.put(grandChildBC.getId(), grandChildBC);
        flatNodesC.put(grandChildAC.getId(), grandChildAC);
        flatNodesC.put(childBC.getId(), childBC);
        flatNodesC.put(childAC.getId(), childAC);
        flatNodesC.put(rootC.getId(), rootC);

        Node customRoot = customStrategy.buildFullTree(flatNodesC);

        // Run DFS on both
        List<TreeTraversalNode> dfsCollections = collectionsStrategy.getTraversal(collectionsRoot, "DFS");
        List<TreeTraversalNode> dfsCustom = customStrategy.getTraversal(customRoot, "DFS");

        org.junit.jupiter.api.Assertions.assertEquals(dfsCustom.size(), dfsCollections.size());
        for (int i = 0; i < dfsCustom.size(); i++) {
            org.junit.jupiter.api.Assertions.assertEquals(dfsCustom.get(i).getNode().getCode(), dfsCollections.get(i).getNode().getCode());
        }

        // Run BFS on both
        List<TreeTraversalNode> bfsCollections = collectionsStrategy.getTraversal(collectionsRoot, "BFS");
        List<TreeTraversalNode> bfsCustom = customStrategy.getTraversal(customRoot, "BFS");

        org.junit.jupiter.api.Assertions.assertEquals(bfsCustom.size(), bfsCollections.size());
        for (int i = 0; i < bfsCustom.size(); i++) {
            org.junit.jupiter.api.Assertions.assertEquals(bfsCustom.get(i).getNode().getCode(), bfsCollections.get(i).getNode().getCode());
        }
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

